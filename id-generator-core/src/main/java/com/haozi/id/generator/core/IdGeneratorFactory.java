package com.haozi.id.generator.core;

import com.haozi.id.generator.core.exception.IdGeneratorException;
import com.haozi.id.generator.core.buffer.BufferPool;
import com.haozi.id.generator.core.buffer.CleanIdBuffer;
import com.haozi.id.generator.core.buffer.ProductIdBuffer;
import com.haozi.id.generator.core.buffer.ResetIdBuffer;
import com.haozi.id.generator.core.sequence.SequenceService;
import com.haozi.id.generator.core.util.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author haozi
 * @date 2020/4/244:21 下午
 */
@Slf4j
public class IdGeneratorFactory {
    private final static Long TIMEOUT = 1L;
    private final SequenceService sequenceService;
    private final Integer limit;
    private final ProductIdBuffer productIdBuffer;
    private final ResetIdBuffer resetIdBuffer;
    private final CleanIdBuffer cleanIdBuffer;

    public IdGeneratorFactory(SequenceService sequenceService, Integer limit) {
        this.sequenceService = sequenceService;
        this.limit = limit;
        this.productIdBuffer = new ProductIdBuffer(this);
        this.resetIdBuffer = new ResetIdBuffer(this);
        this.cleanIdBuffer = new CleanIdBuffer(this);
    }

    public void start() {
        this.productIdBuffer.start();
        this.resetIdBuffer.start();
        this.cleanIdBuffer.start();
    }

    public SequenceService getSequenceService() {
        return sequenceService;
    }

    public ProductIdBuffer getIdProducer() {
        return productIdBuffer;
    }

    public <T> List<T> getId(String key, int num) {
        Assert.notNull(key, "key not null");
        Assert.isTrue(num > 0, "num > 0");
        Assert.isTrue(num <= limit, "num <= " + limit);
        String nowSequenceKey = sequenceService.getNowSequenceRuntimeKey(key);
        BlockingQueue<T> queue = BufferPool.getBuffer(nowSequenceKey);
        Assert.notNull(queue, "Key does not exist. key=" + key);
        List<T> list = new ArrayList<T>(num);
        //一次获取，避免循环poll加锁、释放锁，数量不足，再补充
        int drain = 0;
        try {
            for (int i = 0; i < num - drain; i++) {
                long startTime = System.currentTimeMillis();
                T id = queue.poll(TIMEOUT, TimeUnit.SECONDS);
                if (id == null) {
                    log.error("id is null, nowSequenceKey={}, size={}, cost={}", nowSequenceKey, queue.size(), (System.currentTimeMillis() - startTime));
                    throw new IdGeneratorException("Too many operations, please try again later");
                }
                list.add(id);
            }
            return list;
        } catch (InterruptedException e) {
            log.error("get key fail. key={}, nowSequenceKey={}, num={}", key, nowSequenceKey, num, e);
            Thread.currentThread().interrupt();
            throw new IdGeneratorException("Too many operations, please try again later");
        }
    }

    public List<Long> getGuid(int num) {
        Assert.isTrue(num > 0, "num > 0");
        Assert.isTrue(num <= limit, "num <= " + limit);

        List<Long> list = new ArrayList<Long>(num);
        for (int i = 0; i < num; i++) {
            list.add(Snowflake.nextId());
        }
        return list;
    }
}
