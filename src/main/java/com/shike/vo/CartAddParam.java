package com.shike.vo;

import java.sql.Timestamp;

/**
 * cart库实体类
 * Created by shike on 16/3/24.
 */
public class CartAddParam {
    //购物车Id
    private String cartId;
    //店铺Id
    private String shopId;
    //商品skuId,商品唯一标识
    private String skuId;
    //商品数量
    private Integer amount;
    //商品价格
    private Integer price;
    //用户Id
    private String userId;
    //状态
    private Integer status;
    //保留待用
    private String description;

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuId() {
        return skuId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
