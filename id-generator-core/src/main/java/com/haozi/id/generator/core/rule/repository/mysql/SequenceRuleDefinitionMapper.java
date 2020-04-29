package com.haozi.id.generator.core.rule.repository.mysql;

import com.haozi.id.generator.core.rule.repository.SequenceRule;
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
    int updateByKey(SequenceRule record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    @InsertProvider(type = SequenceRuleDefinitionSQL.class, method = "insert")
    int insert(SequenceRule record);

    /**
     * 查询指定规则
     *
     * @param key
     * @return
     */
    @Select("select * from t_sequence_rule where `key`=#{key}")
    @ResultMap("sequenceRuleDefinitionResults")
    SequenceRule getByKey(@Param("key") String key);

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
    @Select("select * from t_sequence_rule where status=#{status}")
    List<SequenceRule> getByStatus(@Param("status") Byte status);

    /**
     * 分页查询
     *
     * @param key
     * @param status
     * @param row
     * @param pageSize
     * @return
     */
    @SelectProvider(type = SequenceRuleDefinitionSQL.class, method = "selectByPage")
    @ResultMap("sequenceRuleDefinitionResults")
    List<SequenceRule> selectByPage(@Param("key") String key,
                                    @Param("status") Byte status,
                                    @Param("row") long row,
                                    @Param("pageSize") int pageSize);

    /**
     * 查询总数
     *
     * @param key
     * @param status
     * @return
     */
    @SelectProvider(type = SequenceRuleDefinitionSQL.class, method = "selectByCount")
    long selectByCount(@Param("key") String key,
                       @Param("status") Byte status);

}