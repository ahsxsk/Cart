package com.shike.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartDelParam;
import com.shike.vo.CartEditParam;
import com.shike.vo.CartQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * 验证参数工具
 * Created by shike on 16/3/29.
 */
public final class ValidateUtils {
    /**
     * 验证Controller层方法的参数
     * @param param
     * @param method
     * @return
     */
    public static Boolean validateController(Map<String,String> param, MethodsEnum method) {
        if (param == null || method == null) {
            return Boolean.FALSE;
        }

        switch (method) {
            case ADD:
                return validateAdd(param);
            case GETALL:
                return validateGetAll(param);
            case DELETE:
                return validateDelete(param);
            case GET:
                return validateGetCart(param);
            case EDIT:
                return validateEdit(param);
            default:
                return Boolean.FALSE;
        }
    }

    /**
     * 验证查询方法getCart的参数
     * @param param
     * @return Boolean
     */
    private static Boolean validateGetCart(Map<String,String> param) {
        if (param == null || param.size() == 0 || param.get("cartQuery") == null) {
            return Boolean.FALSE;
        }
        CartQuery cartQuery = JSON.parseObject(param.get("cartQuery"), CartQuery.class);
        if(cartQuery.getCartId() == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 验证查询列表getAll的参数
     * @param param
     * @return Boolean
     */
    private static Boolean validateGetAll(Map<String, String> param) {
        if (param == null || param.size() == 0 || param.get("cartQuery") == null) {
            return Boolean.FALSE;
        }
        CartQuery cartQuery = JSON.parseObject(param.get("cartQuery"), CartQuery.class);
        if(cartQuery.getUserId() == null || cartQuery.getStatus() == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 验证编辑方法editSkuAmount的参数
     * @param param
     * @return
     */
    private static Boolean validateEdit(Map<String, String> param) {
        if (param == null || param.size() == 0 || param.get("cartEditParam") == null) {
            return Boolean.FALSE;
        }
        CartEditParam cartEditParam = JSON.parseObject(param.get("cartEditParam"), CartEditParam.class);
        if(cartEditParam.getCartId() == null || cartEditParam.getAmount() == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 验证加车addCart的参数
     * @param param
     * @return
     */
    private static Boolean validateAdd(Map<String, String> param) {
        if (param == null || param.size() == 0 || param.get("cartAddParam") == null) {
            return Boolean.FALSE;
        }
        CartAddParam cartAddParam = JSON.parseObject(param.get("cartAddParam"), CartAddParam.class);
        String shopId = cartAddParam.getSkuId(); //skuId
        String userId = cartAddParam.getUserId(); //用户Id
        Integer price = cartAddParam.getPrice(); //商品价格
        Integer amount = cartAddParam.getAmount(); //商品数量
        String skuId = cartAddParam.getSkuId();
        Integer status = cartAddParam.getStatus();
        if (shopId == null || userId == null || price == null
                || amount == null || skuId == null || status == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 验证删车deleteCart方法的参数
     * @param param
     * @return
     */
    private static Boolean validateDelete(Map<String, String> param) {
        if (param == null || param.size() == 0) {
            return Boolean.FALSE;
        }
        String cartDelParams = param.get("cartDelParams");
        if (cartDelParams == null || cartDelParams.length() == 0) {
            return Boolean.FALSE;
        }
        List<CartDelParam> cartDelParamList = JSONArray.parseArray(cartDelParams, CartDelParam.class);
        Iterator it = cartDelParamList.iterator();
        CartDelParam cartDelParam = null;
        while (it.hasNext()) {
            cartDelParam = (CartDelParam) it.next();
            if (cartDelParam.getCartId() == null || cartDelParam.getUserId() == null) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
