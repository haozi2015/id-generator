package com.haozi.id.generator.demo.dao;

import com.haozi.id.generator.plugin.mybatis.IdField;
import lombok.Data;

/**
 * @author haozi
 * @date 2020/5/206:59 下午
 */
@Data
public class DemoModel {
    @IdField("test1")
    private String test1;
    @IdField("pay_id")
    private Integer test2;

}
