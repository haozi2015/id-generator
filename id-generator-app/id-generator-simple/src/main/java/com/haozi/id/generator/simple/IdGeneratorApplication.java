package com.haozi.id.generator.simple;

import com.haozi.id.generator.core.EnableIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableIdGenerator
@SpringBootApplication
public class IdGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdGeneratorApplication.class, args);
        log.info("id-generator SpringBoot Start Success");
    }
}
