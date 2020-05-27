package com.haozi.id.generator.core.buffer;

import com.haozi.id.generator.common.ServiceThread;
import com.haozi.id.generator.core.IdGeneratorFactory;
import com.haozi.id.generator.core.rule.RuntimeSequence;
import com.haozi.id.generator.core.rule.repository.SequenceEnum;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import com.haozi.id.generator.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 生产ID
 * <p>
 * 根据ID规则条件判断是否需要向缓冲区补充，如果补充创建runnable任务，由线程池执行。
 * <p>
 * 定时任务补充ID，当消费量大时，一次补充不能满足消费需要，在生产ID后再次调用productIfAbsent(),验证是否
 * 补充完成，如果仍不足，继续补充。
 *
 * @author haozi
 * @date 2019-11-0811:46
 */
@Slf4j
public class ProductIdBuffer extends ServiceThread {
    //200毫秒 TODO 加到配置项
    private final static Long DEFAULT_WAIT_INTERVAL = 200L;

    //ID生成池
    private ExecutorService generateThreadPool = Executors.newCachedThreadPool();

    //多线程生产任务信号量，防止重复生产
    private Map<SequenceRule, ProducerStatus> sequenceRuleSemaphore = new ConcurrentHashMap<>();

    //生产状态
    enum ProducerStatus {
        //缺失中
        MISSING,
        //生产中
        PRODUCING
    }

    private IdGeneratorFactory idGeneratorFactory;

    public ProductIdBuffer(IdGeneratorFactory idGeneratorFactory) {
        this.idGeneratorFactory = idGeneratorFactory;
        init();
    }

    private void init() {
        idGeneratorFactory.getSequenceRuleService()
                .getRunningRule()
                .stream()
                .map(rule -> idGeneratorFactory.getSequenceRuleService().getSequenceRuntime(rule, SequenceEnum.Runtime.NOW))
                .forEach(this::product);
    }

    /**
     * 如果缺少，生产ID
     *
     * @param sequenceRule
     * @return
     */
    private <T> void productIfAbsent(SequenceRule sequenceRule) {
        RuntimeSequence runtimeSequence = idGeneratorFactory.getSequenceRuleService().getSequenceRuntime(sequenceRule, SequenceEnum.Runtime.NOW);
        String sequenceKey = runtimeSequence.getSequenceKey();
        BlockingQueue<T> queue = BufferPool.getBuffer(sequenceKey);
        //未到阈值比例
        if (queue != null && queue.size() > sequenceRule.getReloadThreshold()) {
            return;
        }
        log.info("productIfAbsent Thread={} queueSize={}", Thread.currentThread().getName(), queue == null ? 0 : queue.size());
        //防止重复补充
        if (sequenceRuleSemaphore.putIfAbsent(sequenceRule, ProducerStatus.MISSING) == null) {
            generateThreadPool.submit(() -> {
                sequenceRuleSemaphore.put(sequenceRule, ProducerStatus.PRODUCING);
                this.product(runtimeSequence);
                sequenceRuleSemaphore.remove(sequenceRule);
                //补充后再次验证，预防定时任务补充不及时问题
                this.productIfAbsent(sequenceRule);
            });
        }
    }

    /**
     * 执行生产ID逻辑
     *
     * @param runtimeSequence
     * @param <T>
     */
    protected <T> void product(RuntimeSequence runtimeSequence) {
        long startTime = System.currentTimeMillis();
        String sequenceKey = runtimeSequence.getSequenceKey();
        SequenceRule sequenceRule = runtimeSequence.getSequenceRule();
        BlockingQueue<T> queue = BufferPool.getBuffer(sequenceKey);
        if (queue == null) {
            //不固定容量，为支持运行时动态修改
            queue = new LinkedBlockingQueue<T>();
            BufferPool.putIfAbsent(sequenceKey, queue);
            log.info("init KEY：{}, RuntimeSequence:{}", sequenceKey, runtimeSequence);
        }
        //补充条数
        int v = sequenceRule.getMemoryCapacity() - queue.size();
        //步长
        int increment = sequenceRule.getIncrement();
        Long offset = idGeneratorFactory.getSequenceRuleService().updateAndGetOffset(sequenceKey, v * sequenceRule.getIncrement(), sequenceRule.getInitialValue());

        //内存初始值
        Long initValue = offset - v;
        for (Long i = initValue; i < offset; i += increment) {
            try {
                queue.put(IdUtil.generateId(i, runtimeSequence));
            } catch (InterruptedException e) {
                log.error("product Thread:{} exception model:{} , offset:{}", Thread.currentThread().getName(), increment, offset, e);
                Thread.currentThread().interrupt();
            }
        }
        log.info("product Thread:{} key:{}, offset: {} -> {},total cost：{}ms", Thread.currentThread().getName(), sequenceKey, initValue, offset, (System.currentTimeMillis() - startTime));
    }


    @Override
    public String getServiceName() {
        return "Id-Producer-Thread";
    }

    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        while (!this.isStopped()) {
            this.waitForRunning(DEFAULT_WAIT_INTERVAL);

            idGeneratorFactory.getSequenceRuleService()
                    .getRunningRule()
                    .forEach(this::productIfAbsent);
        }

        log.info(this.getServiceName() + " service end");
    }
}
