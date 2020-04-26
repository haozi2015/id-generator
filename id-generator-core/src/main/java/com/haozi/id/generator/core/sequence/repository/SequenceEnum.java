package com.haozi.id.generator.core.sequence.repository;

/**
 * 序列枚举
 *
 * @author haozi
 * @date 2020/4/232:45 下午
 */
public final class SequenceEnum {
    /**
     * 运行时状态
     */
    public enum Runtime {
        PREV(-1),
        NOW(0),
        NEXT(1);
        //偏移时间
        private int offsetTime;

        Runtime(int offsetTime) {
            this.offsetTime = offsetTime;
        }

        public int getOffsetTime() {
            return offsetTime;
        }
    }

    /**
     * 规则状态
     */
    public enum Status {
        //停止
        STOP((byte) 0),
        //启用
        RUNNING((byte) 1);
        private Byte value;

        Status(Byte value) {
            this.value = value;
        }

        public Byte getValue() {
            return value;
        }


    }

    public static Status getStatus(Byte value) {
        for (Status status : Status.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("value[" + value + "] 不存在");
    }
}
