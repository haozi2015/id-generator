package com.haozi.id.generator.plugin.mybatis.config;

import com.haozi.id.generator.dubbo.api.IdGenerator;
import com.haozi.id.generator.plugin.mybatis.MyBatisIdGenerator;
import com.haozi.id.generator.plugin.mybatis.intercept.IdGeneratorInterceptor;
import feign.Feign;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

/**
 * ID生成器集成Mybatis插件
 * <p>
 * 兼容dubbo、spring boot方式
 *
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
            Object a= idGenerator.generateId("test1");
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
    @ConditionalOnClass(Feign.class)
    @ConditionalOnMissingBean(MyBatisIdGenerator.class)
    static class SpringCloudAutoConfiguration {

        interface IdGeneratorFeign {

            @GetMapping(value = "/generate/id")
            Map generateId(@RequestParam("key") String key, @RequestParam("num") int num);

        }

        @Bean
        public MyBatisIdGenerator idGenerator(@Autowired ApplicationContext appContext) {
            FeignClientBuilder feignClientBuilder = new FeignClientBuilder(appContext);
            IdGeneratorFeign idGeneratorFeign = feignClientBuilder.forType(IdGeneratorFeign.class, "id-generator").build();
            return (key, num) -> (Collection) idGeneratorFeign.generateId(key, num).get("data");
        }

        @Bean
        public IdGeneratorInterceptor paramInterceptor(MyBatisIdGenerator myBatisIdGenerator) {
            return new IdGeneratorInterceptor(myBatisIdGenerator);
        }
    }


}
