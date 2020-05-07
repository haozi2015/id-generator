package com.haozi.id.generator.metric;

import com.haozi.id.generator.core.config.IdGeneratorFactoryAutoConfiguration;
import com.haozi.id.generator.metric.config.ClientAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 支持客户端ID生成器
 *
 * @author haozi
 * @date 2020/5/74:40 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({IdGeneratorFactoryAutoConfiguration.class, ClientAutoConfiguration.class})
public @interface EnableClientIdGenerator {
}
