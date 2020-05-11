package com.haozi.id.generator.metric.server.discovery;

import org.springframework.util.Assert;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class SimpleClientDiscovery implements ClientDiscovery {

    private final ConcurrentMap<String, ClientInfo> clients = new ConcurrentHashMap<>();

    @Override
    public long addClient(ClientInfo clientInfo) {
        Assert.notNull(clientInfo, "ClientInfo cannot be null");
        clients.put(clientInfo.getHost(), clientInfo);
        return 1;
    }

    @Override
    public boolean removeClient(String app, String ip, int port) {
        Assert.hasText(app, "app name cannot be blank");
        return false;
    }

    @Override
    public boolean removeClient(ClientInfo clientInfo) {
        clients.remove(clientInfo.getHost());
        return true;
    }

    @Override
    public Collection<ClientInfo> getAllClient() {
        return clients.values();
    }
}
