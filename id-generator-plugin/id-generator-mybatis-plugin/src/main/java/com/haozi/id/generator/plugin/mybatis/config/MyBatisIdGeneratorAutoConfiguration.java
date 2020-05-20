package com.haozi.id.generator.plugin.mybatis.config;

import com.haozi.id.generator.dubbo.api.IdGenerator;
import com.haozi.id.generator.plugin.mybatis.MyBatisIdGenerator;
import com.haozi.id.generator.plugin.mybatis.intercept.IdGeneratorInterceptor;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

/**
 * @author haozi
 * @date 2020/5/2011:44 上午
 */
@Configuration
public class MyBatisIdGeneratorAutoConfiguration {
    /**
     * dubbo方式
     */
    @ConditionalOnClass(IdGenerator.class)
    @ConditionalOnMissingBean(MyBatisIdGenerator.class)
    static class DubboAutoConfiguration {
        @Reference
        private IdGenerator idGenerator;

        @Bean
        public MyBatisIdGenerator idGenerator() {
            return (key, num) -> idGenerator.generateId(key, num);
        }

        @Bean
        public IdGeneratorInterceptor paramInterceptor(MyBatisIdGenerator myBatisIdGenerator) {
            return new IdGeneratorInterceptor(myBatisIdGenerator);
        }
    }

    /**
     * spring cloud方式
     */
    @ConditionalOnBean(FeignContext.class)
    @ConditionalOnMissingBean(MyBatisIdGenerator.class)
    static class SpringCloudAutoConfiguration {

        @FeignClient("id-generator")
        interface IdGeneratorFeign {

            @GetMapping(value = "/generate/id")
            Collection generateId(String key, int num);

        }

        @Bean
        public MyBatisIdGenerator idGenerator(IdGeneratorFeign idGeneratorFeign) {
            return (key, num) -> idGeneratorFeign.generateId(key, num);
        }

        @Bean
        public IdGeneratorInterceptor paramInterceptor(MyBatisIdGenerator myBatisIdGenerator) {
            return new IdGeneratorInterceptor(myBatisIdGenerator);
        }
    }


}
