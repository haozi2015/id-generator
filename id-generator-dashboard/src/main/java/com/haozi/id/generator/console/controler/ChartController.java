package com.haozi.id.generator.console.controler;

import com.haozi.id.generator.console.service.SequenceAdminService;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import com.haozi.id.generator.metric.common.MetricEntity;
import com.haozi.id.generator.metric.server.repository.MetricsRepository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ID管理
 * <p>
 * 开发自用
 *
 * @author haozi
 * @date 2019-10-2910:46
 */
@CrossOrigin
@RestController
@RequestMapping("/chart")
public class ChartController {
    @Resource
    private MetricsRepository<MetricEntity> metricsRepository;
    @Resource
    private SequenceAdminService sequenceAdminService;

    /**
     * 详情
     *
     * @param key
     * @return
     */
    @RequestMapping("/buffer/used")
    public Object getRule(@RequestParam(value = "key") String key) {
        Assert.notNull(key, "key is null");
        Map<String, List<MetricEntity>> metricEntitys = metricsRepository.queryAllByKey(key);
        Set<String> time = new TreeSet<>();
        List<Map<String, Object>> list = new ArrayList<>(metricEntitys.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        metricEntitys.forEach((k, v) -> {
            Map<String, Object> client = new HashMap<>();
            client.put("name", k);
            client.put("type", "line");
            client.put("stack", "总量");
            client.put("data", v.stream().map(MetricEntity::getMemoryCount).collect(Collectors.toList()));
            list.add(client);
            time.addAll(v.stream().map(MetricEntity::getLocalData).map(date -> sdf.format(date)).collect(Collectors.toList()));
        });
        SequenceRule rule = sequenceAdminService.getRule(key);
        int reloadThresholdSize = rule.getReloadThreshold();
        Map<String, Object> client = new HashMap<>();
        client.put("name", " 内存阈值");
        client.put("type", "line");
        client.put("stack", "总量");
        client.put("data", time.stream().map(a -> reloadThresholdSize).collect(Collectors.toList()));
        list.add(client);

        Map<String, Object> result = new HashMap<>(2);
        result.put("time", time);
        result.put("data", list);
        return result;
    }
}
