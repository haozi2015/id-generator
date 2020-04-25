package com.haozi.id.generator.core.sequence.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface SequenceRuleDefinitionMapper {

    /**
     * 更新
     *
     * @param record
     * @return
     */
    @InsertProvider(type = SequenceRuleDefinitionSQL.class, method = "updateByKey")
    int updateByKey(SequenceRuleDefinition record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    @InsertProvider(type = SequenceRuleDefinitionSQL.class, method = "insert")
    int insert(SequenceRuleDefinition record);

    /**
     * 查询指定规则
     *
     * @param key
     * @return
     */
    @Select("select * from t_sequence_rule where `key`=#{key}")
    @ResultMap("sequenceRuleDefinitionResults")
    SequenceRuleDefinition getByKey(@Param("key") String key);

    /**
     * 查询全部规则
     *
     * @return
     */
    @Results(id = "sequenceRuleDefinitionResults", value = {
            @Result(column = "id", property = "id", javaType = Long.class),
            @Result(column = "key", property = "key", javaType = String.class),
            @Result(column = "increment", property = "increment", javaType = Integer.class),
            @Result(column = "memory_capacity", property = "memoryCapacity", javaType = Integer.class),
            @Result(column = "reload_threshold_rate", property = "reloadThresholdRate", javaType = Integer.class),
            @Result(column = "prefix", property = "prefix", javaType = String.class),
            @Result(column = "digits", property = "digits", javaType = Byte.class),
            @Result(column = "status", property = "status", javaType = Byte.class),
            @Result(column = "reset_rule", property = "resetRule", javaType = String.class)

    })
    @Select("select * from t_sequence_rule where status=1")
    List<SequenceRuleDefinition> runningAll();

}