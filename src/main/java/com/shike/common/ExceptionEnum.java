package com.shike.common;

/**
 * 异常枚举
 * Created by shike on 16/3/27.
 */
public enum  ExceptionEnum {
    UNKOWN(1000001, "未知"),
    WRONGTYPE(1000002, "类型错误"),
    SUCCESS(100000, "成功"),

    PARAM_NULL(110000, "参数不能为空");


    private final Integer errorCode;
    private final String errorMessage;

    ExceptionEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ExceptionEnum of(int errorCode) {
        for (ExceptionEnum bean : values()) {
            if (bean.key().intValue() == errorCode) {
                return bean;
            }
        }
        return null;
    }

    public String value() {
        return errorMessage;
    }

    public Integer key() {
        return errorCode;
    }

}