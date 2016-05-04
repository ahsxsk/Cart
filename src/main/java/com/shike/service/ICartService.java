package com.shike.service;

import com.shike.model.Cart;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartDelParam;
import com.shike.vo.CartEditParam;
import com.shike.vo.CartQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车服务基本操作
 * Created by shike on 16/3/25.
 */
@Service
public interface ICartService {
    /**
     * 根据购物车ID获取购物车信息
     * @param cartQuery
     * @return 购物车信息
     */
    public Cart getCart(CartQuery cartQuery) throws Exception;

    /**
     * 查询多条Cart信息,即购物车列表
     * @param cartQuery
     * @return
     */
    public List<Cart> getAll(CartQuery cartQuery) throws Exception;

    /**
     * 加车
     * @param cartAddParam
     * @return
     */
    public Boolean addCart(CartAddParam cartAddParam) throws Exception;

    /**
     * 编辑Cart商品数量
     * @param cartEditParam
     * @return
     * @throws Exception
     */
    public Boolean editSkuAmount(CartEditParam cartEditParam) throws Exception;

    /**
     * 删除购物车,支持批量删除
     * @param carts
     * @return
     * @throws Exception
     */
    public Boolean delectCart(List<CartDelParam> carts) throws Exception;


}
