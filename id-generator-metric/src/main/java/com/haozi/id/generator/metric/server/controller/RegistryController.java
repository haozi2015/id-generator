package com.haozi.id.generator.metric.server.controller;

import com.haozi.id.generator.metric.server.discovery.ClientDiscovery;
import com.haozi.id.generator.metric.server.discovery.ClientInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public class RegistryController {

    private ClientDiscovery clientDiscovery;

    public RegistryController(ClientDiscovery clientDiscovery) {
        this.clientDiscovery = clientDiscovery;
    }

    @ResponseBody
    @RequestMapping("/registry/machine")
    public Object receiveHeartBeat(String hostname, String ip, Integer port) {
        Assert.notNull(ip, "ip can't be null");
        Assert.notNull(port, "port can't be null");
        Assert.isTrue(port != -1, "your port not set yet");

        try {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setHostname(hostname);
            clientInfo.setIp(ip);
            clientInfo.setPort(port);
            clientInfo.setLastHeartbeat(System.currentTimeMillis());
            clientDiscovery.addClient(clientInfo);
            return "success";
        } catch (Exception e) {
            log.error("Receive heartbeat error", e);
            throw e;
        }
    }
}
