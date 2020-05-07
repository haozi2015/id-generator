package com.haozi.id.generator.metric.server.discovery;

import lombok.Data;

/**
 * 客户端信息对象
 *
 * @author haozi
 * @date 2020/5/74:42 下午
 */
@Data
public class ClientInfo implements Comparable<ClientInfo> {

    private String hostname = "";
    private String ip = "";
    private Integer port = -1;
    private long lastHeartbeat;

    public String getHost() {
        return "".equals(hostname) ? ip + ":" + port : hostname;
    }

    public boolean isHealthy() {
        long delta = System.currentTimeMillis() - lastHeartbeat;
        return delta < 1000L * 60 * 5;
    }

    /**
     * whether dead should be removed
     *
     * @return
     */
    public boolean isDead() {
        long delta = System.currentTimeMillis() - lastHeartbeat;

        return delta > 1000L * 60 * 5;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    @Override
    public int compareTo(ClientInfo o) {
        if (this == o) {
            return 0;
        }
        if (!port.equals(o.getPort())) {
            return port.compareTo(o.getPort());
        }
        return ip.compareToIgnoreCase(o.getIp());
    }

}
