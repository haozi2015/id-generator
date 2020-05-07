package com.haozi.id.generator.metric.server.discovery;

import java.util.Set;

/**
 * 维护客户端接口
 *
 * @author haozi
 * @date 2020/5/74:42 下午
 */
public interface ClientDiscovery {

    long addClient(ClientInfo ClientInfo);

    boolean removeClient(String app, String ip, int port);

    boolean removeClient(ClientInfo ClientInfo);

    Set<ClientInfo> getAllClient();
}