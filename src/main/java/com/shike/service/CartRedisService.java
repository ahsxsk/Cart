package com.shike.service;

import com.shike.model.Cart;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartQuery;

import java.util.List;

/**
 * 购物车Redis服务
 * Created by shike on 16/4/29.
 */
public interface CartRedisService {
    /*根据cartId获取购物车信息*/
    Cart getCart(CartQuery cartQuery) throws Exception;
    /*加车*/
    Boolean addCart(CartAddParam cartAddParam) throws Exception;
    /**
     * 查询多条Cart信息,即购物车列表
     * @param cartQuery
     * @return
     */
    public List<Cart> getAll(CartQuery cartQuery) throws Exception;
}
