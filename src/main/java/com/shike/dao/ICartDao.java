package com.shike.dao;

import com.shike.model.Cart;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartDelParam;
import com.shike.vo.CartEditParam;
import com.shike.vo.CartQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shike on 16/3/25.
 */
@Repository(value="cartDao")
public interface ICartDao {
    /**
     * 查询单条Cart信息
     * @param cartQuery
     * @return
     */
    public Cart selectCartByCartId(CartQuery cartQuery);

    /**
     * 查询多条Cart信息
     * @param cartQuery
     * @return
     */
    public List<Cart> selectCartByUserId(CartQuery cartQuery);

    /**
     * 插入一条Cart信息
     * @param cartAddParam
     * @return
     */
    public Boolean insertCart(CartAddParam cartAddParam);

    /**
     * 更新Cart信息
     * @param cartEditParam
     * @return
     */
    public int updateCartByCartId(CartEditParam cartEditParam);

    /**
     * 删除购物车
     * @param carts
     * @return
     */
    public int deleteCartByCartId(List<CartDelParam> carts);
}