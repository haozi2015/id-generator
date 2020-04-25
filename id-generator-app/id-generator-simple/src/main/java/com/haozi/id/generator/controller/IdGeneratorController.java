package com.haozi.id.generator.controller;

import com.haozi.id.generator.core.id.IdFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 获取ID
 * <p>
 * http方式
 *
 * @author haozi
 * @date 2019-10-2910:46
 */
@RestController
public class IdGeneratorController {

    @Resource
    private IdFactory idFactory;

    /**
     * id生成
     * <p>
     * 根据key不同，生成不同id
     * <p>
     * 根据num不同，返回1个或多个
     *
     * @return
     */
    @RequestMapping("/generate/id")
    public Object generator(String key, @RequestParam(required = false, defaultValue = "1") Integer num) {
        return idFactory.getId(key, num);
    }

    /**
     * 全局唯一id生成
     * <p>
     *
     * <p>
     * 根据num不同，返回1个或多个，最多限制100个（包含100）
     *
     * @return
     */
    @RequestMapping("/generate/guid")
    public Object generator(@RequestParam(required = false, defaultValue = "1") Integer num) {
        return idFactory.getGuid(num);

    }
}
