package com.haozi.id.generator.core.rule.repository.mysql;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SequenceMapper {

    /**
     * 覆盖更新
     *
     * @param sequenceKey
     * @param offset
     * @return
     */
    @Update("update t_sequence set offset = #{offset} where sequence_key = #{sequenceKey}")
    int updateOffset(@Param("sequenceKey") String sequenceKey, @Param("offset") Long offset);

    /**
     * 查询偏移量
     *
     * @param sequenceKey
     * @return
     */
    @Select("select offset from t_sequence where sequence_key = #{sequenceKey}")
    Long selectOffsetByKey(String sequenceKey);

    /**
     * 更新偏移量，原子加操作
     *
     * @param sequenceKey
     * @param incValue
     * @return
     */
    @Insert("INSERT INTO t_sequence (sequence_key,offset) VALUES (#{sequenceKey},#{incValue}+1) ON DUPLICATE KEY UPDATE offset = offset + #{incValue}")
    int incOffsetByKey(@Param("sequenceKey") String sequenceKey, @Param("incValue") Long incValue);

}