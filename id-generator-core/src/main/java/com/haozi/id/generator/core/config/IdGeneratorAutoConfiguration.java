package com.haozi.id.generator.core.config;

import com.haozi.id.generator.core.id.IdFactory;
import com.haozi.id.generator.core.sequence.SequenceService;
import com.haozi.id.generator.core.sequence.dao.SequenceMapper;
import com.haozi.id.generator.core.sequence.dao.SequenceRuleDefinitionMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haozi
 * @date 2020/4/246:26 下午
 */
@Configuration
@MapperScan("com.haozi.id.generator.core.sequence.dao")
public class IdGeneratorAutoConfiguration {

    @Value("${generate.id.limit:100}")
    private Integer limit;

    @Bean(initMethod = "start")
    public IdFactory idFactory(SequenceService sequenceService) {
        return new IdFactory(sequenceService, limit);
    }

    @Bean
    public SequenceService sequenceService(SequenceRuleDefinitionMapper sequenceRuleDefinitionMapper, SequenceMapper sequenceMapper) {
        return new SequenceService(sequenceRuleDefinitionMapper, sequenceMapper);
    }
}
