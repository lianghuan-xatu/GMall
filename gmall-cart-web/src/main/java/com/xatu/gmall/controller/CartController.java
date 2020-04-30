package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.xatu.gmall.entity.OmsCartItem;
import com.xatu.gmall.entity.PmsSkuInfo;
import com.xatu.gmall.service.CartService;
import com.xatu.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Reference
    CartService cartService;

    @Reference
    SkuService skuService;
    @RequestMapping("/addToCart")
    public String addToCart(String skuId, int quantity,HttpServletRequest request, HttpServletResponse response) {

        //调用商品服务查询商品信息
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(BigDecimal.valueOf(pmsSkuInfo.getPrice()).multiply(BigDecimal.valueOf(quantity)));
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(Long.valueOf(pmsSkuInfo.getSpuId()));
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("1111111111");
        omsCartItem.setProductSkuId(Long.valueOf(skuId));
        omsCartItem.setQuantity(quantity);

        //判断用户是否登录
        String memberId = "";//"1"
        if (StringUtils.isBlank(memberId)) {
            //用户未登录
            List<OmsCartItem> omsCartItems = new ArrayList<>();
            //判断用户本地是由有cookie信息
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (cartListCookie != null) {
                //cookie不为空
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                boolean exist = if_cart_exist(omsCartItems, omsCartItem);
                //判断当点添加的商品在我们的购物车是否存在
                if (exist) {
                    //之前添加过，更新操作
                    for (OmsCartItem cartItem : omsCartItems) {
                        String productSkuId = cartItem.getProductSkuId().toString();
                        if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity() + omsCartItem.getQuantity());
                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));
                        }
                    }
                }else{
                        //之前没有添加过
                        omsCartItems.add(omsCartItem);
                    }

                } else {
                    //cookie为空
                    omsCartItems.add(omsCartItem);
                }
                CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);

            } else {
            //用户已登录
            //在db中查询购物车数据
            OmsCartItem omsCartItemFromDB = cartService.getCartExistByUser(memberId,skuId);
            if(omsCartItemFromDB==null){
                //购物车中没有该商品
                omsCartItemFromDB.setMemberId(Long.valueOf(memberId));
                cartService.addCartIterm(omsCartItem);
            }else{
                //购物车中有该商品 直接更新数据库
                omsCartItemFromDB.setQuantity(omsCartItem.getQuantity());
                cartService.updateCarItem(omsCartItemFromDB);
            }
            //同步缓存
            cartService.flushCache(memberId);
            }

            return "redirect:/success.html";
        }

    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {
        boolean exist = false;
        for (OmsCartItem cartItem : omsCartItems) {
            String cartItemSkuId = cartItem.getProductSkuId().toString();
            String productSkuId = omsCartItem.getProductSkuId().toString();
            if(cartItemSkuId.equals(productSkuId)){
                exist=true;
            }
        }
        return exist;
    }
}
