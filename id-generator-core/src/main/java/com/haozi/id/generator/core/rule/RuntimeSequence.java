package com.haozi.id.generator.core.rule;

import com.haozi.id.generator.core.rule.repository.SequenceRule;
import lombok.Data;

import java.time.LocalDate;

/**
 * 运行时序列
 *
 * @author haozi
 * @date 2019-11-2700:10
 */
@Data
public class RuntimeSequence {
    //规则
    private SequenceRule sequenceRule;
    //运行时规则，生成的序列key
    private String sequenceKey;
    //运行时规则，生成的时间
    private LocalDate ruleDate;

    public RuntimeSequence(SequenceRule sequenceRule, String sequenceKey, LocalDate ruleDate) {
        this.sequenceRule = sequenceRule;
        this.sequenceKey = sequenceKey;
        this.ruleDate = ruleDate;
    }

    public RuntimeSequence(SequenceRule sequenceRule, String sequenceKey) {
        this.sequenceRule = sequenceRule;
        this.sequenceKey = sequenceKey;
    }
}
