package com.shike.service;

import com.shike.model.Cart;
import com.shike.vo.CartQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DB购物车服务基本操作
 * Created by shike on 16/3/25.
 */
@Service
public interface CartDbService {
    /**
     * 根据购物车ID获取购物车信息
     * @param cartQuery
     * @return Cart 购物车信息
     */
    public Cart getCart(CartQuery cartQuery) throws Exception;

    /**
     * 查询多条Cart信息,即购物车列表
     * @param userId
     * @param status
     * @return
     */
    public List<Cart> getAll(String userId, Integer status) throws Exception;

    /**
     * 加车
     * @param cart
     * @return
     */
    public int addCart(Cart cart) throws Exception;

    /**
     * 编辑Cart商品数量
     * @param userId
     * @param skuId
     * @param amount
     * @return
     * @throws Exception
     */
    public int editSkuAmount(String userId, String skuId, Integer amount) throws Exception;

    /**
     * 删除购物车,支持批量删除
     * @param cartIds
     * @return
     * @throws Exception
     */
    public int delectCart(List<String> cartIds) throws Exception;


}

