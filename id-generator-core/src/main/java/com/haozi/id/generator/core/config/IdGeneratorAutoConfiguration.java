package com.haozi.id.generator.core.config;

import com.haozi.id.generator.core.rule.SequenceRuleService;
import com.haozi.id.generator.core.rule.repository.SequenceRepository;
import com.haozi.id.generator.core.rule.repository.mysql.MySQLSequenceRepository;
import com.haozi.id.generator.core.rule.repository.mysql.SequenceMapper;
import com.haozi.id.generator.core.rule.repository.mysql.SequenceRuleMapper;
import com.haozi.id.generator.core.rule.repository.redis.RedisSequenceRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author haozi
 * @date 2020/4/246:26 下午
 */
@Configuration
public class IdGeneratorAutoConfiguration {

    @Bean
    public SequenceRuleService sequenceService(SequenceRepository sequenceRepository) {
        return new SequenceRuleService(sequenceRepository);
    }

    @ConditionalOnProperty(name = "id.generator.repository", havingValue = "mysql")
    @MapperScan("com.haozi.id.generator.core.rule.repository.mysql")
    static class MySQLRepository {
        @Bean
        public SequenceRepository sequenceRepository(SequenceRuleMapper sequenceRuleDefinitionMapper, SequenceMapper sequenceMapper) {
            return new MySQLSequenceRepository(sequenceRuleDefinitionMapper, sequenceMapper);
        }
    }

    @ConditionalOnProperty(name = "id.generator.repository", havingValue = "redis")
    static class RedisRepository {
        @Bean
        public SequenceRepository sequenceRepository(RedisTemplate redisTemplate) {
            return new RedisSequenceRepository(redisTemplate);
        }
    }
}
