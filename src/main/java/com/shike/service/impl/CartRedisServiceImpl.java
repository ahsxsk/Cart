package com.shike.service.impl;

import com.shike.cart.redis.exception.RedisException;
import com.shike.cart.redis.service.RedisService;
import com.shike.cart.redis.service.impl.RedisServiceImpl;
import com.shike.model.Cart;
import com.shike.service.CartRedisService;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MLS on 16/4/29.
 */
@Service("cartRedisService")
public class CartRedisServiceImpl implements CartRedisService{
    private static Logger logger = Logger.getLogger(CartRedisServiceImpl.class);
    /*userId-cartId有序集合键前缀*/
    private final String preUid = "uid:";
    /*cartId属性散列前缀*/
    private final String preCartId = "cartId:";
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
        return transMap2Cart(redisMap);
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
        Map<String, String> redisMap = transCartAddParam2Map(cartAddParam);
        if (redisMap == null) {
            logger.error("CartRedisServiceImpl.addCart() | transMap is null");
            throw new NullPointerException("transMap is null");
        }
        String keyUid = preUid + redisMap.get("userId"); //用户cartId列表key
        String cartId = redisMap.get("cartId").trim();
        String keyCartId = preCartId + cartId;
        /*判断keyUid对应的member是否存在, 一般不需要判断,防止回灌Redis时ms, us级的误差*/
        try {
            if(redisService.zscore(keyUid.trim(), cartId) != null) {
                redisService.hmset(keyCartId.trim(), redisMap); /*cartId列表中有此cartId, 只写散列表*/
            } else {
            /*有序集合和散列表都需要写*/
                redisService.zadd(keyUid.trim(), Double.valueOf(cartId), cartId);
                redisService.hmset(keyCartId.trim(), redisMap);
            }
        } catch (Exception ex) {
            logger.error("CartRedisServiceImpl.addCart() | Exception: " + ex.getMessage());
            throw new RedisException(ex);
        }

        return Boolean.TRUE;
    }
    /**
     * 根据从Redis获取的map,设置Cart对象
     * @param map redis中获取的map
     * @return cartInfo 购物车实体类对象
     */
    private Cart transMap2Cart(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Cart cartInfo = new Cart();
        String buf = null;
        if ((buf = map.get("cartId")) != null) {
            cartInfo.setCartId(buf); //cartId
        }
        if ((buf = map.get("shopId")) != null) {
            cartInfo.setShopId(buf); //shopId
        }
        if ((buf = map.get("userId")) != null) {
            cartInfo.setUserId(buf); //userId
        }
        if ((buf = map.get("Id")) != null) {
            cartInfo.setId(Long.getLong(buf)); //Id,基本不使用
        }
        if ((buf = map.get("status")) != null) {
            cartInfo.setStatus(Integer.getInteger(buf)); //status
        }
        if ((buf = map.get("amount")) != null) {
            cartInfo.setAmount(Integer.getInteger(buf)); //amount
        }
        if ((buf = map.get("skuId")) != null) {
            cartInfo.setSkuId(buf); //skuId
        }
        if ((buf = map.get("price")) != null) {
            cartInfo.setPrice(Integer.valueOf(buf)); //price
        }
        if ((buf = map.get("description")) != null) {
            cartInfo.setDescription(buf); //description
        }
        if ((buf = map.get("createTime")) != null) {
            cartInfo.setCreateTime(Timestamp.valueOf(buf)); //createTime
        }
        if ((buf = map.get("updateTime")) != null) {
            cartInfo.setUpdateTime(Timestamp.valueOf(buf)); //updateTime
        }
        return cartInfo;
    }

    /**
     * 将加车对象转换为Map对象
     * @param cartAddParam
     * @return Map
     */
    private Map<String,String> transCartAddParam2Map(CartAddParam cartAddParam) {
        if (cartAddParam == null) {
            logger.error("CartRedisServiceImpl.transCartAddParam2Map() | cartAddParam is null");
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        try {
            result.put("cartId", cartAddParam.getCartId());
            result.put("skuId", cartAddParam.getSkuId());
            result.put("userId", cartAddParam.getUserId());
            result.put("price", cartAddParam.getPrice().toString());
            result.put("amount", cartAddParam.getAmount().toString());
            result.put("shopId", cartAddParam.getShopId());
            result.put("status", cartAddParam.getStatus().toString());
            if (cartAddParam.getDescription() == null)
            {
                result.put("description", "null"); //防止description为空
            } else {
                result.put("description", cartAddParam.getDescription());
            }

        } catch (Exception ex) {
            logger.error("CartRedisServiceImpl.transCartAddParam2Map() | Exception:" + ex.getMessage()
                    + " | cartAddParam" + cartAddParam);
            return null;
        }
        return result;
    }
}
