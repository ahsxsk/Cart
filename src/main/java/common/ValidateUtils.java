package main.java.common;

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
        if (param == null || param.size() == 0 || param.get("cartId") == null) {
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
        if (param == null || param.size() == 0 || param.get("userId") == null || param.get("status") == null) {
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
        if (param == null || param.size() == 0) {
            return Boolean.FALSE;
        }
        String userId = param.get("userId");
        String amount = param.get("amount");
        String skuId = param.get("skuId");
        if (userId == null || amount == null || skuId == null) {
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
        if (param == null || param.size() == 0) {
            return Boolean.FALSE;
        }
        String cartId = param.get("cartId"); //购物车Id
        String shopId = param.get("skuId"); //skuId
        String userId = param.get("userId"); //用户Id
        String price = param.get("price"); //商品价格
        String amount = param.get("amount"); //商品数量
        String skuId = param.get("skuId");
        String status = param.get("status");
        if (cartId == null || shopId == null || userId == null || price == null
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
        String cartIds = param.get("cartIds");
        if (cartIds == null || cartIds.length() == 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
