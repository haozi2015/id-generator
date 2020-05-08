package com.haozi.id.generator.metric.client;

import com.haozi.id.generator.core.buffer.BufferPool;
import com.haozi.id.generator.core.rule.SequenceRuleService;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import com.haozi.id.generator.metric.common.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * 向服务端提供客户端的监控查询接口
 *
 * @author haozi
 * @date 2020/5/74:03 下午
 */
@Slf4j
public class MetricClientController {
    @Resource
    private SequenceRuleService sequenceRuleService;

    @ResponseBody
    @RequestMapping("/metric")
    public Object metric() {
        Collection<SequenceRule> runningRule = sequenceRuleService.getRunningRule();
        Map collect = runningRule.stream().map(rule -> {
            String nowSequenceRuntionKey = sequenceRuleService.getNowSequenceRuntimeKey(rule);
            BlockingQueue<Object> buffer = BufferPool.getBuffer(nowSequenceRuntionKey);
            int memoryCount = 0;
            if (buffer != null) {
                memoryCount = buffer.size();
            }
            return new Tuple2(rule.getKey(), memoryCount);
        }).collect(Collectors.toMap(t -> t._1(), t2 -> t2._2()));
        return collect;
    }
}
