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
import java.util.List;

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
            Long id = new Long(1);
            Cart ret = cartService.getCart(id);
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
            Integer uid = new Integer(10001);
            Integer status = new Integer(0);
            List<Cart> ret = cartService.getAll(uid,status);
            int len = ret.size();
            while (len-- > 0) {
                System.out.println(ret.get(len).getSkuId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddCart() {
        try {
            Cart cart = new Cart();
            cart.setAmount(new Integer(3));
            cart.setUserId(new Integer(10002));
            cart.setShopId(new Integer(12324));
            cart.setStatus(new Integer(0));
            cart.setSkuId(new Long(10004));
            cart.setCtime(new Integer(123434533));
            System.out.println(cartService.addCart(cart));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditNum() {
        try {
            cartService.editSkuAmount(new Long(5), new Integer(100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteCart() {
        try {
            List<Long> ids = new ArrayList<Long>();
            ids.add(new Long(6));
            ids.add(new Long(7));
            ids.add(new Long(5));
            cartService.delectCart(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
