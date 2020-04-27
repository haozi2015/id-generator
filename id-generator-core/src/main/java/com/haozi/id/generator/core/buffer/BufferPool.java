package com.haozi.id.generator.core.buffer;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ID缓冲池
 * <p>
 * 存储预先生成的ID，应用重启缓冲区ID失效
 *
 * @author haozi
 * @date 2019-11-0811:43
 */
public class BufferPool {

    private static Map<String, BlockingQueue> idBuffer = new ConcurrentHashMap<>();

    public static <T> BlockingQueue<T> getBuffer(String sequenceKey) {
        return idBuffer.get(sequenceKey);
    }

    public static <T> void putIfAbsent(String sequenceKey, BlockingQueue<T> queue) {
        idBuffer.putIfAbsent(sequenceKey, queue);
    }

    public static <T> void put(String sequenceKey, BlockingQueue<T> queue) {
        idBuffer.put(sequenceKey, queue);
    }

    public static <T> void remove(String sequenceKey) {
        idBuffer.remove(sequenceKey);
    }
}
