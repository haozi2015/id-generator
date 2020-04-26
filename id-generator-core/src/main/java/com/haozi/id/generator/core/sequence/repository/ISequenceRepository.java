package com.haozi.id.generator.core.sequence.repository;

import java.util.List;

/**
 * 序列存储接口
 *
 * @author haozi
 * @date 2020/4/262:28 下午
 */
public interface ISequenceRepository {
    /**
     * 增
     *
     * @param sequenceRule
     * @return
     */
    Integer insertRule(SequenceRuleDefinition sequenceRule);

    /**
     * 改
     *
     * @param sequenceRule
     * @return
     */
    Integer updateRuleByKey(SequenceRuleDefinition sequenceRule);

    /**
     * 通过Key查询
     *
     * @param key
     * @return
     */
    SequenceRuleDefinition getRuleByKey(String key);

    /**
     * 通过状态查询
     *
     * @param status
     * @return
     */
    List<SequenceRuleDefinition> getRuleByStatus(SequenceEnum.Status status);

    /**
     * 分页查询
     *
     * @param status
     * @return
     */
    List<SequenceRuleDefinition> getRuleByPage(String key, SequenceEnum.Status status, int page, int pageSize);

    /**
     * 规则数量
     *
     * @param status
     * @return
     */
    Long getRuleCount(String key, SequenceEnum.Status status);

    /**
     * 偏移量原子增，并获取增加后的值
     *
     * @param sequenceKey
     * @param inc
     * @return
     */
    Long incAndGetOffset(String sequenceKey, long inc);

    /**
     * 更新偏移量
     *
     * @param sequenceKey
     * @param offset
     * @return
     */
    Integer updateOffset(String sequenceKey, long offset);

    /**
     * 查询序列值
     *
     * @param sequenceKey
     * @return
     */
    Long getSequenceOffset(String sequenceKey);
}
