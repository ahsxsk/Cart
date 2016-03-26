package main.java.service.impl;

import main.java.dao.ICartDao;
import main.java.model.Cart;
import main.java.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by MLS on 16/3/25.
 */
@Service("cartService")
public class CartServiceImpl implements ICartService {
    @Resource(name = "cartDao")
    private ICartDao cartDao;

    /**
     * 查询购物车信息
     * @param id 购物车ID
     * @return 购物车信息
     * @throws Exception
     */
    @Override
    public Cart getCart(Long id) throws Exception {
        if (id == null) {
            throw new NullPointerException("id is null!");
        }
        Cart cart = new Cart();
        cart.setId(id);
        return cartDao.selectCartById(cart);
    }

    /**
     * 获取购物车列表
     * @param userId 用户ID
     * @param status 购物车状态 0:正常 -1:删除
     * @return 购物车列表
     * @throws Exception
     */
    @Override
    public List<Cart> getAll(Integer userId, Integer status) throws Exception{
        if (userId == null) {
            throw new NullPointerException("userId is null");
        }

        if (status == null) {
            throw new NullPointerException("status is null");
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setStatus(status);
        return cartDao.selectCartByUserId(cart);
    }

    /**
     * 加车
     * @param cart 购物车信息
     * @return 是否加车成功 1:成功
     * @throws Exception
     */
    @Override
    public int addCart(Cart cart) throws Exception {
        if (cart == null) {
            throw new NullPointerException("cart is null");
        }

        if (cart.getUserId() == null || cart.getSkuId() == null || cart.getShopId() == null) {
            throw new IllegalArgumentException("参数异常");
        }

        try {
            return cartDao.insertCart(cart);
        } catch (Exception e) {
            throw new Exception("TODO: CartException");
        }
    }

    /**
     * 更新购物车商品数量
     * @param id 购物车ID
     * @param amount 商品数量
     * @return 是否更新成功
     * @throws Exception
     */
    @Override
    public int editSkuAmount(Long id, Integer amount) throws Exception {
        if (id == null) {
            throw new NullPointerException("id is null");
        }

        if (amount == null) {
            throw new NullPointerException("amount is null");
        }

        try {
            Cart cart = new Cart();
            cart.setId(id);
            cart.setAmount(amount);
            return cartDao.updateCartById(cart);
        } catch (Exception e) {
            throw new Exception("TODO: CartException");
        }
    }

    /**
     * 删除购物车
     * @param ids 购物车ID列表
     * @return 是否删除成功
     * @throws Exception
     */
    @Override
    public int delectCart(List<Long> ids) throws Exception {
        if (ids == null) {
            throw new NullPointerException("ids is null");
        }
        List<Cart> carts = new ArrayList<Cart>();
        int len = ids.size();
        while (len-- > 0) {
            Cart cart = new Cart();
            cart.setId(ids.get(len));
            carts.add(cart);
        }

        try {
            return cartDao.deleteCartById(carts);
        } catch (Exception e) {
            throw new Exception("TODO:");
        }
    }
}
