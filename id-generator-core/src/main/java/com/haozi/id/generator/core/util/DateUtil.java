package com.haozi.id.generator.core.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 日期工具类
 *
 * @author haozi
 * @date 2019-11-1015:23
 */
public class DateUtil {
    /**
     * 获取距离当天的结束时间
     *
     * @return 秒
     */
    public static Integer getRemainSeconds() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        long seconds = ChronoUnit.SECONDS.between(now, midnight);
        return (int) seconds;
    }

    /**
     * 是否当月最后一天
     *
     * @return
     */
    public static boolean isLastDayInMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusDays(1L);
        return now.getMonth() == localDateTime.getMonth();
    }

    /**
     * 是否当年最后一天
     *
     * @return
     */
    public static boolean isLastDayInYear() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusMonths(1L);
        return now.getYear() == localDateTime.getYear();
    }
}
