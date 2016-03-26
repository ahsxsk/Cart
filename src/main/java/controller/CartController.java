package main.java.controller;

import main.java.service.ICartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Cart对外服务接口
 * Created by shike on 16/3/26.
 */
@Controller
@RequestMapping(value = "/cart")
public class CartController extends AbstractCtl {
    @Resource
    private ICartService cartService;

    /**
     * 查询购物车
     * @param request
     * @param response
     */
    @RequestMapping(value = "/query")
    public void getCart(HttpServletRequest request, HttpServletResponse response) {

    }
}
