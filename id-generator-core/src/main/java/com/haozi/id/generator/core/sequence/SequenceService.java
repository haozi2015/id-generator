package com.haozi.id.generator.core.sequence;


import com.haozi.id.generator.core.sequence.repository.ISequenceRepository;
import com.haozi.id.generator.core.sequence.repository.SequenceEnum;
import com.haozi.id.generator.core.sequence.repository.SequenceRuleDefinition;
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
public class SequenceService {

    private ISequenceRepository sequenceRepository;
    private SequenceRuleCache sequenceRuleCache;

    public SequenceService(ISequenceRepository sequenceRepository) {
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

    /**
     * 查询全部运行中规则
     * <p>
     * TODO 规则多，需分页
     *
     * @return
     */
    protected List<SequenceRuleDefinition> runningAllFromSource() {
        return sequenceRepository.getRuleByStatus(SequenceEnum.Status.RUNNING);
    }

    public Collection<SequenceRuleDefinition> getRunningRule() {
        return sequenceRuleCache.getAllRule();
    }

    public Long updateAndGetOffset(String key, long inc) {
        return sequenceRepository.incAndGetOffset(key, inc);
    }

}
