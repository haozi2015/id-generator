package com.haozi.id.generator.dubbo.provider;

import com.haozi.id.generator.core.IdGeneratorFactory;
import com.haozi.id.generator.dubbo.api.IdGenerator;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * dubbo provider
 *
 * @author haozi
 * @date 2020/4/2410:48 下午
 */
@Service
public class IdGeneratorProvider<T> implements IdGenerator<T> {
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @Override
    public Long generateGuid() {
        return idGeneratorFactory.getGuid(1).get(0);
    }

    @Override
    public List<Long> generateGuid(int num) {
        return idGeneratorFactory.getGuid(num);
    }

    @Override
    public T generateId(String key) {
        return (T) idGeneratorFactory.getId(key, 1).get(0);
    }

    @Override
    public List<T> generateId(String key, int num) {
        return idGeneratorFactory.getId(key, num);
    }
}
