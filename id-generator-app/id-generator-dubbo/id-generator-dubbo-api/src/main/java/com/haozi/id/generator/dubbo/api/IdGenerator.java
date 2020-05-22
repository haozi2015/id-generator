package com.haozi.id.generator.dubbo.api;


import java.util.List;

/**
 * ID生成器
 *
 * @author haozi
 * @date 2019-10-2914:08
 */
public interface IdGenerator<T> {
    /**
     * 全局唯一ID
     *
     * @return
     */
    Long generateGuid();

    /**
     * 全局唯一ID
     *
     * @param num ID个数
     * @return
     */
    List<Long> generateGuid(int num);

    /**
     * 隔离自增ID
     *
     * @param key 隔离唯一标识
     * @return
     */
    T generateId(String key);

    /**
     * 隔离自增ID
     *
     * @param key 隔离唯一标识
     * @param num ID个数
     * @return
     */
    List<T> generateId(String key, int num);
}
