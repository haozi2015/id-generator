package com.haozi.id.generator.metric;

import com.haozi.id.generator.metric.config.ServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 支持服务端
 *
 * @author haozi
 * @date 2020/5/74:40 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ServerAutoConfiguration.class})
public @interface EnableServerIdGenerator {
}
