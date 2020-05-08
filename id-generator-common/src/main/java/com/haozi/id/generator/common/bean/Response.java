package com.haozi.id.generator.common.bean;

import lombok.Data;

/**
 * @author zhanghao
 * @date 2020/5/811:49 上午
 */
@Data
public class Response<T> {
    private final static transient int OK_CODE = 0;
    private final static transient int ERROR_CODE = 1;

    private Integer code;
    private String msg;
    private T data;
    private long timestamp;

    public static <T> Response success(T data) {
        Response response = new Response();
        response.setCode(OK_CODE);
        response.setTimestamp(System.currentTimeMillis());
        response.setData(data);
        return response;
    }

    public static Response error() {
        Response response = new Response();
        response.setCode(ERROR_CODE);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public static Response error(String msg) {
        Response response = new Response();
        response.setCode(ERROR_CODE);
        response.setMsg(msg);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public boolean isSuccess() {
        return code != null && code == OK_CODE;
    }
}
