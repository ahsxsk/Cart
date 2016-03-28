package test;

import main.java.controller.CartController;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;

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
}
