package com.haozi.id.generator.core.sequence.dao;

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
     * @param key
     * @return
     */
    @Select("select offset from t_sequence where sequence_key = #{key}")
    Long selectOffsetByKey(String key);

    /**
     * 更新偏移量，原子加操作
     *
     * @param key
     * @param incValue
     * @return
     */
    @Insert("INSERT INTO t_sequence (sequence_key,offset) VALUES (#{key},#{incValue}+1) ON DUPLICATE KEY UPDATE offset = offset + #{incValue}")
    int updateOffsetByKey(@Param("key") String key, @Param("incValue") Long incValue);
}