package com.haozi.id.generator.core;

import com.haozi.id.generator.core.config.IdGeneratorFactoryAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用ID生成器
 *
 * @author haozi
 * @date 2020/4/276:42 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({IdGeneratorFactoryAutoConfiguration.class})
public @interface EnableIdGenerator {
}
