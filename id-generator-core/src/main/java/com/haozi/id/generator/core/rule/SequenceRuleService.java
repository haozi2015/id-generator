package com.haozi.id.generator.core.rule;


import com.haozi.id.generator.core.rule.repository.SequenceEnum;
import com.haozi.id.generator.core.rule.repository.SequenceRepository;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import com.haozi.id.generator.core.util.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
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
public class SequenceRuleService {

    private SequenceRepository sequenceRepository;
    private SequenceRuleCache sequenceRuleCache;

    public SequenceRuleService(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
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
        SequenceRule sequenceRule = sequenceRuleCache.get(key);
        Assert.notNull(sequenceRule, "Key does not exist. key=" + key);
        return getNowSequenceRuntimeKey(sequenceRule);
    }

    public String getSequenceRuntimeKey(SequenceRule sequenceRule, SequenceEnum.Runtime runtimeType) {
        RuntimeSequence runtimeSequence = getSequenceRuntime(sequenceRule, runtimeType);
        return runtimeSequence.getSequenceKey();
    }

    /**
     * 获取当前序列key
     *
     * @param sequenceRule
     * @return
     */
    public String getNowSequenceRuntimeKey(SequenceRule sequenceRule) {
        RuntimeSequence runtimeSequence = getSequenceRuntime(sequenceRule, SequenceEnum.Runtime.NOW);
        return runtimeSequence.getSequenceKey();
    }

    /**
     * 获取当前序列key
     *
     * @param sequenceRule
     * @return
     */
    public RuntimeSequence getSequenceRuntime(SequenceRule sequenceRule, SequenceEnum.Runtime runtimeType) {
        return SequenceUtil.createRuntimeSequence(sequenceRule, runtimeType);
    }

    /**
     * 查询全部运行中规则
     * <p>
     *
     * @return
     */
    protected List<SequenceRule> runningAllFromSource() {
        return sequenceRepository.getRuleByStatus(SequenceEnum.Status.RUNNING);
    }

    public Collection<SequenceRule> getRunningRule() {
        return sequenceRuleCache.getAllRule();
    }

    /**
     * 更新序列获取ID范围
     *
     * @param key
     * @param inc
     * @return
     */
    public Long updateAndGetOffset(String key, long inc, long initialValue) {
        return sequenceRepository.incAndGetOffset(key, inc, initialValue);
    }

}
