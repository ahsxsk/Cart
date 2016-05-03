package com.shike.service.impl;

import com.shike.common.IdGenerator;
import com.shike.dao.ICartDao;
import com.shike.model.Cart;
import com.shike.service.CartDbService;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartEditParam;
import com.shike.vo.CartQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.shike.service.ICartService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shike on 16/3/25.
 */
@Service("cartDbService")
public class CartDbServiceImpl implements CartDbService {
    private static Logger logger = Logger.getLogger(CartDbServiceImpl.class);
    @Resource(name = "cartDao")
    private ICartDao cartDao;

    /**
     * 查询购物车信息
     * @param cartQuery 购物车ID
     * @return 购物车信息
     * @throws Exception
     */
    public Cart getCart(CartQuery cartQuery) throws Exception {
        if (cartQuery == null) {
            logger.error("CartDbServiceImpl.getCart() | Error:cartQuery is null!");
            throw new NullPointerException("cartQuery is null!");
        }
        if (cartQuery.getCartId() == null) {
            logger.error("CartDbServiceImpl.getCart() | Error:cartId is null!");
            throw new IllegalArgumentException("cartId is null!");
        }
        return cartDao.selectCartByCartId(cartQuery);
    }

    /**
     * 获取购物车列表
     * @param cartQuery
     * @return 购物车列表
     * @throws Exception
     */
    public List<Cart> getAll(CartQuery cartQuery) throws Exception{
        if (cartQuery == null) {
            logger.error("CartDbServiceImpl.getAll() | Error:cartQuery is null!");
            throw new NullPointerException("cartQuery is null");
        }
        if (cartQuery.getUserId() == null) {
            logger.error("CartDbServiceImpl.getAll() | Error:userId is null!");
            throw new IllegalArgumentException("userId is null");
        }
        return cartDao.selectCartByUserId(cartQuery);
    }

    /**
     * 加车
     * @param cartAddParam 购物车信息
     * @return 是否加车成功 1:成功
     * @throws Exception
     */
    public Boolean addCart(CartAddParam cartAddParam) throws Exception {
        if (cartAddParam == null) {
            logger.error("CartDbServiceImpl.addCart() | Error:cartAddParam is null");
            throw new NullPointerException("cart is null");
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
            logger.error("CartDbServiceImpl.addCart() | Arguments Error. Arguments:" + "cartId:" + cartId + ",skuId:" + skuId
            + "userId:" + userId + ",status:" + status + ",amount:" + amount + ",shopId:" + shopId
                    + ",price:" + price + ",description:" + description);
            throw new IllegalArgumentException("参数异常");
        }
        if (description == null) { //redis空置无法操作, 统一置为null
            description = "null";
        }
        if (cartDao.insertCart(cartAddParam)) {
            return Boolean.TRUE;
        } else {
            logger.error("CartDbServiceImpl.addCart() | DbError:db error");
            return Boolean.FALSE;
        }
    }

    /**
     * 更新购物车商品数量, 根据sku
     * @param cartEditParam
     * @return
     * @throws Exception
     */
    public Boolean editSkuAmount(CartEditParam cartEditParam) throws Exception {
        if (cartEditParam == null) {
            logger.error("CartDbServiceImpl.editSkuAmount() | Error:cartEditParam is null");
            throw new NullPointerException("cartQuery is null");
        }
        String cartId = cartEditParam.getCartId();
        Integer amount = cartEditParam.getAmount();
        if (cartId == null || amount == null || amount < 0) {
            logger.error("CartDbServiceImpl.editSkuAmount() | Arguments Error. Arguments:userId:"  + ",amount:" + amount);
            throw new IllegalArgumentException("参数异常");
        }
        if (cartDao.updateCartByCartId(cartEditParam) >= 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
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
}
