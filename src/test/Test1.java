package test;

import main.java.dao.ICartDao;
import main.java.model.Cart;
import main.java.service.ICartService;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shike on 16/3/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:main/resources/applicationContext.xml"))
public class Test1 {
    @Resource(name = "cartService")
    private ICartService cartService = null;
    @org.junit.Test
    public void test() {
        try {
             String cartId = "1";
            Cart ret = cartService.getCart(cartId);
            System.out.println("Amount:" + ret.getAmount());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void testGetAll() {
        try {
            String uid = "user0001";
            Integer status = 0;
            List<Cart> ret = cartService.getAll(uid,status);
            int len = ret.size();
            while (len-- > 0) {
                System.out.println(ret.get(len).getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddCart() {
        try {
            Cart cart = new Cart();
            cart.setCartId("201603260002");
            cart.setShopId("shop10001");
            cart.setSkuId("sku0001");
            cart.setAmount(1);
            cart.setPrice(10000);
            cart.setUserId("user00001");
            cart.setStatus(0);
            cart.setCreateTime(new Timestamp(System.currentTimeMillis()));
            cart.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            cart.setDescription(new String("red"));
            System.out.println(cartService.addCart(cart));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditNum() {
        try {
            cartService.editSkuAmount("user00001", "sku0001", 53);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteCart() {
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("1");
            ids.add("2");
            cartService.delectCart(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
