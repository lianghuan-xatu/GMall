package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xatu.gmall.entity.OmsCartItem;
import com.xatu.gmall.mapper.CartMapper;
import com.xatu.gmall.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-30
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, OmsCartItem> implements CartService {



    @Autowired
    CartMapper cartMapper;



    @Override
    public OmsCartItem getCartExistByUser(String memberId, String skuId) {
        return cartMapper.selectOne(new QueryWrapper<OmsCartItem>().eq("memeber_Id",memberId).eq("sku_id",skuId));
    }

    @Override
    public void addCartIterm(OmsCartItem omsCartItem) {
        cartMapper.insert(omsCartItem);
    }

    @Override
    public void updateCarItem(OmsCartItem omsCartItemFromDB) {
        cartMapper.update(omsCartItemFromDB,new UpdateWrapper<OmsCartItem>().eq("id",omsCartItemFromDB.getId()));
    }

    @Override
    public void flushCache(String memberId) {
        List<OmsCartItem> omsCartItems = cartMapper.selectList(new QueryWrapper<OmsCartItem>().eq("memeber_id",memberId));
        //同步到redis缓存

        }
}
