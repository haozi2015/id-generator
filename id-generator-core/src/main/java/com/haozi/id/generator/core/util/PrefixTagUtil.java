package com.haozi.id.generator.core.util;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * 替换标签
 *
 * @author zhanghao
 * @date 2020/5/2711:33 上午
 */
public class PrefixTagUtil {

    private final static Function<String, String> function = createFun(v -> v);

    private static Function<String, String> createFun(Function<String, String> function) {
        return function
                //适用至2100年
                .andThen(v -> v.replace("${YY}", String.valueOf(LocalDate.now().getYear() - 2000)))
                .andThen(v -> v.replace("${YYYY}", String.valueOf(LocalDate.now().getYear())))
                .andThen(v -> v.replace("${MM}", String.valueOf(LocalDate.now().getMonth())))
                .andThen(v -> v.replace("${DD}", String.valueOf(LocalDate.now().getDayOfMonth())));
    }

    /**
     * 替换标签
     *
     * @param prefix
     * @return
     */
    public static String replaceTag(String prefix) {
        if (!prefix.contains("$")) {
            return prefix;
        }
        return function.apply(prefix);
    }
}
