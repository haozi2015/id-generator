package com.haozi.id.generator.spring.cloud;

import com.haozi.id.generator.metric.EnableClientIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@Slf4j
@EnableClientIdGenerator
@EnableEurekaClient
@SpringBootApplication
public class IdGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdGeneratorApplication.class, args);
        log.info("id-generator SpringBoot Start Success");
    }
}
