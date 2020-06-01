package com.xatu.gmall.service;

import com.xatu.gmall.entity.OmsCartItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-30
 */
public interface CartService extends IService<OmsCartItem> {

    /**
     * 查询用户购物车中是否有该商品
     * @param memberId
     * @param skuId
     * @return
     */
    OmsCartItem getCartExistByUser(String memberId, String skuId);

    /**
     * 添加商品信息到购物车
     * @param omsCartItem
     */
    void addCartIterm(OmsCartItem omsCartItem);

    /**
     * 更新购物车中的该商品信息
     * @param omsCartItemFromDB
     */
    void updateCarItem(OmsCartItem omsCartItemFromDB);


    /**
     * 刷新缓存
     * @param memberId
     */
    void flushCache(String memberId);

    /**
     * 根据userId查询购物车
     * @param userId
     * @return
     */
    List<OmsCartItem> carList(String userId);

    void checkCart(String skuId,String memberId, String isChecked);
}
