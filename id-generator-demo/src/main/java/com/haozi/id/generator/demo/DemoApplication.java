package com.haozi.id.generator.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
/**
 * spring cloud 使用, dubbo 可以去掉
 */
//@EnableFeignClients
//@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        log.info("id-generator-demo SpringBoot Start Success");
    }
}
