package com.haozi.id.generator.core.id;

import com.haozi.id.generator.core.sequence.repository.SequenceEnum;
import com.haozi.id.generator.core.util.ServiceThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 销毁buffer中过期的sequence_key
 *
 * @author haozi
 * @date 2019-11-1016:40
 */
@Slf4j
public class IdDestroy extends ServiceThread {
    //15分钟 TODO 加到配置项
    private final static Long DEFAULT_WAIT_INTERVAL = 15L * 60 * 1000L;

    private IdFactory idFactory;

    public IdDestroy(IdFactory idFactory) {
        this.idFactory = idFactory;
    }

    private void destroy() {
        idFactory.getSequenceService().getRunningRule().stream()
                //是否配置重置
                .filter(sequenceRule -> !StringUtils.isEmpty(sequenceRule.getResetRule()))
                //循环删除
                .forEach(ruleDefinition -> {
                    String runtimeKey = idFactory.getSequenceService().getSequenceRuntimeKey(ruleDefinition, SequenceEnum.Runtime.PREV);
                    IdBuffer.remove(runtimeKey);
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
