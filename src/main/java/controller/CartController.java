package main.java.controller;

import main.java.common.ExceptionEnum;
import main.java.common.HttpUtils;
import main.java.common.JsonUtil;
import main.java.model.Cart;
import main.java.service.GetReturn;
import main.java.service.ICartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cart对外服务接口
 * Created by shike on 16/3/26.
 */
@Controller
@RequestMapping(value = "/cart")
public class CartController extends AbstractCtl {
    private static Logger logger = Logger.getLogger(CartController.class);
    @Resource
    private ICartService cartService;

    /**
     * 查询购物车
     * @param request
     * @param response
     */
    @RequestMapping(value = "/query")
    public void getCart(HttpServletRequest request, HttpServletResponse response) {
        //购物车信息
        Cart cartInfo = new Cart();
        //除购物车信息外的其他返回信息
        GetReturn getReturn = new GetReturn();
        //response信息
        String resultStr = "";
        try {
            //获取请求的IP,记录日志
            String remoteIp = HttpUtils.getRemoteIp(request);
            logger.info("CartController.getCart() | remoteIp=" + remoteIp);
            //获取前端请求参数并校验
            Map<String, String> paramMap = HttpUtils.getParameterMap(request);
            logger.info("CartController.getCart() | paramMap=" + paramMap);
            //提前设置返回
            getReturn.setRet(true,
                    ExceptionEnum.SUCCESS.key().toString(),
                    ExceptionEnum.SUCCESS.value()
            );
            //验证参数
            if (paramMap == null || paramMap.size() == 0 || paramMap.get("cartId") == null) {
                getReturn.setRet(false,
                        ExceptionEnum.PARAM_NULL.key().toString(),
                        ExceptionEnum.PARAM_NULL.value()
                );
            } else if (paramMap.get("cartId") instanceof String) { //判断类型
                cartInfo = cartService.getCart(paramMap.get("cartId"));
            } else {
                getReturn.setRet(false,
                        ExceptionEnum.WRONGTYPE.key().toString(),
                        ExceptionEnum.WRONGTYPE.value()
                );
            }
            resultStr = constructResult(cartInfo,getReturn);
            logger.info("CartController.getCart() | result:" + resultStr);
            System.out.println(resultStr);
            response.getOutputStream().write(resultStr.getBytes());
        } catch (Exception e) {
            logger.error("CartController.getCart() | Exception:" + e.getMessage());
        }
    }

    private <T> String constructResult(T data, GetReturn getReturn) {
        String resultStr = "";
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            retMap.put("retCode", getReturn.getErrorCode());
            retMap.put("retDesc", getReturn.getErrorMessage());
            retMap.put("retData", data);
            resultStr = JsonUtil.toJson(retMap);
        } catch (IOException e) {
            logger.error("CartController.constructResult | Exception:" + e.getMessage());
        }
        return resultStr;
    }
}
