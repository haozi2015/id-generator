package com.haozi.id.generator.core.buffer;

import com.haozi.id.generator.common.ServiceThread;
import com.haozi.id.generator.core.IdGeneratorFactory;
import com.haozi.id.generator.core.rule.repository.SequenceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 销毁buffer中过期的sequence_key
 *
 * @author haozi
 * @date 2019-11-1016:40
 */
@Slf4j
public class CleanIdBuffer extends ServiceThread {
    //15分钟 TODO 加到配置项
    private final static Long DEFAULT_WAIT_INTERVAL = 15L * 60 * 1000L;

    private IdGeneratorFactory idGeneratorFactory;

    public CleanIdBuffer(IdGeneratorFactory idGeneratorFactory) {
        this.idGeneratorFactory = idGeneratorFactory;
    }

    private void destroy() {
        idGeneratorFactory.getSequenceRuleService().getRunningRule().stream()
                //是否配置重置
                .filter(sequenceRule -> !StringUtils.isEmpty(sequenceRule.getResetRule()))
                //循环删除
                .forEach(ruleDefinition -> {
                    String runtimeKey = idGeneratorFactory.getSequenceRuleService().getSequenceRuntimeKey(ruleDefinition, SequenceEnum.Runtime.PREV);
                    BufferPool.remove(runtimeKey);
                });
    }

    @Override
    public String getServiceName() {
        return "Id-Destroy-Thread";
    }

    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        while (!this.isStopped()) {
            this.waitForRunning(DEFAULT_WAIT_INTERVAL);
            this.destroy();
        }

        log.info(this.getServiceName() + " service end");
    }


}
