package com.shike.dao;

import com.shike.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shike on 16/3/25.
 */
@Repository(value="cartDao")
public interface ICartDao {
    /**
     * 查询单条Cart信息
     * @param cart
     * @return
     */
    public Cart selectCartByCartId(Cart cart);

    /**
     * 查询多条Cart信息
     * @param cart
     * @return
     */
    public List<Cart> selectCartByUserId(Cart cart);

    /**
     * 插入一条Cart信息
     * @param cart
     * @return
     */
    public int insertCart(Cart cart);

    /**
     * 更新Cart信息
     * @param cart
     * @return
     */
    public int updateCartBySkuId(Cart cart);

    /**
     * 删除购物车
     * @param carts
     * @return
     */
    public int deleteCartByCartId(List<Cart> carts);
}