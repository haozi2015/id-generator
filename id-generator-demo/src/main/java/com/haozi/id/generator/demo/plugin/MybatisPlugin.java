package com.haozi.id.generator.demo.plugin;

import com.haozi.id.generator.dubbo.api.IdGenerator;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haozi
 * @date 2020/5/207:07 下午
 */
@Component
public class MybatisPlugin implements InitializingBean {
    @Resource
    private TestMapper testMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        Test test = new Test();
        Test test2 = new Test();
        List<Test> a = new ArrayList<>();
        a.add(test);
        a.add(test2);
        testMapper.insert2(a);
    }
}
