package com.haozi.id.generator.console.controler;

import com.haozi.id.generator.console.service.SequenceAdminService;
import com.haozi.id.generator.core.rule.repository.SequenceEnum;
import com.haozi.id.generator.core.rule.repository.SequenceRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/manage/rule")
public class IdManageController {
    @Resource
    private SequenceAdminService sequenceAdminService;

    @Value("${generate.id.default-page-size:20}")
    private Integer defaultPageSize;

    /**
     * 详情
     *
     * @param key
     * @return
     */
    @RequestMapping("/detail")
    public Object getRule(@RequestParam(value = "key") String key) {
        Assert.notNull(key, "key is null");
        return sequenceAdminService.getRule(key);
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping("/list")
    public Object query(@RequestParam(value = "key", required = false) String key,
                        @RequestParam(value = "status", required = false) Byte statusValue,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }

        SequenceEnum.Status status = null;
        if (statusValue != null) {
            status = SequenceEnum.getStatus(statusValue);
        }
        Long count = sequenceAdminService.getRuleCount(key, status);
        List<SequenceRule> list = Collections.EMPTY_LIST;
        if (count > 0) {
            list = sequenceAdminService.getRuleByPage(key, status, page, pageSize);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("count", count);
        data.put("page", page);
        data.put("pageSize", pageSize);
        return data;
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping("/query/key")
    public Object queryKey(String key) {
        Assert.hasText(key, "key not null");
        return sequenceAdminService.getOffset(key);
    }

    /**
     * 增加
     *
     * @return
     */
    @RequestMapping("/add")
    public Object add(@RequestBody SequenceRule sequenceRule) {
        return sequenceAdminService.insert(sequenceRule);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping("/update")
    public Object update(@RequestBody SequenceRule sequenceRule) {
        return sequenceAdminService.update(sequenceRule);
    }

    /**
     * 用于使用客户端接入前设置起点值
     *
     * @param key
     * @param initialValue
     * @return
     */
    @RequestMapping("/initial")
    public Object initialSequence(@RequestParam("key") String key, @RequestParam("initial") long initialValue) {
        Assert.hasText(key, "key not null");
        Assert.notNull(initialValue, "initialValue not null");
        return sequenceAdminService.initialValue(key, initialValue);
    }

    /**
     * 启动
     *
     * @param key
     * @return
     */
    @RequestMapping("/run")
    public Object run(String key) {
        Assert.hasText(key, "key not null");
        return sequenceAdminService.run(key);
    }

    /**
     * 停止
     *
     * @param key
     * @return
     */
    @RequestMapping("/stop")
    public Object stop(String key) {
        Assert.hasText(key, "key not null");
        return sequenceAdminService.stop(key);
    }
}
