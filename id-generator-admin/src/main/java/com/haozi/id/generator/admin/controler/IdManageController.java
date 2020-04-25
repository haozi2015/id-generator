package com.haozi.id.generator.admin.controler;

import com.haozi.id.generator.core.exception.IdGeneratorException;
import com.haozi.id.generator.core.id.IdBuffer;
import com.haozi.id.generator.core.sequence.SequenceEnum;
import com.haozi.id.generator.core.sequence.SequenceService;
import com.haozi.id.generator.core.sequence.dao.SequenceRuleDefinition;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ID管理
 * <p>
 * 开发自用
 *
 * @author haozi
 * @date 2019-10-2910:46
 */
@RestController
@RequestMapping("/manage/rule")
public class IdManageController {
    @Resource
    private SequenceService sequenceService;

    /**
     * 增加
     *
     * @return
     */
    @RequestMapping("/add")
    public Object add(@RequestBody SequenceRuleDefinition sequenceRule) {
        return sequenceService.insert(sequenceRule);
    }

    /**
     * 修改
     *
     * @return
     */
    @RequestMapping("/update")
    public Object update(@RequestBody SequenceRuleDefinition sequenceRule) {
        return sequenceService.update(sequenceRule);
    }

//    /**
//     * 查询
//     *
//     * @return
//     */
//    @RequestMapping("/query")
//    public Object query() {
//        return sequenceService.get();
//    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping("/query/key")
    public Object queryKey(String key) {
        return sequenceService.getConcurrentOffset(key);
    }


    /**
     * 初始化初值
     * <p>
     * 用于使用客户端接入前设置起点值
     * <p>
     *
     * @return 结果值
     */
    @RequestMapping("/initial")
    public Object initialSequence(@RequestParam("key") String key, @RequestParam("initial") long initialValue) {
        Assert.notNull(key, "key not null");
        Assert.notNull(initialValue, "initialValue not null");

        SequenceRuleDefinition sequenceRuleDefinition = sequenceService.getSequenceRuleFromSource(key);
        Assert.notNull(sequenceRuleDefinition, "SequenceRuleDefinition non-existent");

        if (SequenceEnum.Status.STOP.getValue().equals(sequenceRuleDefinition.getStatus())) {
            String sequenceKey = sequenceService.getNowSequenceRuntimeKey(sequenceRuleDefinition);
            return sequenceService.updateAndGetOffset(sequenceKey, initialValue);
        }
        throw new IdGeneratorException("Pause first，try again.");
    }

    @RequestMapping("/run")
    public Object run(String key) {
        Assert.notNull(key, "key not null");

        SequenceRuleDefinition sequenceRuleDefinition = sequenceService.getSequenceRuleFromSource(key);
        Assert.notNull(sequenceRuleDefinition, "SequenceRuleDefinition non-existent");

        if (SequenceEnum.Status.RUNNING.getValue().equals(sequenceRuleDefinition.getStatus())) {
            throw new IdGeneratorException("Already [RUNNING].");
        }
        sequenceRuleDefinition.setStatus(SequenceEnum.Status.RUNNING.getValue());
        return sequenceService.update(sequenceRuleDefinition);
    }

    @RequestMapping("/stop")
    public Object stop(String key) {
        Assert.notNull(key, "key not null");

        SequenceRuleDefinition sequenceRuleDefinition = sequenceService.getSequenceRuleFromSource(key);
        Assert.notNull(sequenceRuleDefinition, "SequenceRuleDefinition non-existent");
        if (SequenceEnum.Status.STOP.getValue().equals(sequenceRuleDefinition.getStatus())) {
            throw new IdGeneratorException("Already [RUNNING].");
        }
        sequenceRuleDefinition.setStatus(SequenceEnum.Status.RUNNING.getValue());
        int result = sequenceService.update(sequenceRuleDefinition);
        if (result > 0) {
            String sequenceKey = sequenceService.getNowSequenceRuntimeKey(sequenceRuleDefinition);
            IdBuffer.remove(sequenceKey);
        }
        return result;
    }
}
