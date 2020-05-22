package com.haozi.id.generator.demo.plugin;

import com.haozi.id.generator.demo.plugin.dao.DemoMapper;
import com.haozi.id.generator.demo.plugin.dao.DemoModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mybatis插件demo
 *
 * @author haozi
 * @date 2020/5/207:07 下午
 */
@Component
public class MybatisPlugin implements InitializingBean {
    @Resource
    private DemoMapper testMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        //使用@IdField注解的对象
        DemoModel demo1 = new DemoModel();
        //调用insert方法，自动调用分布式ID生成器，注入ID值
        testMapper.insert(demo1);
    }
}
