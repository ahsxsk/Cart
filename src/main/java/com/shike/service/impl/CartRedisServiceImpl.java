package com.shike.service.impl;

import com.shike.cart.redis.exception.RedisException;
import com.shike.cart.redis.service.RedisService;
import com.shike.cart.redis.service.impl.RedisServiceImpl;
import com.shike.common.TransUtils;
import com.shike.model.Cart;
import com.shike.service.CartRedisService;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartDelParam;
import com.shike.vo.CartEditParam;
import com.shike.vo.CartQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by MLS on 16/4/29.
 */
@Service("cartRedisService")
public class CartRedisServiceImpl implements CartRedisService{
    private static Logger logger = Logger.getLogger(CartRedisServiceImpl.class);
    /*userId-cartId有序集合键前缀*/
    private final String preUid = "userId:";
    /*cartId属性散列前缀*/
    private final String preCartId = "cartId:";
    /*过期时间, 7天*/
    private final int expireTime = 604800;
    /*每次只允许删除一个cartId*/
    private final int MaxSize = 1;
    @Resource(name = "redisService")
    private RedisServiceImpl redisService;

    /**
     * 查询购物车信息
     * @param cartQuery 购物车查询对象
     * @return Cart 购物车信息
     * @throws Exception
     */
    public Cart getCart(CartQuery cartQuery) throws Exception {
        if (cartQuery == null) {
            logger.error("CartRedisServiceImpl.getCart() | cartQuery is null");
            throw new IllegalArgumentException("cartQuery is null");
        }

        String cartId = cartQuery.getCartId(); //购物车Id
        if (cartId == null) {
            logger.error("CartRedisServiceImpl.getCart() | cartId is null");
            throw new IllegalArgumentException("cartId is null");
        }
        String key = preCartId + cartId;
        Map<String, String> redisMap = new HashMap<String, String>();
        redisMap = redisService.hgetAll(key);
        return TransUtils.transMap2Cart(redisMap);
    }

    /**
     * 获取购物车列表
     * @param cartQuery
     * @return 购物车列表
     * @throws Exception
     */
    public List<Cart> getAll(CartQuery cartQuery) throws Exception{
        if (cartQuery == null) {
            logger.error("CartDbServiceImpl.getAll() | error:cartQuery is null!");
            throw new NullPointerException("cartQuery is null");
        }
        String userId = cartQuery.getUserId();
        if (userId == null) {
            logger.error("CartDbServiceImpl.getAll() | error:userId is null!");
            throw new IllegalArgumentException("userId is null");
        }
        /*获取该用户的所有cartId*/
        String keyUid = preUid + userId.trim();
        Set<String> cartIds = redisService.zrange(keyUid, 0, -1);
        if (cartIds == null || cartIds.isEmpty()) { //Redis中没有数据, 返回的不是null
            return null;
        }
        List<Cart> carts = new LinkedList<Cart>();
        Iterator it = cartIds.iterator();
        CartQuery query = new CartQuery(); //根据cartId查询
        while (it.hasNext()) {
            query.setCartId(it.next().toString());
            Cart cartInfo = getCart(query);
            if (cartInfo != null) { //防止有userId列表有, 散列对应没有, 概率极小ms us级误差
                carts.add(cartInfo);
            }
        }

        return carts;
    }

