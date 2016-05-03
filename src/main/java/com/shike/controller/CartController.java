package com.shike.controller;

import com.shike.common.ExceptionEnum;
import com.shike.common.HttpUtils;
import com.shike.common.JsonUtils;
import com.shike.model.Cart;
import com.shike.service.CartRedisService;
import com.shike.service.impl.CartRedisServiceImpl;
import com.shike.vo.CartAddParam;
import com.shike.vo.CartEditParam;
import com.shike.vo.CartQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shike.service.GetReturn;
import com.shike.service.ICartService;
import com.shike.service.impl.CartServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.shike.common.ValidateUtils;
import com.shike.common.MethodsEnum;
import com.shike.common.IdGenerator;
import com.alibaba.fastjson.*;

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
            if (!ValidateUtils.validateController(paramMap, MethodsEnum.GET)) {
                getReturn.setRet(false,
                        ExceptionEnum.PARAM_NULL.key().toString(),
                        ExceptionEnum.PARAM_NULL.value()
                );
            } else { //判断类型
                CartQuery cartQuery = JSON.parseObject(paramMap.get("cartQuery"), CartQuery.class);
                cartInfo = cartService.getCart(cartQuery);
            }
            resultStr = constructResult(cartInfo, getReturn);
            logger.info("CartController.getCart() | result:" + resultStr);
            response.getOutputStream().write(resultStr.getBytes());
        } catch (Exception e) {
            logger.error("CartController.getCart() | Exception:" + e.getMessage());
        }
    }

    /**
     * 获取购物车列表
     * @param request
     * @param response
     */
    @RequestMapping(value = "/list")
    public void getAll(HttpServletRequest request, HttpServletResponse response) {
        //购物车列表信息
        List<Cart> carts = new ArrayList<Cart>();
        //除购物车信息外的其他返回信息
        GetReturn getReturn = new GetReturn();
        //response信息
        String resultStr = "";
        try {
            //获取请求的IP,记录日志
            String remoteIp = HttpUtils.getRemoteIp(request);
            logger.info("CartController.getAll() | remoteIp=" + remoteIp);
            //获取前端请求参数并校验
            Map<String, String> paramMap = HttpUtils.getParameterMap(request);
            logger.info("CartController.getAll() | paramMap=" + paramMap);
            //提前设置返回
            getReturn.setRet(true,
                    ExceptionEnum.SUCCESS.key().toString(),
                    ExceptionEnum.SUCCESS.value()
            );
            //验证参数
            if (!ValidateUtils.validateController(paramMap,MethodsEnum.GETALL)) {
                getReturn.setRet(false,
                        ExceptionEnum.PARAM_NULL.key().toString(),
                        ExceptionEnum.PARAM_NULL.value()
                );
            } else {
                CartQuery cartQuery = JSON.parseObject(paramMap.get("cartQuery"), CartQuery.class);
                carts = cartService.getAll(cartQuery);
            }
            resultStr = constructResult(carts,getReturn);
            logger.info("CartController.getCart() | result:" + resultStr);
            response.getOutputStream().write(resultStr.getBytes());
        } catch (Exception e) {
            logger.error("CartController.getCart() | Exception:" + e.getMessage());
        }
    }

    /**
     * 更新商品数量
     * @param request
     * @param response
     */
    @RequestMapping(value = "/edit")
    public void editSkuAmount(HttpServletRequest request, HttpServletResponse response) {
        //除购物车信息外的其他返回信息
        GetReturn getReturn = new GetReturn();
        //response信息
        String resultStr = "";
        Boolean effect = Boolean.FALSE;
        try {
            //获取请求的IP,记录日志
            String remoteIp = HttpUtils.getRemoteIp(request);
            logger.info("CartController.ditSkuAmount() | remoteIp=" + remoteIp);
            //获取前端请求参数并校验
            Map<String, String> paramMap = HttpUtils.getParameterMap(request);
            logger.info("CartController.editSkuAmount() | paramMap=" + paramMap);
            //提前设置返回
            getReturn.setRet(true,
                    ExceptionEnum.SUCCESS.key().toString(),
                    ExceptionEnum.SUCCESS.value()
            );
            //验证参数
            if (!ValidateUtils.validateController(paramMap,MethodsEnum.EDIT)) {
                getReturn.setRet(false,
                        ExceptionEnum.PARAM_NULL.key().toString(),
                        ExceptionEnum.PARAM_NULL.value()
                );
            } else {
                CartEditParam cartEditParam = JSON.parseObject(paramMap.get("cartEditParam"), CartEditParam.class);
                effect = cartService.editSkuAmount(cartEditParam);
            }
            resultStr = constructResult(effect, getReturn);
            logger.info("CartController.editSkuAmount | result:" + resultStr);
            System.out.println(resultStr);
            response.getOutputStream().write(resultStr.getBytes());
        } catch (Exception e) {
            logger.error("CartController.editSkuAmount | Exception:" + e.getMessage());
        }
    }

    /**
     * 加车
     * @param request
     * @param response
     */
    @RequestMapping(value = "/add")
    public void addCart(HttpServletRequest request, HttpServletResponse response) {
        //购物车信息
        Cart cart = new Cart();
        //除购物车信息外的其他返回信息
        GetReturn getReturn = new GetReturn();
        //response信息
        String resultStr = "";
        Boolean effect = Boolean.FALSE;
        try {
            //获取请求的IP,记录日志
            String remoteIp = HttpUtils.getRemoteIp(request);
            logger.info("CartController.addCart() | remoteIp=" + remoteIp);
            //获取前端请求参数并校验
            Map<String, String> paramMap = HttpUtils.getParameterMap(request);
            logger.info("CartController.addCart | paramMap=" + paramMap);
            //提前设置返回
            getReturn.setRet(true,
                    ExceptionEnum.SUCCESS.key().toString(),
                    ExceptionEnum.SUCCESS.value()
            );
            //验证参数
            if (!ValidateUtils.validateController(paramMap,MethodsEnum.ADD)) {
                getReturn.setRet(false,
                        ExceptionEnum.PARAM_NULL.key().toString(),
                        ExceptionEnum.PARAM_NULL.value()
                );
            } else {
                CartAddParam cartAddParam = JSON.parseObject(paramMap.get("cartAddParam"), CartAddParam.class);
                effect = cartService.addCart(cartAddParam);
            }
            resultStr = constructResult(effect, getReturn);
            logger.info("CartController.addCart() | result:" + resultStr);
            System.out.println(resultStr);
            response.getOutputStream().write(resultStr.getBytes());
        } catch (Exception e) {
            logger.error("CartController.addCart() | Exception:" + e.getMessage());
        }
    }

    /**
     * 删除购物车, 逻辑删除,状态置为-1
     * @param request
     * @param response
     */
    @RequestMapping(value = "/delete")
    public void deleteCart(HttpServletRequest request, HttpServletResponse response) {
        //除购物车信息外的其他返回信息
        GetReturn getReturn = new GetReturn();
        //response信息
        String resultStr = "";
        Integer effect = 0;
        try {
            //获取请求的IP,记录日志
            String remoteIp = HttpUtils.getRemoteIp(request);
            logger.info("CartController.deleteCart() | remoteIp=" + remoteIp);
            //获取前端请求参数并校验
            Map<String, String> paramMap = HttpUtils.getParameterMap(request);
            logger.info("CartController.deleteCart() | paramMap=" + paramMap);
            //提前设置返回
            getReturn.setRet(true,
                    ExceptionEnum.SUCCESS.key().toString(),
                    ExceptionEnum.SUCCESS.value()
            );
            //验证参数
            if (!ValidateUtils.validateController(paramMap,MethodsEnum.DELETE)) {
                getReturn.setRet(false,
                        ExceptionEnum.PARAM_NULL.key().toString(),
                        ExceptionEnum.PARAM_NULL.value()
                );
            } else {
                List<String> cartIds = JSON.parseArray(paramMap.get("cartIds"), String.class);
                effect = cartService.delectCart(cartIds);
            }
            resultStr = constructResult(effect, getReturn);
            logger.info("CartController.deleteCart() | result:" + resultStr);
            System.out.println(resultStr);
            response.getOutputStream().write(resultStr.getBytes());
        } catch (Exception e) {
            logger.error("CartController.deleteCart() | Exception:" + e.getMessage());
        }
    }
    /**
     * 组装返回结果
     * @param data
     * @param getReturn
     * @param <T>
     * @return
     */
    private <T> String constructResult(T data, GetReturn getReturn) {
        String resultStr = "";
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            retMap.put("retCode", getReturn.getErrorCode());
            retMap.put("retDesc", getReturn.getErrorMessage());
            retMap.put("retData", data);
            resultStr = JsonUtils.toJson(retMap);
        } catch (IOException e) {
            logger.error("CartController.constructResult | Exception:" + e.getMessage());
        }
        return resultStr;
    }

}