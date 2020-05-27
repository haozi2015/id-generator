package com.haozi.id.generator.dashboard.service;


import com.haozi.id.generator.core.exception.IdGeneratorException;
import com.haozi.id.generator.core.rule.RuntimeSequence;
import com.haozi.id.generator.core.rule.SequenceRuleService;
import com.haozi.id.generator.core.rule.repository.SequenceEnum;
import com.haozi.id.generator.core.rule.repository.SequenceRepository;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import com.haozi.id.generator.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 顺序规则与序值dao操作
 *
 * @author haozi
 * @date 2019-11-0811:58
 */
@Slf4j
@Service
public class SequenceAdminService {
    @Resource
    private SequenceRepository sequenceRepository;
    @Resource
    private SequenceRuleService sequenceRuleService;

    /**
     * 查询规则
     *
     * @param key
     * @return
     */
    public SequenceRule getRule(String key) {
        return sequenceRepository.getRuleByKey(key);
    }

    /**
     * 查询规则分页列表
     *
     * @param key
     * @param status
     * @param page
     * @param pageSize
     * @return
     */
    public List<SequenceRule> getRuleByPage(String key, SequenceEnum.Status status, int page, int pageSize) {
        if (page <= 0) {
            page = 1;
        }
        return sequenceRepository.getRuleByPage(key, status, page, pageSize);
    }

    /**
     * 查询规则总数量
     *
     * @param key
     * @param status
     * @return
     */
    public Long getRuleCount(String key, SequenceEnum.Status status) {
        return sequenceRepository.getRuleCount(key, status);
    }

    /**
     * 查询最近偏移量
     * <p>
     * 因数据缓存在内存，无法提供实时偏移量
     *
     * @param key
     * @return
     */
    public Long getOffset(String key) {
        SequenceRule sequenceRule = sequenceRepository.getRuleByKey(key);
        Assert.notNull(sequenceRule, "SequenceRuleDefinition non-existent");
        String sequenceKey = sequenceRuleService.getNowSequenceRuntimeKey(sequenceRule);
        return sequenceRepository.getSequenceOffset(sequenceKey);
    }

    /**
     * 新增规则
     * <p>
     * 在下一次加载规则时，生效
     *
     * @param sequenceRule
     * @return
     */
    public int insert(SequenceRule sequenceRule) {
        sequenceRule.setStatus(SequenceEnum.Status.STOP.getValue());
        sequenceRule.setLastUpdateTime(new Date());
        return sequenceRepository.insertRule(sequenceRule);
    }

    /**
     * 修改规则
     * <p>
     * 在下一次加载规则时，生效
     *
     * @param sequenceRule
     * @return
     */
    public int update(SequenceRule sequenceRule) {
        sequenceRule.setLastUpdateTime(new Date());
        return sequenceRepository.updateRuleByKey(sequenceRule);
    }

    /**
     * 设置初始值
     *
     * @param key
     * @param initialValue
     * @return
     */
    public Integer initialValue(String key, long initialValue) {
        SequenceRule sequenceRule = sequenceRepository.getRuleByKey(key);
        Assert.notNull(sequenceRule, "SequenceRuleDefinition non-existent");
        if (SequenceEnum.Status.RUNNING.getValue().equals(sequenceRule.getStatus())) {
            throw new IdGeneratorException("Pause first，try again.");
        }
        String sequenceKey = sequenceRuleService.getNowSequenceRuntimeKey(sequenceRule);
        return sequenceRepository.updateOffset(sequenceKey, initialValue);
    }

    /**
     * 启动
     *
     * @param key
     * @return
     */
    public Integer run(String key) {
        SequenceRule sequenceRule = sequenceRepository.getRuleByKey(key);
        Assert.notNull(sequenceRule, "SequenceRuleDefinition non-existent");

        if (SequenceEnum.Status.RUNNING.getValue().equals(sequenceRule.getStatus())) {
            throw new IdGeneratorException("Already [RUNNING].");
        }

        sequenceRule.setStatus(SequenceEnum.Status.RUNNING.getValue());
        return sequenceRepository.updateRuleByKey(sequenceRule);
    }

    /**
     * 停止
     *
     * @param key
     * @return
     */
    public Integer stop(String key) {
        SequenceRule sequenceRule = sequenceRepository.getRuleByKey(key);
        Assert.notNull(sequenceRule, "SequenceRuleDefinition non-existent");

        if (SequenceEnum.Status.STOP.getValue().equals(sequenceRule.getStatus())) {
            throw new IdGeneratorException("Already [STOP].");
        }
        sequenceRule.setStatus(SequenceEnum.Status.STOP.getValue());
        return sequenceRepository.updateRuleByKey(sequenceRule);
    }

    /**
     * 模拟生成ID
     *
     * @param key
     * @return
     */
    public Object tryIt(String key) {
        SequenceRule sequenceRule = sequenceRepository.getRuleByKey(key);
        RuntimeSequence sequenceRuntime = sequenceRuleService.getSequenceRuntime(sequenceRule, SequenceEnum.Runtime.NOW);
        return IdUtil.generateId(sequenceRule.getInitialValue(), sequenceRuntime);
    }
}
