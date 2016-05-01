package com.shike.common;

import com.shike.model.Cart;
import com.shike.vo.CartAddParam;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shike on 16/5/1.
 * 类型转换工具类
 */
public final class TransUtils {
    /**
     * 根据从Redis获取的map,设置Cart对象
     * @param map redis中获取的map
     * @return cartInfo 购物车实体类对象
     */
    public static Cart transMap2Cart(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Cart cartInfo = new Cart();
        String buf = null;
        if ((buf = map.get("cartId")) != null) {
            cartInfo.setCartId(buf); //cartId
        }
        if ((buf = map.get("shopId")) != null) {
            cartInfo.setShopId(buf); //shopId
        }
        if ((buf = map.get("userId")) != null) {
            cartInfo.setUserId(buf); //userId
        }
        if ((buf = map.get("Id")) != null) {
            cartInfo.setId(Long.getLong(buf)); //Id,基本不使用
        }
        if ((buf = map.get("status")) != null) {
            cartInfo.setStatus(Integer.valueOf(buf)); //status
        }
        if ((buf = map.get("amount")) != null) {
            cartInfo.setAmount(Integer.valueOf(buf)); //amount
        }
        if ((buf = map.get("skuId")) != null) {
            cartInfo.setSkuId(buf); //skuId
        }
        if ((buf = map.get("price")) != null) {
            cartInfo.setPrice(Integer.valueOf(buf)); //price
        }
        if ((buf = map.get("description")) != null) {
            cartInfo.setDescription(buf); //description
        }
        if ((buf = map.get("createTime")) != null) {
            cartInfo.setCreateTime(Timestamp.valueOf(buf)); //createTime
        }
        if ((buf = map.get("updateTime")) != null) {
            cartInfo.setUpdateTime(Timestamp.valueOf(buf)); //updateTime
        }
        return cartInfo;
    }

    /**
     * 将加车对象转换为Map对象
     * @param cartAddParam
     * @return Map
     */
    public static Map<String,String> transCartAddParam2Map(CartAddParam cartAddParam) {
        if (cartAddParam == null) {
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        try {
            result.put("cartId", cartAddParam.getCartId());
            result.put("skuId", cartAddParam.getSkuId());
            result.put("userId", cartAddParam.getUserId());
            result.put("price", cartAddParam.getPrice().toString());
            result.put("amount", cartAddParam.getAmount().toString());
            result.put("shopId", cartAddParam.getShopId());
            result.put("status", cartAddParam.getStatus().toString());
            if (cartAddParam.getDescription() == null)
            {
                result.put("description", "null"); //防止description为空
            } else {
                result.put("description", cartAddParam.getDescription());
            }

        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    /**
     * 将Cart对象转换为CartAddParam对象, 回写Redis时使用
     * @param cart
     * @return cartAddParam
     */
    public static CartAddParam transCart2CartAddParam(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartAddParam cartAddParam = new CartAddParam();
        cartAddParam.setCartId(cart.getCartId());
        cartAddParam.setDescription(cart.getDescription());
        cartAddParam.setShopId(cart.getShopId());
        cartAddParam.setSkuId(cart.getSkuId());
        cartAddParam.setStatus(cart.getStatus());
        cartAddParam.setPrice(cart.getPrice());
        cartAddParam.setAmount(cart.getAmount());
        cartAddParam.setUserId(cart.getUserId());
        return  cartAddParam;
    }
}
