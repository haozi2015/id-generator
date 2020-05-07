package com.haozi.id.generator.metric.config;

import com.haozi.id.generator.metric.client.Heartbeat;
import com.haozi.id.generator.metric.client.MetricClientController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * metric client auto configuration
 *
 * @author haozi
 * @date 2020/5/74:42 下午
 */
public class ClientAutoConfiguration {
    @Value("${server.port:-1}")
    private String port;
    @Value("${id.generator.dashboard}")
    private String dashboard;

    @Bean(initMethod = "init")
    public Heartbeat heartbeat(RestTemplate restTemplate) {
        return new Heartbeat(restTemplate, port, dashboard);
    }

    @Bean
    public MetricClientController metricClientController(RequestMappingHandlerMapping requestMappingHandlerMapping) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        Method method = MetricClientController.class.getDeclaredMethod("metric");
        Field field = RequestMappingHandlerMapping.class.getDeclaredField("config");
        field.setAccessible(true);
        //解析注解
        RequestMappingInfo.BuilderConfiguration configuration = (RequestMappingInfo.BuilderConfiguration) field.get(requestMappingHandlerMapping);

        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(requestMapping.path())
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name());
        builder.options(configuration);
        MetricClientController metricClientController = new MetricClientController();
        requestMappingHandlerMapping.registerMapping(builder.build(), metricClientController, method);
        return metricClientController;
    }
}
