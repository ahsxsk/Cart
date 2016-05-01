package com.shike.service.impl;

import com.shike.cart.redis.exception.RedisException;
import com.shike.cart.redis.service.RedisService;
import com.shike.cart.redis.service.impl.RedisServiceImpl;
import com.shike.common.TransUtils;
import com.shike.model.Cart;
import com.shike.service.CartRedisService;
import com.shike.vo.CartAddParam;
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
            logger.error("CartRedisServiceImpl.getCart() | cartQuery is null", new NullPointerException("cartQuery is null"));
        }

        String cartId = cartQuery.getCartId(); //购物车Id
        if (cartId == null) {
            logger.error("CartRedisServiceImpl.getCart() | cartId is null", new NullPointerException("cartId is null"));
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
            throw new NullPointerException("userId is null");
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
        Map<String, String> redisMap = TransUtils.transCartAddParam2Map(cartAddParam);
        if (redisMap == null) {
            logger.error("CartRedisServiceImpl.addCart() | transMap is null");
            throw new NullPointerException("transMap is null");
        }
        String keyUid = preUid + redisMap.get("userId"); //用户cartId列表key
        String cartId = redisMap.get("cartId").trim();
        String keyCartId = preCartId + cartId;
        /*判断keyUid对应的member是否存在, 一般不需要判断,防止回灌Redis时ms, us级的误差*/
        try {
            if(redisService.zscore(keyUid, cartId) != null) {
                redisService.hmset(keyCartId, redisMap); /*cartId列表中有此cartId, 只写散列表*/
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

}
