package com.haozi.id.generator.core.util;

import com.haozi.id.generator.core.rule.RuntimeSequence;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import org.springframework.util.StringUtils;

/**
 * id生成工具类
 *
 * @author haozi
 * @date 2019-11-0815:51
 */
public class IdUtil {

    /**
     * 替换前缀规则
     * <p>
     * TODO 暂时满足现有需要，后续再支持递归和拼接
     *
     * @param runtimeSequence
     * @return
     */
    private static String prefixRep(RuntimeSequence runtimeSequence) {
        SequenceRule sequenceRule = runtimeSequence.getSequenceRule();
        String prefix = sequenceRule.getPrefix();
        if (StringUtils.isEmpty(prefix)) {
            return prefix;
        }
        return PrefixTagUtil.replaceTag(prefix);
    }

    /**
     * 生成ID
     * <p>
     * 拼接前缀+补0+ID序号
     *
     * @param id
     * @param runtimeSequence
     * @param <T>             Long or String
     * @return
     */
    public static <T> T generateId(Long id, RuntimeSequence runtimeSequence) {
        SequenceRule sequenceRule = runtimeSequence.getSequenceRule();
        String prefix = sequenceRule.getPrefix();
        Byte digits = sequenceRule.getDigits();
        //无补0 且 无前缀 返回Long类型
        if (digits <= 0 && StringUtils.isEmpty(prefix)) {
            return (T) id;
        }
        String idStr = null;
        /**
         * TODO 考虑如果超出设定位数，是否要抛异常，不能再生产超出位数的ID
         */
        int idDigits = (int) Math.log10(id) + 1;
        //位数不足 && 没超过规定位数
        if (digits > 0 && idDigits < digits) {
            idStr = String.format("%0" + digits + "d", id);
        } else {
            idStr = String.valueOf(id);
        }
        return (T) (prefixRep(runtimeSequence) + idStr);
    }

}
