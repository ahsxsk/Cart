package main.java.controller;

import main.java.common.ExceptionEnum;
import main.java.common.HttpUtils;
import main.java.common.JsonUtil;
import main.java.model.Cart;
import main.java.service.ICartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

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
    public void getCart(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Cart result = new Cart();
        String resultStr = "";
        try {
            //获取请求的IP,记录日志
            String remoteIp = HttpUtils.getRemoteIp(request);
            logger.info("CartController.getCart() | remoteIp=" + remoteIp);
            //获取前端请求参数并校验
            Map<String, String> paramMap = HttpUtils.getParameterMap(request);
            logger.info("CartController.getCart() | paramMap=" + paramMap);
            if (paramMap == null || paramMap.size() == 0) {
                throw new IllegalArgumentException("query param is null");
            }
            result = cartService.getCart(paramMap.get("cartId"));
            resultStr = JsonUtil.toJson(result);
            logger.info("CartController.getCart() | result:" + resultStr);
            response.getOutputStream().write(resultStr.getBytes());
        } catch (Exception e) {
            logger.error("CartController.getCart() | Exception:" + e.getMessage());
        }
    }
}
