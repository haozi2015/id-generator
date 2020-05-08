package com.haozi.id.generator.metric.client;

import com.haozi.id.generator.core.util.NamedThreadFactory;
import com.haozi.id.generator.metric.common.Response;
import com.haozi.id.generator.metric.util.HostUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
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
    private static final int OK_STATUS = 200;
    private static final long DEFAULT_INTERVAL = 1000 * 10;

    private ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(2,
            new NamedThreadFactory("heartbeat-task", true),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    private static Map<String, String> message = new HashMap<>();

    static {
        message.put("ip", HostUtil.getIp());
        message.put("host", HostUtil.getHostName());
    }

    private RestTemplate restTemplate;
    private String appPort;
    private String dashboard;

    public Heartbeat(RestTemplate restTemplate, String appPort, String dashboard) {
        this.restTemplate = restTemplate;
        this.appPort = appPort;
        this.dashboard = dashboard;
    }

    private void sendHeartbeat() {
        message.put("port", appPort);
        try {
            Response response = restTemplate.getForObject(dashboard + HEARTBEAT_PATH, Response.class, message);
            if (response.getCode() == OK_STATUS) {
            }
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
