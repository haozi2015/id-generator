package com.haozi.id.generator.plugin.mybatis;

import java.util.Collection;

/**
 * Mybatis定义获得ID方法
 * <p>
 * 扩展中实现dubbo方式或spring cloud方式
 *
 * @author haozi
 * @date 2020/5/2011:27 上午
 */
@FunctionalInterface
public interface MyBatisIdGenerator<T> {
    Collection<T> generateId(String key, int num);
}
