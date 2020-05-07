package com.haozi.id.generator.metric.common;

/**
 * @author haozi
 * @date 2020/5/74:19 下午
 */
public class Tuple2<T1, T2> {
    private T1 t1;
    private T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 _1() {
        return t1;
    }

    public T2 _2() {
        return t2;
    }
}
