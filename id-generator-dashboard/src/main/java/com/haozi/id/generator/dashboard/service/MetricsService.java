package com.haozi.id.generator.dashboard.service;


import com.haozi.id.generator.metric.common.MetricEntity;
import com.haozi.id.generator.metric.server.repository.MetricsRepository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author haozi
 * @date 2020/5/611:02 上午
 */
public class MetricsService {
    @Resource
    private MetricsRepository<MetricEntity> metricsRepository;

    public List<Map<String, Object>> query(String key) {
        Map<String, List<MetricEntity>> data = metricsRepository.queryAllByKey(key);
        return Collections.EMPTY_LIST;
    }
}
