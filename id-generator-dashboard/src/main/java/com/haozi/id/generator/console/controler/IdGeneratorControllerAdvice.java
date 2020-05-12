package com.haozi.id.generator.console.controler;

import com.alibaba.fastjson.JSON;
import com.haozi.id.generator.bean.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author haozi
 * @date 2020/4/242:11 下午
 */
@Slf4j
@RestControllerAdvice
public class IdGeneratorControllerAdvice implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest request) {
        log.error("自定义拦截异常", e);
        return Response.error(e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(RequestMapping.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            return JSON.toJSONString(Response.success(body));
        }
        return Response.success(body);

    }
}
