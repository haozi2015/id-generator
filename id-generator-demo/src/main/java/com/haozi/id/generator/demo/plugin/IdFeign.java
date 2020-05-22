package com.haozi.id.generator.demo.plugin;

import com.haozi.id.generator.bean.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhanghao
 * @date 2020/5/2210:20 上午
 */
@FeignClient("id-generator")
public interface IdFeign {
    /**
     * 全局唯一ID
     *
     * @return
     */
    @GetMapping(value = "/generate/guid")
    Response generateGuId();

    /**
     * 全局唯一ID
     *
     * @param num ID个数
     * @return
     */
    @GetMapping(value = "/generate/guid")
    Response generateGuId(@RequestParam("num") int num);

    /**
     * 隔离自增ID
     *
     * @param key 隔离唯一标识
     * @return
     */
    @GetMapping(value = "/generate/id")
    Response generateId(@RequestParam("key") String key);

    /**
     * 隔离自增ID
     *
     * @param key 隔离唯一标识
     * @param num ID个数
     * @return
     */
    @GetMapping(value = "/generate/id")
    Response generateId(@RequestParam("key") String key, @RequestParam("num") int num);

}
