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
     * 生成全局唯一ID
     *
     * @return
     */
    Long generateGuid();

    /**
     * 生成多个全局唯一ID
     *
     * @return
     */
    List<Long> generateGuid(int num);

    /**
     * 生成单个ID
     *
     * @param key
     * @return
     */
    T generateId(String key);

    /**
     * 生成多个ID
     *
     * @param key
     * @param num
     * @return
     */
    List<T> generateId(String key, int num);
}