    /**
     * 加车, 回写Redis
     * @param cartAddParam
     * @return 是否加车成功
     * @throws Exception
     */
    public Boolean addCart(CartAddParam cartAddParam) throws Exception {
        if (cartAddParam == null) {
            logger.error("CartRedisServiceImpl.addCart() | cartAddParam is null");
            throw new NullPointerException("cartAddParam is null");
        }
        /*参数检验, TODO:优化为统一代码*/
        String cartId = cartAddParam.getCartId();
        String skuId = cartAddParam.getSkuId();
        String userId = cartAddParam.getUserId();
        Integer status = cartAddParam.getStatus();
        Integer amount = cartAddParam.getAmount();
        String shopId = cartAddParam.getShopId();
        Integer price = cartAddParam.getPrice();
        String description = cartAddParam.getDescription();
        if (skuId == null || userId == null || status == null || status < 0
                || cartId == null || amount == null || shopId == null || price == null) {
            logger.error("CartDbServiceImpl.addCart() | Arguments:" + "cartId:" + cartId + ",skuId:" + skuId
                    + "userId:" + userId + ",status:" + status + ",amount:" + amount + ",shopId:" + shopId
                    + ",price:" + price + ",description:" + description);
            throw new IllegalArgumentException("参数异常");
        }
        Map<String, String> redisMap = TransUtils.transCartAddParam2Map(cartAddParam);
        if (redisMap == null) {
            logger.error("CartRedisServiceImpl.addCart() | transMap is null");
            throw new NullPointerException("transMap is null");
        }
        String keyUid = preUid + redisMap.get("userId"); //用户cartId列表key
        cartId = redisMap.get("cartId").trim();
        String keyCartId = preCartId + cartId;
        /*判断keyUid对应的member是否存在, 一般不需要判断,防止回灌Redis时ms, us级的误差*/
        try {
            if(redisService.zscore(keyUid, cartId) != null) {
                /*cartId列表中有此cartId, 只写散列表*/
                redisService.hmset(keyCartId, redisMap);
            } else {
                /*有序集合和散列表都需要写*/
                redisService.zadd(keyUid, Double.valueOf(cartId), cartId);
                redisService.hmset(keyCartId, redisMap);
            }
            /*设置超时*/
            redisService.expire(keyUid, expireTime);
            redisService.expire(keyCartId, expireTime);
        } catch (Exception ex) {
            logger.error("CartRedisServiceImpl.addCart() | Exception: " + ex.getMessage());
            throw new RedisException(ex);
        }
        return Boolean.TRUE;
    }

    /**
     * 编辑Cart商品数量, 根据cartId
     * @param cartEditParam
     * @return
     * @throws Exception
     */
    public Boolean editSkuAmount(CartEditParam cartEditParam) throws Exception {
        if (cartEditParam == null) {
            logger.error("CartRedisServiceImpl.editSkuAmount() | Error:cartEditParam is null");
            throw new NullPointerException("cartQuery is null");
        }
        String cartId = cartEditParam.getCartId();
        Integer amount = cartEditParam.getAmount();
        if (cartId == null || amount == null || amount < 0) {
            logger.error("CartRedisServiceImpl.editSkuAmount() | Arguments Error. Arguments:cartId:" + cartId
                    + ",amount:" + amount);
            throw new IllegalArgumentException("参数异常");
        }
        String keyCartId = preCartId + cartId;
        Map<String, String> redisMap = new HashMap<String, String>();
        redisMap.put("amount", amount.toString());
        if (redisService.hmset(keyCartId, redisMap).equals("OK")) { //修改成功
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    /**
     * 删除购物车
     * @param carts 购物车列表
     * @return 是否删除成功
     * @throws Exception
     */
    public Boolean delectCart(List<CartDelParam> carts) throws Exception {
        if (carts == null) {
            logger.error("CartRedisServiceImpl.delectCart() | Error:CartDelParam is null");
            throw new NullPointerException("CartDelParam is null");
        }
        Iterator it = carts.iterator();
        String cartId = null;
        String userId = null;
        CartDelParam cartDelParam = null;
        String keyUserId = null;
        String keyCartId = null;
        /*散列中需要删除的属性*/
        String [] fields = {"cartId", "skuId", "description", "shopId", "status", "price", "userId", "amount"};
        try {
            while (it.hasNext()) {
                cartDelParam = (CartDelParam) it.next();
                if ((cartId = cartDelParam.getCartId()) != null
                        && (userId = cartDelParam.getUserId()) != null) {
                    keyUserId = preUid + userId;
                    keyCartId = preCartId + cartId;
                    String [] cartIds = {cartId};
                    redisService.zrem(keyUserId, cartIds);
                    redisService.hdel(keyCartId, fields);
                    return Boolean.TRUE;
                } else {
                    logger.error("CartRedisServiceImpl.delectCart() | Arguments:cartId:" + cartId + ",userId" + userId);
                    return Boolean.FALSE;
                }
            }
        } catch (RedisException rex) {
            logger.error("CartRedisServiceImpl.delectCart() | Exception:" + rex.getMessage());
            throw new Exception(rex);
        } catch (Exception ex) {
            logger.error("CartRedisServiceImpl.delectCart() | Exception:" + ex.getMessage());
            throw ex;
        }
        return Boolean.FALSE;
    }
}
