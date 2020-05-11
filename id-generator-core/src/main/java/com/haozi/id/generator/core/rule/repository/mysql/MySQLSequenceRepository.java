package com.haozi.id.generator.core.rule.repository.mysql;

import com.haozi.id.generator.core.rule.repository.SequenceEnum;
import com.haozi.id.generator.core.rule.repository.SequenceRepository;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 持久化-MySQL方式
 *
 * @author haozi
 * @date 2020/4/262:34 下午
 */
@Slf4j
public class MySQLSequenceRepository implements SequenceRepository {

    private SequenceRuleMapper sequenceRuleDefinitionMapper;
    private SequenceMapper sequenceMapper;

    public MySQLSequenceRepository(SequenceRuleMapper sequenceRuleDefinitionMapper, SequenceMapper sequenceMapper) {
        this.sequenceRuleDefinitionMapper = sequenceRuleDefinitionMapper;
        this.sequenceMapper = sequenceMapper;
    }

    @Override
    public Integer insertRule(SequenceRule sequenceRule) {
        return sequenceRuleDefinitionMapper.insert(sequenceRule);
    }

    @Override
    public Integer updateRuleByKey(SequenceRule sequenceRule) {
        Assert.notNull(sequenceRule.getKey(), "key is null");
        return sequenceRuleDefinitionMapper.updateByKey(sequenceRule);
    }

    @Override
    public SequenceRule getRuleByKey(String key) {
        return sequenceRuleDefinitionMapper.getByKey(key);
    }

    @Override
    public List<SequenceRule> getRuleByStatus(SequenceEnum.Status status) {
        return sequenceRuleDefinitionMapper.getByStatus(status.getValue());
    }

    @Override
    public List<SequenceRule> getRuleByPage(String key, SequenceEnum.Status status, int page, int pageSize) {
        int row = pageSize * (page - 1);
        return sequenceRuleDefinitionMapper.selectByPage(key, status == null ? null : status.getValue(), row, pageSize);
    }

    @Override
    public Long getRuleCount(String key, SequenceEnum.Status status) {
        return sequenceRuleDefinitionMapper.selectByCount(key, status == null ? null : status.getValue());
    }


    /**
     * 更新\获取增量值
     * <p>
     * 注意保留事务，保证更新和读取的原子性
     * <p>
     * 本地（非wifi）开发环境测试方法内耗时约10ms（未计算事务提交），方法外耗时约20ms（计算事务提交）
     *
     * @param sequenceKey
     * @param inc
     * @return
     */
    @Override
    @Transactional
    public Long incAndGetOffset(String sequenceKey, long inc) {
        long startTime = System.currentTimeMillis();
        sequenceMapper.incOffsetByKey(sequenceKey, inc);
        Long offset = sequenceMapper.selectOffsetByKey(sequenceKey);
        log.info("incAndGetOffset sequenceKey:{}, inc:{}, cost：{}ms", sequenceKey, inc, (System.currentTimeMillis() - startTime));
        return offset;
    }

    @Override
    public Integer updateOffset(String sequenceKey, long offset) {
        return sequenceMapper.updateOffset(sequenceKey, offset);
    }

    @Override
    public Long getSequenceOffset(String sequenceKey) {
        return sequenceMapper.selectOffsetByKey(sequenceKey);
    }

}
