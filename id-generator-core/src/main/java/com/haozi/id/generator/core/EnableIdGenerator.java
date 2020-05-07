package com.haozi.id.generator.core;

import com.haozi.id.generator.core.config.IdGeneratorFactoryAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ID Generator enable
 * <p>
 * 默认不主动生成ID，缓存至内存。
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
