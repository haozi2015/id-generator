package com.haozi.id.generator.core.id;

import com.haozi.id.generator.core.exception.IdGeneratorException;
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
public class IdFactory {
    private final static Long TIMEOUT = 1L;
    private final SequenceService sequenceService;
    private final Integer limit;
    private final IdProducer idProducer;
    private final IdReset idReset;
    private final IdDestroy idDestroy;

    public IdFactory(SequenceService sequenceService, Integer limit) {
        this.sequenceService = sequenceService;
        this.limit = limit;
        this.idProducer = new IdProducer(this);
        this.idReset = new IdReset(this);
        this.idDestroy = new IdDestroy(this);
    }

    public void start() {
        this.idProducer.start();
        this.idReset.start();
        this.idDestroy.start();
    }

    protected SequenceService getSequenceService() {
        return sequenceService;
    }

    protected IdProducer getIdProducer() {
        return idProducer;
    }

    public <T> List<T> getId(String key, int num) {
        Assert.notNull(key, "key not null");
        Assert.isTrue(num > 0, "num > 0");
        Assert.isTrue(num <= limit, "num <= " + limit);
        String nowSequenceKey = sequenceService.getNowSequenceRuntimeKey(key);
        BlockingQueue<T> queue = IdBuffer.getBuffer(nowSequenceKey);
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
