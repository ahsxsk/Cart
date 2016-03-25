package main.java.model;

import org.springframework.stereotype.Repository;

/**
 * cart库实体类
 * Created by MLS on 16/3/24.
 */
public class Cart {
    //购物车信息id
    private Long id;
    //用户Id
    private Integer userId;
    //商品skuId,商品唯一标识
    private Long skuId;
    //商品数量
    private Integer amount;
    //店铺Id
    private Integer shopId;
    //创建时间
    private Integer ctime;
    //状态
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
