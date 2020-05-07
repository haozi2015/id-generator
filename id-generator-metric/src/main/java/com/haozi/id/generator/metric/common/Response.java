package com.haozi.id.generator.metric.common;

import lombok.Data;

/**
 * @author haozi
 * @date 2020/5/73:50 下午
 */
@Data
public class Response {
    private final static transient int OK_CODE = 200;

    private Integer code;
    private Object data;

    public static <T> Response buildSuccess(T data) {
        Response response = new Response();
        response.setCode(OK_CODE);
        response.setData(data);
        return response;
    }
}
