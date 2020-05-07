package com.haozi.id.generator.metric.server.repository;

import java.util.List;
import java.util.Map;

/**
 * Repository interface for aggregated metrics data.
 *
 * @author haozi
 * @date 2020/4/305:14 下午
 */
public interface MetricsRepository<T> {
    /**
     * @param metric
     */
    void save(T metric);

    /**
     * Save all metrics to the storage repository.
     *
     * @param metrics metrics to save
     */
    void saveAll(Iterable<T> metrics);

    /**
     * @param key
     * @param startTime
     * @param endTime
     * @return
     */
    Map</* ip:port */String, List<T>> queryByKeyAndBetween(String key, long startTime, long endTime);

    /**
     * @param key
     * @return
     */
    Map</* ip:port */String, List<T>> queryAllByKey(String key);
}
