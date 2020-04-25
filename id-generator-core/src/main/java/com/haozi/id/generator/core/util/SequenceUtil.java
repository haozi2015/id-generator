package com.haozi.id.generator.core.util;

import com.haozi.id.generator.core.sequence.SequenceEnum;
import com.haozi.id.generator.core.sequence.SequenceRuntime;
import com.haozi.id.generator.core.sequence.dao.SequenceRuleDefinition;

import java.time.LocalDate;

/**
 * @author haozi
 * @date 2020/4/243:18 下午
 */
public class SequenceUtil {
    //提前重置时间（秒）
    private final static long RESET_TIME = 60L * 30L;

    public static String getNowSequenceKey(SequenceRuleDefinition sequenceRule) {
        return getSequenceKey(sequenceRule, SequenceEnum.Runtime.NOW);
    }

    public static String getSequenceKey(SequenceRuleDefinition sequenceRule, SequenceEnum.Runtime keyRule) {
        String key = sequenceRule.getKey();

        switch (sequenceRule.getResetRule()) {
            case "DD": {
                LocalDate localDate = LocalDate.now().plusDays(keyRule.getOffsetTime());
                return key + localDate.toString();
            }
            case "MM": {
                LocalDate localDate = LocalDate.now().plusMonths(keyRule.getOffsetTime());
                return key + localDate.getYear() + "-" + localDate.getMonth();
            }
            case "YY": {
                LocalDate localDate = LocalDate.now().plusYears(keyRule.getOffsetTime());
                return key + localDate.getYear();
            }
            default:
                return key;
        }
    }

    /**
     * 获取运行时序列
     *
     * @param sequenceRule
     * @return
     */
    public static SequenceRuntime createRuntimeSequence(SequenceRuleDefinition sequenceRule, SequenceEnum.Runtime keyRule) {
        String key = sequenceRule.getKey();

        switch (sequenceRule.getResetRule()) {
            case "DD": {
                LocalDate localDate = LocalDate.now().plusDays(keyRule.getOffsetTime());
                return new SequenceRuntime(sequenceRule, key + localDate.toString(), localDate);
            }
            case "MM": {
                LocalDate localDate = LocalDate.now().plusMonths(keyRule.getOffsetTime());
                return new SequenceRuntime(sequenceRule, key + localDate.getYear() + "-" + localDate.getMonth(), localDate);
            }
            case "YY": {
                LocalDate localDate = LocalDate.now().plusYears(keyRule.getOffsetTime());
                return new SequenceRuntime(sequenceRule, key + localDate.getYear(), localDate);
            }
            default:
                return new SequenceRuntime(sequenceRule, key);
        }
    }

    /**
     * 是否重置
     *
     * @param sequenceRule
     * @return
     */
    public static boolean isReset(SequenceRuleDefinition sequenceRule) {
        switch (sequenceRule.getResetRule()) {
            case "DD": {
                //距离当天结束小于半小时，return true
                return DateUtil.getRemainSeconds() < RESET_TIME;
            }
            case "MM": {
                //月份最后一天
                if (!DateUtil.isLastDayInMonth()) {
                    return false;
                }
                //距离当天结束小于半小时，return true
                return DateUtil.getRemainSeconds() < RESET_TIME;
            }
            case "YY": {
                //月份最后一天
                if (!DateUtil.isLastDayInYear()) {
                    return false;
                }
                //距离当天结束小于半小时，return true
                return DateUtil.getRemainSeconds() < RESET_TIME;
            }
            default:
                return false;
        }
    }


}
