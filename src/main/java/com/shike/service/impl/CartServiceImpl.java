package com.shike.service.impl;

import com.alibaba.fastjson.JSON;
import com.shike.cart.redis.exception.RedisException;
import com.shike.common.IdGenerator;
import com.shike.common.TransUtils;
import com.shike.dao.ICartDao;
import com.shike.model.Cart;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.shike.service.ICartService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shike on 16/3/25.
 */
@Service("cartService")
public class CartServiceImpl implements ICartService {
    private static Logger logger = Logger.getLogger(CartServiceImpl.class);
    @Resource(name = "cartDao")
    private ICartDao cartDao;

    @Resource(name = "cartRedisService")
    private CartRedisServiceImpl cartRedisService;
    @Resource(name = "cartDbService")
    private CartDbServiceImpl cartDbService;
    /**
     * 查询购物车信息
     * @param cartQuery
     * @return 购物车信息
     * @throws Exception
     */
    public Cart getCart(CartQuery cartQuery) throws Exception {
        if (cartQuery == null) {
            logger.error("CartServiceImpl.getCart() | error:cartQuery is null!");
            throw new NullPointerException("cartQuery is null!");
        }
        Cart cartInfo = cartRedisService.getCart(cartQuery); //先查Redis
        if (cartInfo == null) { //Redis没有,查DB,然后回灌Redis
            cartInfo = cartDbService.getCart(cartQuery);
            if (cartInfo != null) { //回写Redis
                List<Cart> carts = new LinkedList<Cart>();
                carts.add(cartInfo);
                writeRedisWithDb(carts);
            }
        }
        return cartInfo;
    }

    /**
     * 获取购物车列表
     * @param cartQuery
     * @return 购物车列表
     * @throws Exception
     */
    public List<Cart> getAll(CartQuery cartQuery) throws Exception{
        if (cartQuery == null) {
            logger.error("CartServiceImpl.getAll() | error:cartQuery is null!");
            throw new NullPointerException("cartQuery is null");
        }
        String userId = cartQuery.getUserId();
        if (userId == null) {
            logger.error("CartServiceImpl.getAll() | error:userId is null!");
            throw new NullPointerException("userId is null");
        }
        List<Cart> carts = cartRedisService.getAll(cartQuery); //先查Redis, 查不到时返回空的List
        if (carts == null || carts.isEmpty()) { //Redis没查到, 查DB,回写Redis
            carts = cartDbService.getAll(cartQuery);

            if (carts != null) { //回写Redis
                writeRedisWithDb(carts);
            }
        }
        return carts;
    }

    /**
     * 加车
     * @param cart 购物车信息
     * @return 是否加车成功 1:成功
     * @throws Exception
     */
    public int addCart(Cart cart) throws Exception {
        if (cart == null) {
            throw new NullPointerException("cart is null");
        }
        String skuId = cart.getSkuId();
        String userId = cart.getUserId();
        Integer status = cart.getStatus();
        if (skuId == null || userId == null || status < 0) {
            throw new IllegalArgumentException("参数异常");
        }

//        try {
//            List<Cart> carts = getAll(userId, status); //获取用户购物车商品列表
//            int len = carts.size();                    //如果购物车该sku存在,则修改数量,否则加车
//            if (len != 0) {
//                while (len-- > 0) {
//                    if (carts.get(len).getSkuId().equals(skuId)) {
//                        Integer amount = carts.get(len).getAmount() + 1;
//                        return editSkuAmount(userId, skuId, amount);
//                    }
//                }
//            }
//            IdGenerator idGenerator = IdGenerator.getIdGenerator();
//            try {
//                String cartId = idGenerator.getId(userId).toString();//购物车Id
//                cart.setCartId(cartId);
//            } catch (Exception e) {
//                //TODO
//                throw e;
//            }
//            return cartDao.insertCart(cart);
//        } catch (Exception e) {
//            throw new Exception("TODO: CartException");
//        }
        return 1;
    }

    /**
     * 更新购物车商品数量
     * @param userId
     * @param skuId
     * @param amount
     * @return
     * @throws Exception
     */
    public int editSkuAmount(String userId, String skuId, Integer amount) throws Exception {
        if (userId == null) {
            throw new NullPointerException("userId is null");
        }
        if (skuId == null) {
            throw new NullPointerException("skuId is null");
        }
        if (amount == null) {
            throw new NullPointerException("amount is null");
        }

        try {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setSkuId(skuId);
            cart.setAmount(amount);
            return cartDao.updateCartBySkuId(cart);
        } catch (Exception e) {
            throw new Exception("TODO: CartException");
        }
    }

    /**
     * 删除购物车
     * @param cartIds 购物车ID列表
     * @return 是否删除成功
     * @throws Exception
     */
    public int delectCart(List<String> cartIds) throws Exception {
        if (cartIds == null) {
            throw new NullPointerException("ids is null");
        }
        List<Cart> carts = new ArrayList<Cart>();
        int len = cartIds.size();
        while (len-- > 0) {
            Cart cart = new Cart();
            cart.setCartId(cartIds.get(len));
            carts.add(cart);
        }

        try {
            return cartDao.deleteCartByCartId(carts);
        } catch (Exception e) {
            throw new Exception("TODO:");
        }
    }


    /**
     * 使用DB数据回写Redis
     * @param carts
     * @return
     */
    private Boolean writeRedisWithDb(List<Cart> carts) {
        if (carts == null || carts.isEmpty()) {
            return Boolean.FALSE;
        }
        Iterator it = carts.iterator();
        try {
            while (it.hasNext()) {
                CartAddParam cartAddParam = TransUtils.transCart2CartAddParam((Cart) it.next());
                cartRedisService.addCart(cartAddParam);
            }
        } catch (Exception ex) {
            logger.error("CartServiceImpl.writeRedisWithDb() | Exception:" + ex.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}