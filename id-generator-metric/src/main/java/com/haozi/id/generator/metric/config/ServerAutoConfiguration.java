package com.haozi.id.generator.metric.config;

import com.haozi.id.generator.metric.common.MetricEntity;
import com.haozi.id.generator.metric.server.MetricFetcher;
import com.haozi.id.generator.metric.server.controller.RegistryController;
import com.haozi.id.generator.metric.server.discovery.ClientDiscovery;
import com.haozi.id.generator.metric.server.discovery.SimpleClientDiscovery;
import com.haozi.id.generator.metric.server.repository.InMemoryMetricsRepository;
import com.haozi.id.generator.metric.server.repository.MetricsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author haozi
 * @date 2020/5/74:42 下午
 */
public class ServerAutoConfiguration {

    @Bean
    public ClientDiscovery clientDiscovery() {
        return new SimpleClientDiscovery();
    }

    @Bean
    public RegistryController registryController(ClientDiscovery clientDiscovery, RequestMappingHandlerMapping requestMappingHandlerMapping) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        Method method = RegistryController.class.getDeclaredMethod("receiveHeartBeat");
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
        RegistryController registryController = new RegistryController(clientDiscovery);
        requestMappingHandlerMapping.registerMapping(builder.build(), registryController, method);
        return registryController;
    }

    @Bean
    public MetricsRepository metricsRepository() {
        return new InMemoryMetricsRepository();
    }

    @Bean
    public MetricFetcher metricFetcher(MetricsRepository<MetricEntity> metricStore, ClientDiscovery machineDiscovery, RestTemplate restTemplate) {
        return new MetricFetcher(metricStore, machineDiscovery, restTemplate);
    }
}
