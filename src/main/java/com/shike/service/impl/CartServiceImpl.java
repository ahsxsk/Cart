package com.shike.service.impl;

import com.alibaba.fastjson.JSON;
import com.shike.cart.redis.exception.RedisException;
import com.shike.common.IdGenerator;
import com.shike.common.TransUtils;
import com.shike.dao.ICartDao;
import com.shike.model.Cart;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartDelParam;
import com.shike.vo.CartEditParam;
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
        Cart cartInfo = null;
        try {
            cartInfo = cartRedisService.getCart(cartQuery); //先查Redis
            if (cartInfo == null) { //Redis没有,查DB,然后回灌Redis
                cartInfo = cartDbService.getCart(cartQuery);
                if (cartInfo != null) { //回写Redis
                    List<Cart> carts = new LinkedList<Cart>();
                    carts.add(cartInfo);
                    writeRedisWithDb(carts);
                }
            }
        } catch (RedisException rex) {
            logger.error("CartServiceImpl.getCart() | Exception:" + rex.getMessage());
            throw new Exception("Redis Exception:" + rex.getMessage());
        } catch (Exception ex) {
            logger.error("CartServiceImpl.getCart() | Exception:" + ex.getMessage());
            throw new Exception("Redis Exception:" + ex.getMessage());
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
        List<Cart> carts = null;
        try {
            carts = cartRedisService.getAll(cartQuery); //先查Redis, 查不到时返回空的List
            if (carts == null || carts.isEmpty()) { //Redis没查到, 查DB,回写Redis
                carts = cartDbService.getAll(cartQuery);
                if (carts != null) { //回写Redis
                    writeRedisWithDb(carts);
                }
            }
        } catch (RedisException rex) {
            logger.error("CartServiceImpl.addCart() | Exception:" + rex.getMessage());
            throw new Exception(rex.getMessage());
        } catch (Exception ex) {
            logger.error("CartServiceImpl.addCart() | Exception:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }

        return carts;
    }

    /**
     * 加车
     * @param cartAddParam 购物车信息
     * @return 是否加车成功 1:成功
     * @throws Exception
     */
    public Boolean addCart(CartAddParam cartAddParam) throws Exception {

        if (cartAddParam == null) {
            logger.error("CartServiceImpl.addCart() | error:cartAddParam is null!");
            throw new NullPointerException("cartAddParam is null");
        }
        /*参数检验, TODO:优化为统一代码*/
        String skuId = cartAddParam.getSkuId();
        String userId = cartAddParam.getUserId();
        Integer status = cartAddParam.getStatus();
        Integer amount = cartAddParam.getAmount();
        String shopId = cartAddParam.getShopId();
        Integer price = cartAddParam.getPrice();
        String description = cartAddParam.getDescription();
        if (skuId == null || userId == null || status == null || status < 0
                || amount == null || shopId == null || price == null) {
            logger.error("CartServiceImpl.addCart() | Arguments:" + ",skuId:" + skuId
                    + "userId:" + userId + ",status:" + status + ",amount:" + amount + ",shopId:" + shopId
                    + ",price:" + price + ",description:" + description);
            throw new IllegalArgumentException("参数异常");
        }

        try {
            /*获取用户购物车商品列表*/
            CartQuery cartQuery = new CartQuery();
            cartQuery.setUserId(userId);
            cartQuery.setStatus(status);
            List<Cart> carts = getAll(cartQuery);
            int len = carts.size();
            //如果购物车该sku存在,则修改数量,否则加车
            if (len != 0) {
                while (len-- > 0) {
                    if (carts.get(len).getSkuId().equals(skuId)) {
                        CartEditParam cartEditParam = new CartEditParam();
                        cartEditParam.setCartId(carts.get(len).getCartId());
                        Integer nAmount = carts.get(len).getAmount() + 1;
                        cartEditParam.setAmount(nAmount);
                        if (cartRedisService.editSkuAmount(cartEditParam)
                                && true/*TODO:发MQ*/) {
                            return Boolean.TRUE;
                        } else {
                            return Boolean.FALSE;
                        }
                    }
                }
            }
            IdGenerator idGenerator = IdGenerator.getIdGenerator();

            String cartId = idGenerator.getId(userId).toString();//购物车Id
            cartAddParam.setCartId(cartId);
            if (cartAddParam.getDescription() == null) {
                cartAddParam.setDescription("null");
            }
            if (cartRedisService.addCart(cartAddParam)
                    && true/*TODO:发MQ*/) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (RedisException rex) {
            logger.error("CartServiceImpl.addCart() | Exception:" + rex.getMessage());
            throw new Exception(rex.getMessage());
        } catch (Exception ex) {
            logger.error("CartServiceImpl.addCart() | Exception:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 更新购物车商品数量
     * @param cartEditParam
     * @return
     * @throws Exception
     */
    public Boolean editSkuAmount(CartEditParam cartEditParam) throws Exception {
        if (cartEditParam == null) {
            logger.error("CartServiceImpl.editSkuAmount() | error:cartEditParam is null!");
            throw new NullPointerException("cartAddParam is null");
        }

        if (cartEditParam.getCartId() == null || cartEditParam.getAmount() == null) {
            logger.error("CartServiceImpl.editSkuAmount() | error:cartId is null!");
            throw new IllegalArgumentException("cartId is null");
        }

        if (cartEditParam.getCartId() == null || cartEditParam.getAmount() == null) {
            logger.error("CartServiceImpl.editSkuAmount() | error:amount is null!");
            throw new IllegalArgumentException("cartId is null");
        }
        try {
            if (cartRedisService.editSkuAmount(cartEditParam)
                    && true/*TODO:发MQ*/) {
                return Boolean.TRUE;
            }
        } catch (RedisException rex) {
            logger.error("CartServiceImpl.editSkuAmount() | Exception:" + rex.getMessage());
            throw new Exception(rex);
        } catch (Exception ex) {
            logger.error("CartServiceImpl.editSkuAmount() | Exception:" + ex.getMessage());
            throw new Exception(ex);
        }
        return Boolean.FALSE;
    }

    /**
     * 删除购物车
     * @param carts 购物车ID列表
     * @return 是否删除成功
     * @throws Exception
     */
    public Boolean delectCart(List<CartDelParam> carts) throws Exception {
        if (carts == null) {
            logger.error("CartServiceImpl.delectCart() | Error: carts is null");
            throw new NullPointerException("carts is null");
        }
        try {
            if (cartRedisService.delectCart(carts)
                    && true/*TODO:发MQ*/) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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