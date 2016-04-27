package main.java.common;

/**
 * Created by MLS on 16/3/29.
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
