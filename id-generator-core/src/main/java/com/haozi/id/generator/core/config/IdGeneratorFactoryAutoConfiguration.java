package com.haozi.id.generator.core.config;

import com.haozi.id.generator.core.IdGeneratorFactory;
import com.haozi.id.generator.core.sequence.SequenceService;
import com.haozi.id.generator.core.sequence.repository.ISequenceRepository;
import com.haozi.id.generator.core.sequence.repository.mysql.MySQLSequenceRepository;
import com.haozi.id.generator.core.sequence.repository.mysql.SequenceMapper;
import com.haozi.id.generator.core.sequence.repository.mysql.SequenceRuleDefinitionMapper;
import com.haozi.id.generator.core.sequence.repository.redis.RedisSequenceRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author haozi
 * @date 2020/4/246:26 下午
 */
public class IdGeneratorFactoryAutoConfiguration {

    @Value("${generate.id.one-request-id-max:100}")
    private Integer limit;

    @Bean(initMethod = "start")
    public IdGeneratorFactory idFactory(SequenceService sequenceService) {
        return new IdGeneratorFactory(sequenceService, limit);
    }

}
