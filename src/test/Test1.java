package test;

import main.java.dao.ICartDao;
import main.java.model.Cart;
import main.java.service.ICartService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by MLS on 16/3/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:main/resources/applicationContext.xml"))
public class Test1 {
    @Resource(name = "cartService")
    private ICartService cartService = null;
    @org.junit.Test
    public void test() {
        try {
            Cart cart = new Cart();
            cart.setId(1);
            Cart ret = cartService.getCartById(cart);
            System.out.println(ret.getAmount());
        } catch (Exception e) {}
    }
}
