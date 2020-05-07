//package com.haozi.id.generator.metric.client;
//
//import org.apache.catalina.core.ApplicationContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.AnnotatedElementUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
///**
// * @author haozi
// * @date 2020/5/75:39 下午
// */
//public class ControllerRegister implements ApplicationRunner {
//    @Autowired
//    ApplicationContext applicationContext;
//    @Autowired
//    private RequestMappingHandlerMapping requestMappingHandlerMapping;
//    @Autowired
//    private MetricClientController metricClientController;
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        Method method=MetricClientController.class.getDeclaredMethod("metric");
//        Field field= RequestMappingHandlerMapping.class.getDeclaredField("config");
//        field.setAccessible(true);
//        //解析注解
//        RequestMappingInfo.BuilderConfiguration configuration=(RequestMappingInfo.BuilderConfiguration)field.get(requestMappingHandlerMapping);
//
//        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
//
//        RequestMappingInfo.Builder builder = RequestMappingInfo
//                .paths(requestMapping.path())
//                .methods(requestMapping.method())
//                .params(requestMapping.params())
//                .headers(requestMapping.headers())
//                .consumes(requestMapping.consumes())
//                .produces(requestMapping.produces())
//                .mappingName(requestMapping.name());
//        builder.options(configuration);
//        //初始化示例
//        MetricClientController metricClientController=new MetricClientController();
//        //对bean进行注入，使得可以使用@Autowired
//        applicationContext..get.getAutowireCapableBeanFactory().autowireBean(metricClientController);
//        requestMappingHandlerMapping.registerMapping(builder.build(),metricClientController,method);
//    }
//}
