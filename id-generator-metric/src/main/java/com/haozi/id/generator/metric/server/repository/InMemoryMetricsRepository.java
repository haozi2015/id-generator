package com.haozi.id.generator.metric.server.repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.haozi.id.generator.metric.common.MetricEntity;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caches metrics data in a period of time in memory.
 *
 * @author haozi
 * @date 2020/4/2611:04 上午
 */
public class InMemoryMetricsRepository implements MetricsRepository<MetricEntity> {

    private static final long MAX_METRIC_LIVE_TIME_MS = 1000;//1000 * 60 * 5;

    /**
     * {@code app -> resource -> timestamp -> metric}
     */
    private Map<String/* key */, Map<String/* ip:port */, ConcurrentLinkedHashMap</* timestamp */Long, MetricEntity>>> allMetrics = new ConcurrentHashMap<>();

    @Override
    public void save(MetricEntity metric) {
        if (metric == null || StringUtils.isEmpty(metric.getKey())) {
            return;
        }
        allMetrics.computeIfAbsent(metric.getKey(), e -> new ConcurrentHashMap<>(16))
                .computeIfAbsent(metric.getNode(), e -> new ConcurrentLinkedHashMap.Builder<Long, MetricEntity>()
                        .maximumWeightedCapacity(MAX_METRIC_LIVE_TIME_MS).weigher((key, value) -> {
                            // Metric older than {@link #MAX_METRIC_LIVE_TIME_MS} will be removed.
                            int weight = (int) (System.currentTimeMillis() - key);
                            // weight must be a number greater than or equal to one
                            return Math.max(weight, 1);
                        }).build()).put(metric.getAppTimestamp(), metric);
    }

    @Override
    public synchronized void saveAll(Iterable<MetricEntity> metrics) {
        if (metrics == null) {
            return;
        }
        metrics.forEach(entity -> {
            save(entity);
        });
    }

    @Override
    public Map<String, List<MetricEntity>> queryByKeyAndBetween(String key, long startTime, long endTime) {
        if (StringUtils.isEmpty(key)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, ConcurrentLinkedHashMap<Long, MetricEntity>> clusterMap = allMetrics.get(key);
        if (clusterMap == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, List<MetricEntity>> results = new HashMap<>();
        clusterMap.forEach((k, v) -> {
            List<MetricEntity> list = new ArrayList<>();
            v.forEach((k1, v1) -> {
                if (k1 > startTime && k1 < endTime) {
                    list.add(v1);
                }
            });
            results.put(k, list);
        });
        return results;
    }

    @Override
    public Map<String, List<MetricEntity>> queryAllByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, ConcurrentLinkedHashMap<Long, MetricEntity>> clusterMap = allMetrics.get(key);
        if (clusterMap == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, List<MetricEntity>> results = new HashMap<>();
        clusterMap.forEach((k, v) -> {
            results.put(k, new ArrayList<>(v.values()));
        });
        return results;
    }
}
