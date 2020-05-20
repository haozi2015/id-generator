package com.haozi.id.generator.plugin.mybatis.intercept;

import com.haozi.id.generator.plugin.mybatis.IdField;
import com.haozi.id.generator.plugin.mybatis.MyBatisIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义mybatis拦截器
 * <p>
 * 参数解析前，将声明注解变量补充ID
 *
 * @author haozi
 * @date 2020/5/1911:09 下午
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class IdGeneratorInterceptor implements Interceptor {
    private final static Set<Class> SUPPORT_CLASS_TYPE = new HashSet<>();

    static {
        SUPPORT_CLASS_TYPE.add(Long.class);
        SUPPORT_CLASS_TYPE.add(Integer.class);
        SUPPORT_CLASS_TYPE.add(String.class);
    }

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

        Object parameter = invocation.getArgs()[1];
        if (parameter instanceof Map) {
            //Mybatis参数params1、params2的值与自定义@param名称的值重复，用set去重复
            Set<Object> parameterSet = new HashSet<>(((Map) parameter).values());
            Object arg1 = parameterSet.iterator().next();

            if (parameterSet.size() > 1) {
                log.warn("[INSERT]参数类型数量大于1，IdGeneratorInterceptor拦截器仅对第一个参数[{}]注入ID！！！", arg1);
            }

            //集合批量获取ID
            if (arg1 instanceof Collection) {
                Collection collection = (Collection) arg1;
                Class<?> aClass = collection.iterator().next().getClass();
                Map<Field, String> idField = findIdField(aClass);
                if (!idField.isEmpty()) {
                    Map<String, Iterator> collect = idField.values().stream().collect(Collectors.toMap(a -> a, b -> idGenerator.generateId(b, collection.size()).iterator()));
                    collection.forEach(p -> this.setId(idField, p, collect));
                }
            } else {
                //单对象
                this.setId(arg1);
            }
        } else {
            //单对象
            this.setId(parameter);
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

    private void setId(Object parameter) {
        Map<Field, String> idField = findIdField(parameter.getClass());
        if (!idField.isEmpty()) {
            Map<String, Iterator> collect = idField.values().stream().collect(Collectors.toMap(a -> a, b -> idGenerator.generateId(b, 1).iterator()));
            this.setId(idField, parameter, collect);
        }
    }

    private void setId(Map<Field, String> idField, Object data, Map<String, Iterator> ids) {
        idField.forEach((field, key) -> {
            try {
                if (field.getType() == Integer.class) {
                    field.set(data, ((Long) ids.get(key).next()).intValue());
                } else {
                    field.set(data, ids.get(key).next());
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private Map<Field, String> findIdField(Class<?> clazz) {
        Map<Field, String> map = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            IdField annotation = field.getAnnotation(IdField.class);
            if (annotation == null) {
                continue;
            }
            if (!SUPPORT_CLASS_TYPE.contains(field.getType())) {
                throw new UnsupportedOperationException("注解`@IdField`不支持数据类型[" + field.getType() + "]，支持：" + SUPPORT_CLASS_TYPE);
            }
            field.setAccessible(true);
            map.put(field, annotation.value());
        }
        return map;
    }
}
