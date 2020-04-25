package com.haozi.id.generator.core.sequence;

import com.haozi.id.generator.core.sequence.dao.SequenceRuleDefinition;
import lombok.Data;

import java.time.LocalDate;

/**
 * 运行时序列
 *
 * @author haozi
 * @date 2019-11-2700:10
 */
@Data
public class SequenceRuntime {
    //规则
    private SequenceRuleDefinition sequenceRuleDefinition;
    //运行时规则，生成的序列key
    private String sequenceKey;
    //运行时规则，生成的时间
    private LocalDate ruleDate;

    public SequenceRuntime(SequenceRuleDefinition sequenceRuleDefinition, String sequenceKey, LocalDate ruleDate) {
        this.sequenceRuleDefinition = sequenceRuleDefinition;
        this.sequenceKey = sequenceKey;
        this.ruleDate = ruleDate;
    }

    public SequenceRuntime(SequenceRuleDefinition sequenceRuleDefinition, String sequenceKey) {
        this.sequenceRuleDefinition = sequenceRuleDefinition;
        this.sequenceKey = sequenceKey;
    }
}
