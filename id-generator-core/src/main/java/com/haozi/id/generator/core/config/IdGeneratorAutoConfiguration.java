package com.haozi.id.generator.core.config;

import com.haozi.id.generator.core.rule.SequenceRuleService;
import com.haozi.id.generator.core.rule.repository.SequenceRepository;
import com.haozi.id.generator.core.rule.repository.mysql.MySQLSequenceRepository;
import com.haozi.id.generator.core.rule.repository.mysql.SequenceMapper;
import com.haozi.id.generator.core.rule.repository.mysql.SequenceRuleMapper;
import com.haozi.id.generator.core.rule.repository.redis.RedisSequenceRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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

    @ConditionalOnProperty("spring.datasource.url")
    @Import({DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
    @MapperScan("com.haozi.id.generator.core.rule.repository.mysql")
    static class MySQLRepository {
        @Bean
        public SequenceRepository sequenceRepository(SequenceRuleMapper sequenceRuleDefinitionMapper, SequenceMapper sequenceMapper) {
            return new MySQLSequenceRepository(sequenceRuleDefinitionMapper, sequenceMapper);
        }
    }

    @ConditionalOnExpression("!'${spring.redis.host:}'.equals('') || !'${spring.redis.cluster.nodes:}'.equals('') ")
    static class RedisRepository {
        @Bean
        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<Object, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);
            GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
            template.setValueSerializer(genericToStringSerializer);
            template.setKeySerializer(new StringRedisSerializer());
            template.afterPropertiesSet();
            return template;
        }

        @Bean
        public SequenceRepository sequenceRepository(RedisTemplate redisTemplate) {
            return new RedisSequenceRepository(redisTemplate);
        }
    }

}
