package com.haozi.id.generator.core.sequence;


import com.haozi.id.generator.core.sequence.dao.SequenceMapper;
import com.haozi.id.generator.core.sequence.dao.SequenceRuleDefinition;
import com.haozi.id.generator.core.sequence.dao.SequenceRuleDefinitionMapper;
import com.haozi.id.generator.core.util.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

/**
 * 顺序规则与序值dao操作
 *
 * @author haozi
 * @date 2019-11-0811:58
 */
@Slf4j
public class SequenceService {

    private SequenceRuleDefinitionMapper sequenceRuleDefinitionMapper;
    private SequenceMapper sequenceMapper;
    private SequenceRuleCache sequenceRuleCache;

    public SequenceService(SequenceRuleDefinitionMapper sequenceRuleDefinitionMapper, SequenceMapper sequenceMapper) {
        this.sequenceRuleDefinitionMapper = sequenceRuleDefinitionMapper;
        this.sequenceMapper = sequenceMapper;
        this.sequenceRuleCache = new SequenceRuleCache(this);
        sequenceRuleCache.start();
    }

    /**
     * 获取当前序列key
     *
     * @param key
     * @return
     */
    public String getNowSequenceRuntimeKey(String key) {
        SequenceRuleDefinition sequenceRuleDefinition = sequenceRuleCache.get(key);
        Assert.notNull(sequenceRuleDefinition, "Key does not exist. key=" + key);
        return getNowSequenceRuntimeKey(sequenceRuleDefinition);
    }

    public String getSequenceRuntimeKey(SequenceRuleDefinition sequenceRuleDefinition, SequenceEnum.Runtime runtimeType) {
        SequenceRuntime runtimeSequence = getSequenceRuntime(sequenceRuleDefinition, runtimeType);
        return runtimeSequence.getSequenceKey();
    }

    /**
     * 获取当前序列key
     *
     * @param sequenceRuleDefinition
     * @return
     */
    public String getNowSequenceRuntimeKey(SequenceRuleDefinition sequenceRuleDefinition) {
        SequenceRuntime runtimeSequence = getSequenceRuntime(sequenceRuleDefinition, SequenceEnum.Runtime.NOW);
        return runtimeSequence.getSequenceKey();
    }

    /**
     * 获取当前序列key
     *
     * @param sequenceRuleDefinition
     * @return
     */
    public SequenceRuntime getSequenceRuntime(SequenceRuleDefinition sequenceRuleDefinition, SequenceEnum.Runtime runtimeType) {
        return SequenceUtil.createRuntimeSequence(sequenceRuleDefinition, runtimeType);
    }

    public Collection<SequenceRuleDefinition> getRunningRule() {
        return sequenceRuleCache.getAllRule();
    }

    /**
     * 查询全部运行中规则
     * <p>
     * TODO 规则多，需分页
     *
     * @return
     */
    public List<SequenceRuleDefinition> runningAllFromSource() {
        return sequenceRuleDefinitionMapper.runningAll();
    }

    public SequenceRuleDefinition getSequenceRuleFromSource(String key) {
        return sequenceRuleDefinitionMapper.getByKey(key);
    }

    /**
     * 查询当前KEY的序列值
     *
     * @param key
     * @return
     */
    public Long getConcurrentOffset(String key) {
        return sequenceMapper.selectOffsetByKey(key);
    }

    /**
     * 更新\获取增量值
     * <p>
     * 注意保留事务，保证更新和读取的原子性
     * <p>
     * 本地（非wifi）开发环境测试方法内耗时约10ms（未计算事务提交），方法外耗时约20ms（计算事务提交）
     *
     * @param key
     * @param inc
     * @return
     */
    @Transactional
    public Long updateAndGetOffset(String key, long inc) {
        long startTime = System.currentTimeMillis();
        sequenceMapper.updateOffsetByKey(key, inc);
        Long offset = sequenceMapper.selectOffsetByKey(key);
        log.info("updateAndGetOffset key:{}, inc:{}, 耗时：{}ms", key, inc, (System.currentTimeMillis() - startTime));
        return offset;
    }


    /**
     * 新增规则
     * <p>
     * 在下一次加载规则时，生效
     *
     * @param sequenceRule
     * @return
     */
    public int insert(SequenceRuleDefinition sequenceRule) {
        return sequenceRuleDefinitionMapper.insert(sequenceRule);
    }

    /**
     * 修改规则
     * <p>
     * 在下一次加载规则时，生效
     *
     * @param sequenceRule
     * @return
     */
    public int update(SequenceRuleDefinition sequenceRule) {
        return sequenceRuleDefinitionMapper.updateByKey(sequenceRule);
    }

}
