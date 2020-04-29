package com.haozi.id.generator.core.config;

import com.haozi.id.generator.core.IdGeneratorFactory;
import com.haozi.id.generator.core.rule.SequenceRuleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author haozi
 * @date 2020/4/246:26 下午
 */
public class IdGeneratorFactoryAutoConfiguration {

    @Value("${generate.id.one-request-id-max:100}")
    private Integer limit;

    @Bean(initMethod = "start")
    public IdGeneratorFactory idFactory(SequenceRuleService sequenceRuleService) {
        return new IdGeneratorFactory(sequenceRuleService, limit);
    }

}
