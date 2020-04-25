package com.haozi.id.generator.core.id;

import com.haozi.id.generator.core.sequence.SequenceEnum;
import com.haozi.id.generator.core.sequence.SequenceRuntime;
import com.haozi.id.generator.core.sequence.dao.SequenceRuleDefinition;
import com.haozi.id.generator.core.util.IdUtil;
import com.haozi.id.generator.core.util.ServiceThread;
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
public class IdProducer extends ServiceThread {
    //200毫秒 TODO 加到配置项
    private final static Long DEFAULT_WAIT_INTERVAL = 200L;

    //ID生成池
    private ExecutorService generateThreadPool = Executors.newCachedThreadPool();

    //多线程生产任务信号量，防止重复生产
    private Map<SequenceRuleDefinition, ProducerStatusEnum> sequenceRuleSemaphore = new ConcurrentHashMap<>();

    //生产状态
    enum ProducerStatusEnum {
        //缺失中
        MISSING,
        //生产中
        PRODUCING
    }

    private IdFactory idFactory;

    public IdProducer(IdFactory idFactory) {
        this.idFactory = idFactory;
        init();
    }

    private void init() {
        idFactory.getSequenceService()
                .getRunningRule()
                .stream()
                .map(rule -> idFactory.getSequenceService().getSequenceRuntime(rule, SequenceEnum.Runtime.NOW))
                .forEach(this::product);
    }

    /**
     * 如果缺少，生产ID
     *
     * @param sequenceRuleDefinition
     * @return
     */
    private <T> void productIfAbsent(SequenceRuleDefinition sequenceRuleDefinition) {
        SequenceRuntime sequenceRuntime = idFactory.getSequenceService().getSequenceRuntime(sequenceRuleDefinition, SequenceEnum.Runtime.NOW);
        String sequenceKey = sequenceRuntime.getSequenceKey();
        BlockingQueue<T> queue = IdBuffer.getBuffer(sequenceKey);
        //未到阈值比例
        if (queue != null && queue.size() > sequenceRuleDefinition.getReloadThresholdSize()) {
            return;
        }
        log.info("productIfAbsent Thread={} queueSize={}", Thread.currentThread().getName(), queue == null ? 0 : queue.size());
        //防止重复补充
        if (sequenceRuleSemaphore.putIfAbsent(sequenceRuleDefinition, ProducerStatusEnum.MISSING) == null) {
            generateThreadPool.submit(() -> {
                sequenceRuleSemaphore.put(sequenceRuleDefinition, ProducerStatusEnum.PRODUCING);
                this.product(sequenceRuntime);
                sequenceRuleSemaphore.remove(sequenceRuleDefinition);
                //补充后再次验证，预防定时任务补充不及时问题
                this.productIfAbsent(sequenceRuleDefinition);
            });
        }
    }

    /**
     * 执行生产ID逻辑
     *
     * @param sequenceRuntime
     * @param <T>
     */
    protected <T> void product(SequenceRuntime sequenceRuntime) {
        long startTime = System.currentTimeMillis();
        String sequenceKey = sequenceRuntime.getSequenceKey();
        SequenceRuleDefinition sequenceRuleDefinition = sequenceRuntime.getSequenceRuleDefinition();
        BlockingQueue<T> queue = IdBuffer.getBuffer(sequenceKey);
        if (queue == null) {
            //不固定容量，为支持运行时动态修改
            queue = new LinkedBlockingQueue<T>();
            IdBuffer.putIfAbsent(sequenceKey, queue);
            log.info("init KEY：{}, RuntimeSequence:{}", sequenceKey, sequenceRuntime);
        }
        //补充条数
        int v = sequenceRuleDefinition.getMemoryCapacity() - queue.size();
        //步长
        int increment = sequenceRuleDefinition.getIncrement();
        Long offset = idFactory.getSequenceService().updateAndGetOffset(sequenceKey, v * sequenceRuleDefinition.getIncrement());

        //内存初始值
        Long initValue = offset - v;
        for (Long i = initValue; i < offset; i += increment) {
            try {
                queue.put(IdUtil.generateId(i, sequenceRuntime));
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

            idFactory.getSequenceService()
                    .getRunningRule()
                    .forEach(this::productIfAbsent);
        }

        log.info(this.getServiceName() + " service end");
    }
}
