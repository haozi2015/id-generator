package com.haozi.id.generator.metric.server;

import com.haozi.id.generator.common.bean.Response;
import com.haozi.id.generator.core.util.NamedThreadFactory;
import com.haozi.id.generator.metric.common.MetricEntity;
import com.haozi.id.generator.metric.server.discovery.ClientDiscovery;
import com.haozi.id.generator.metric.server.discovery.ClientInfo;
import com.haozi.id.generator.metric.server.repository.MetricsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

/**
 * Fetch metric of machines.
 *
 * @author haozi
 * @date 2020/4/305:14 下午
 */
@Slf4j
public class MetricFetcher {

    private final static String METRIC_URL_PATH = "metric";
    private final long intervalSecond = 5;


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
            fetchWorker.submit(() -> {
                try {
                    fetchOnce();
                } catch (Exception e) {
                    log.info("fetchOnce error", e);
                }
            });
        }, 10, intervalSecond, TimeUnit.SECONDS);
    }

    /**
     * fetch metric between [startTime, endTime], both side inclusive
     */
    private void fetchOnce() {
        Collection<ClientInfo> clientInfos = machineDiscovery.getAllClient();
        if (clientInfos.isEmpty()) {
            return;
        }
        Date localDate = new Date();
        for (final ClientInfo client : clientInfos) {
            // auto remove
            if (client.isDead()) {
                machineDiscovery.removeClient(client);
                log.info("Dead machine removed: {}:{} of {}", client.getIp(), client.getPort());
                continue;
            }
            if (!client.isHealthy()) {
                continue;
            }
            final String url = "http://" + client.getIp() + ":" + client.getPort() + "/" + METRIC_URL_PATH;
            Response result = restTemplate.getForObject(url, Response.class);
            if (!result.isSuccess()) {
                continue;
            }

            Map data = (Map) result.getData();
            if (data.isEmpty()) {
                continue;
            }
            data.forEach((k, v) -> {
                MetricEntity me = MetricEntity.builder()
                        .ip(client.getIp())
                        .port(String.valueOf(client.getPort()))
                        .appTimestamp(result.getTimestamp())
                        .key((String) k)
                        .memoryCount(Long.valueOf(v.toString()))
                        .appTimestamp(result.getTimestamp())
                        .localData(localDate)
                        .build();
                metricStore.save(me);
            });
        }
    }
}
