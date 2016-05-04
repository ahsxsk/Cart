package com.shike.vo;

/**
 * Created by shike on 16/5/4.
 */
public class CartDelParam {
    //购物车Id
    private String cartId;
    //用户Id
    private String userId;
    //状态
    private Integer status;

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
