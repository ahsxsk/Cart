import com.shike.model.Cart;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.shike.service.ICartService;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MLS on 16/4/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:applicationContext.xml"))
public class Test {
    @Resource(name = "cartService")
    private ICartService cartService = null;
//    @org.junit.Test
//    public void test() {
//        try {
//            String cartId = "1";
//            Cart ret = cartService.getCart(cartId);
//            System.out.println("Amount:" + ret.getAmount());
//        } catch (Exception e) {
//            System.out.println(e.getStackTrace());
//            System.out.println(e.getClass());
//            System.out.println(e.getMessage());
//        }
//    }
    @org.junit.Test
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

//    @org.junit.Test
//    public void testAddCart() {
//        try {
//            Cart cart = new Cart();
//            cart.setCartId("201603260002");
//            cart.setShopId("shop10001");
//            cart.setSkuId("sku0001");
//            cart.setAmount(1);
//            cart.setPrice(10000);
//            cart.setUserId("user00001");
//            cart.setStatus(0);
//            cart.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            cart.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//            cart.setDescription(new String("red"));
//            System.out.println(cartService.addCart(cart));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @org.junit.Test
//    public void testEditNum() {
//        try {
//            cartService.editSkuAmount("user00001", "sku0001", 53);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @org.junit.Test
//    public void testDeleteCart() {
//        try {
//            List<String> ids = new ArrayList<String>();
//            ids.add("1");
//            ids.add("2");
//            cartService.delectCart(ids);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
