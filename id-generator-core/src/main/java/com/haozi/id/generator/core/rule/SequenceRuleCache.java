package com.haozi.id.generator.core.rule;

import com.haozi.id.generator.common.ServiceThread;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运行时规则定义缓存
 *
 * @author haozi
 * @date 2019-11-0811:45
 */
@Slf4j
public class SequenceRuleCache extends ServiceThread {
    //30秒
    private final static Long WAIT_INTERVAL = 30000L;

    //配置信息缓存
    private Map<String, SequenceRule> sequenceRuleDefinitionCache = null;

    private SequenceRuleService sequenceRuleService;

    public SequenceRuleCache(SequenceRuleService sequenceRuleService) {
        this.sequenceRuleService = sequenceRuleService;
        load();
    }

    public SequenceRule get(String key) {
        return sequenceRuleDefinitionCache.get(key);
    }

    private void load() {
        sequenceRuleDefinitionCache = sequenceRuleService.runningAllFromSource()
                .stream()
                .collect(Collectors.toMap(SequenceRule::getKey, sequenceRule -> sequenceRule));
    }

    public Collection<SequenceRule> getAllRule() {
        return sequenceRuleDefinitionCache.values();
    }

    @Override
    public String getServiceName() {
        return "Sequence-Rule-Thread";
    }

    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        while (!this.isStopped()) {
            this.waitForRunning(WAIT_INTERVAL);
            this.load();
        }

        log.info(this.getServiceName() + " service end");
    }
}
