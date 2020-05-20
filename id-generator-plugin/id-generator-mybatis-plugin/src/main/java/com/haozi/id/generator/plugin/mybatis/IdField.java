package com.haozi.id.generator.plugin.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ID生成器兼容Mybatis注解
 *
 * @author haozi
 * @date 2020/5/1911:15 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IdField {
    /**
     * ID生成器中定义的唯一KEY
     * <p>
     * 需要预先定义
     *
     * @return
     */
    String value();
}
