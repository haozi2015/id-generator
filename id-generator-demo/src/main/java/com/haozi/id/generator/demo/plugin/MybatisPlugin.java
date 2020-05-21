package com.haozi.id.generator.demo.plugin;

import com.haozi.id.generator.demo.dao.DemoMapper;
import com.haozi.id.generator.demo.dao.DemoModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis插件 自动加入ID值
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
        DemoModel demo1 = new DemoModel();
        DemoModel demo2 = new DemoModel();
        List<DemoModel> a = new ArrayList<>();
        a.add(demo1);
        a.add(demo2);
        testMapper.insert2(a);
    }
}
