package com.haozi.id.generator.metric.server.discovery;

import org.springframework.util.Assert;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class SimpleClientDiscovery implements ClientDiscovery {

    private final Set<ClientInfo> clients = ConcurrentHashMap.newKeySet();

    @Override
    public long addClient(ClientInfo ClientInfo) {
        Assert.notNull(ClientInfo, "ClientInfo cannot be null");
        clients.add(ClientInfo);
        return 1;
    }

    @Override
    public boolean removeClient(String app, String ip, int port) {
        Assert.hasText(app, "app name cannot be blank");
        return false;
    }

    @Override
    public boolean removeClient(ClientInfo ClientInfo) {
        return clients.remove(ClientInfo);
    }

    @Override
    public Set<ClientInfo> getAllClient() {
        return clients;
    }
}
