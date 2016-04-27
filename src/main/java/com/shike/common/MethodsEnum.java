package com.shike.common;

/**
 * Created by shike on 16/4/27.
 */
public enum MethodsEnum {
    GET(0, "获取购物车"),
    GETALL(1, "购物车列表"),
    EDIT(2, "编辑购物车数量"),
    ADD(3, "加车"),
    DELETE(4, "删车");

    private Integer code;
    private String desc;
    MethodsEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
