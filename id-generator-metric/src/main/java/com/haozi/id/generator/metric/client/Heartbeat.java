package com.haozi.id.generator.metric.client;

import com.haozi.id.generator.common.bean.Response;
import com.haozi.id.generator.core.util.NamedThreadFactory;
import com.haozi.id.generator.metric.util.HostUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 客户端心跳
 * <p>
 * 定时任务向server端上传心跳
 *
 * @author haozi
 * @date 2020/5/73:23 下午
 */
@Slf4j
public class Heartbeat {
    private static final String HEARTBEAT_PATH = "/registry/machine";
    private static final String HEARTBEAT_PARAMS = new StringBuffer("?")
            .append("ip=").append(HostUtil.getIp())
            .append("&hostname=").append(HostUtil.getHostName())
            .append("&port=")
            .toString();
    private static final long DEFAULT_INTERVAL = 1000 * 10;

    private ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(2,
            new NamedThreadFactory("heartbeat-task", true),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    private RestTemplate restTemplate;
    private String port;
    private String dashboard;

    public Heartbeat(RestTemplate restTemplate, String port, String dashboard) {
        this.restTemplate = restTemplate;
        this.port = port;
        this.dashboard = dashboard;
    }

    private void sendHeartbeat() {
        String url = dashboard + HEARTBEAT_PATH + HEARTBEAT_PARAMS + port;
        try {
            Response response = restTemplate.getForObject(url, Response.class);
            log.debug("heartbeat response {}", response.toString());
        } catch (ResourceAccessException exception) {
            log.warn("dashboard error,msg[{}]", exception.getMessage());
            //exception.printStackTrace();
        }
    }

    private void init() {
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    sendHeartbeat();
                } catch (Throwable e) {
                    log.warn("[Heartbeat] error", e);
                }
            }
        }, 5000, DEFAULT_INTERVAL, TimeUnit.MILLISECONDS);
        log.info("[Heartbeat] started: "
                + this.getClass().getCanonicalName());
    }

}
