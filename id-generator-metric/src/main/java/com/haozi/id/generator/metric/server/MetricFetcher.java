package com.haozi.id.generator.metric.server;

import com.haozi.id.generator.core.util.NamedThreadFactory;
import com.haozi.id.generator.metric.common.MetricEntity;
import com.haozi.id.generator.metric.common.Response;
import com.haozi.id.generator.metric.server.discovery.ClientDiscovery;
import com.haozi.id.generator.metric.server.discovery.ClientInfo;
import com.haozi.id.generator.metric.server.repository.MetricsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Fetch metric of machines.
 *
 * @author haozi
 * @date 2020/4/305:14 下午
 */
@Slf4j
public class MetricFetcher {

    private static final long MAX_LAST_FETCH_INTERVAL_MS = 1000 * 15;
    private static final long FETCH_INTERVAL_SECOND = 6;
    private final static String METRIC_URL_PATH = "metric";
    private final long intervalSecond = 1;

    private AtomicLong lastFetchTime = new AtomicLong();

    private MetricsRepository<MetricEntity> metricStore;
    private ClientDiscovery machineDiscovery;
    private RestTemplate restTemplate;

    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    private ScheduledExecutorService fetchScheduleService = Executors.newScheduledThreadPool(1,
            new NamedThreadFactory("dashboard-metrics-fetch-task"));
    private ExecutorService fetchWorker;

    public MetricFetcher(MetricsRepository<MetricEntity> metricStore, ClientDiscovery machineDiscovery, RestTemplate restTemplate) {
        this.metricStore = metricStore;
        this.machineDiscovery = machineDiscovery;
        this.restTemplate = restTemplate;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 0;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new DiscardPolicy();
        fetchWorker = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("dashboard-metrics-fetchWorker"), handler);
        start();
    }

    private void start() {
        fetchScheduleService.scheduleAtFixedRate(() -> {
            try {
                fetchMetric();
            } catch (Exception e) {
                log.info("fetchAllApp error:", e);
            }
        }, 10, intervalSecond, TimeUnit.SECONDS);
    }

    private void fetchMetric() {
        long now = System.currentTimeMillis();
        long lastFetchMs = now - MAX_LAST_FETCH_INTERVAL_MS;
        if (lastFetchTime != null) {
            lastFetchMs = Math.max(lastFetchMs, lastFetchTime.get() + 1000);
        }
        // trim milliseconds
        lastFetchMs = lastFetchMs / 1000 * 1000;
        long endTime = lastFetchMs + FETCH_INTERVAL_SECOND * 1000;
        if (endTime > now - 1000 * 2) {
            // to near
            return;
        }
        // update last_fetch in advance.
        lastFetchTime.set(endTime);
        final long finalLastFetchMs = lastFetchMs;
        final long finalEndTime = endTime;
        try {
            // do real fetch async
            fetchWorker.submit(() -> {
                try {
                    fetchOnce(finalLastFetchMs, finalEndTime, 5);
                } catch (Exception e) {
                    log.info("fetchOnce error", e);
                }
            });
        } catch (Exception e) {
            log.info("submit fetchOnc fail, intervalMs [" + lastFetchMs + ", " + endTime + "]", e);
        }
    }

    /**
     * fetch metric between [startTime, endTime], both side inclusive
     */
    private void fetchOnce(long startTime, long endTime, int maxWaitSeconds) {
        if (maxWaitSeconds <= 0) {
            throw new IllegalArgumentException("maxWaitSeconds must > 0, but " + maxWaitSeconds);
        }
        Set<ClientInfo> machines = machineDiscovery.getAllClient();
        if (machines.isEmpty()) {
            return;
        }
        Date localDate = new Date();
        for (final ClientInfo machine : machines) {
            // auto remove
            if (machine.isDead()) {
                machineDiscovery.removeClient(machine);
                log.info("Dead machine removed: {}:{} of {}", machine.getIp(), machine.getPort());
                continue;
            }
            if (!machine.isHealthy()) {
                continue;
            }
            final String url = "http://" + machine.getIp() + ":" + machine.getPort() + "/" + METRIC_URL_PATH
                    + "?startTime=" + startTime + "&endTime=" + endTime + "&refetch=" + false;
            Response result = restTemplate.getForObject(url, Response.class);
            List<MetricEntity> data = (List<MetricEntity>) result;
            data.forEach(m -> m.setLocalData(localDate));
            metricStore.saveAll(data);
        }
    }
}
