package main.java.service;

import main.java.model.Cart;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车服务基本操作
 * Created by MLS on 16/3/25.
 */
public interface ICartService {
    /**
     * 根据购物车ID获取购物车信息
     * @param id 
     * @return
     */
    public Cart getCart(Long id) throws Exception;

    /**
     * 查询多条Cart信息,即购物车列表
     * @param userId
     * @param status
     * @return
     */
    public List<Cart> getAll(Integer userId, Integer status) throws Exception;

    /**
     * 加车
     * @param cart
     * @return
     */
    public int addCart(Cart cart) throws Exception;

    /**
     * 编辑Cart商品数量
     * @param id
     * @param amount
     * @return
     * @throws Exception
     */
    public int editSkuAmount(Long id, Integer amount) throws Exception;

    /**
     * 删除购物车,支持批量删除
     * @param ids
     * @return
     * @throws Exception
     */
    public int delectCart(List<Long> ids) throws Exception;


}
