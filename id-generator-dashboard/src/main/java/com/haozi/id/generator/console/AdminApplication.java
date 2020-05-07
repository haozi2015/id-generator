package com.haozi.id.generator.console;

import com.haozi.id.generator.metric.EnableServerIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableServerIdGenerator
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        log.info("id-generator-admin SpringBoot Start Success");
    }
}
