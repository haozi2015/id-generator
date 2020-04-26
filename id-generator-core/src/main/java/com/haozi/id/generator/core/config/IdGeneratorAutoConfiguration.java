package com.haozi.id.generator.core.config;

import com.haozi.id.generator.core.id.IdFactory;
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
@Configuration
public class IdGeneratorAutoConfiguration {

    @Value("${generate.id.limit:100}")
    private Integer limit;

    @Bean(initMethod = "start")
    @ConditionalOnProperty(name = "id.generator.id-product", havingValue = "true")
    public IdFactory idFactory(SequenceService sequenceService) {
        return new IdFactory(sequenceService, limit);
    }

    @Bean
    public SequenceService sequenceService(ISequenceRepository sequenceRepository) {
        return new SequenceService(sequenceRepository);
    }

    @ConditionalOnProperty(name = "id.generator.repository", havingValue = "mysql")
    @MapperScan("com.haozi.id.generator.core.sequence.repository.mysql")
    static class MySQLRepository {
        @Bean
        public ISequenceRepository sequenceRepository(SequenceRuleDefinitionMapper sequenceRuleDefinitionMapper, SequenceMapper sequenceMapper) {
            return new MySQLSequenceRepository(sequenceRuleDefinitionMapper, sequenceMapper);
        }
    }

    @ConditionalOnProperty(name = "id.generator.repository", havingValue = "redis")
    static class RedisRepository {
        @Bean
        public ISequenceRepository sequenceRepository(RedisTemplate redisTemplate) {
            return new RedisSequenceRepository(redisTemplate);
        }
    }
}
