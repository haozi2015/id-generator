package com.haozi.id.generator.core.sequence.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sequence {
    /**
     * 自增主键ID
     **/
    private Long id;

    /**
     * 序列KEY
     **/
    private String sequenceKey;

    /**
     * 当前值
     **/
    private Long offset;

}