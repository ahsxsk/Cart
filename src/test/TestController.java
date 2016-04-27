package test;

import com.alibaba.fastjson.JSON;
import main.java.controller.CartController;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shike on 16/3/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:main/resources/applicationContext.xml"))
public class TestController {
    private static final Logger logger = Logger.getLogger(TestController.class);

    @Resource
    private CartController cartController;
    @org.junit.Test
    public void testQuery() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        try {
            request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING,true);
            request.setMethod("POST");
            request.setParameter("cartId", "6");
            cartController.getCart(request,response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testList() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        try {
            request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING,true);
            request.setMethod("POST");
            request.setParameter("userId", "user00001");
            request.setParameter("status", "0");
            cartController.getAll(request,response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testEdit() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        try {
            request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING,true);
            request.setMethod("POST");
            request.setParameter("userId", "user00001");
            request.setParameter("amount", "67");
            request.setParameter("skuId", "sku0001");
            cartController.editSkuAmount(request,response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testAdd() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        try {
            request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING,true);
            request.setMethod("POST");
            request.setParameter("userId", "user00001");
            request.setParameter("amount", "1");
            request.setParameter("skuId", "sku0001");
            request.setParameter("price", "1000");
            request.setParameter("cartId", "2016032900001");
            request.setParameter("shopId", "shop0002");
            request.setParameter("status", "0");
            cartController.addCart(request,response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testDel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        try {
            request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING,true);
            request.setMethod("POST");
            List<String> cartIds = new ArrayList<String>();
            cartIds.add("3");
            cartIds.add("4");
            cartIds.add("5");
            String str = JSON.toJSONString(cartIds);
            request.setParameter("cartIds", str);

            cartController.deleteCart(request,response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
