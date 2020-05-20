package com.haozi.id.generator.plugin.mybatis.intercept;

import com.haozi.id.generator.plugin.mybatis.IdField;
import com.haozi.id.generator.plugin.mybatis.MyBatisIdGenerator;
import org.apache.ibatis.javassist.bytecode.analysis.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * insert前补充ID
 *
 * @author haozi
 * @date 2020/5/1911:09 下午
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class IdGeneratorInterceptor implements Interceptor {
    private MyBatisIdGenerator idGenerator;

    public IdGeneratorInterceptor(MyBatisIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (!SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        // 获取参数
        Object parameter = invocation.getArgs()[1];
        if (parameter instanceof Collection) {
            Collection parameter2 = (Collection) parameter;
            Class<?> aClass = parameter2.iterator().next().getClass();
            Map<Field, String> idField = findIdField(aClass);
            if (!idField.isEmpty()) {
                Map<String, Iterator> collect = idField.values().stream().collect(Collectors.toMap(a -> a, b -> idGenerator.generateId(b, parameter2.size()).iterator()));
                for (Object obj : parameter2) {
                    idField.forEach((f, k) -> {
                        try {
                            f.set(obj, collect.get(k).next());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } else {
            Map<Field, String> idField = findIdField(parameter.getClass());
            if (!idField.isEmpty()) {
                Map<String, Iterator> collect = idField.values().stream().collect(Collectors.toMap(a -> a, b -> idGenerator.generateId(b, 1).iterator()));
                idField.forEach((f, k) -> {
                    try {
                        f.set(parameter, collect.get(k).next());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private Map<Field, String> findIdField(Class<?> clazz) {
        Map<Field, String> map = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            IdField annotation = field.getAnnotation(IdField.class);
            if (annotation != null) {
                field.setAccessible(true);
                map.put(field, annotation.value());
            }
        }
        return map;
    }
}
