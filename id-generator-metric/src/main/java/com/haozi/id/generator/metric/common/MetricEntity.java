package com.haozi.id.generator.metric.common;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author haozi
 * @date 2020/4/305:14 下午
 */
@Data
@Builder
public class MetricEntity {
    private String ip;
    private String port;
    private String key;
    private Long memoryCount;
    private Long appTimestamp;
    private Date localData;

    public String getNode() {
        return ip + ":" + port;
    }

}
