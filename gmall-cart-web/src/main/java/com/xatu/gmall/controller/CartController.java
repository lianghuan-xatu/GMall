package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.xatu.gmall.entity.OmsCartItem;
import com.xatu.gmall.entity.PmsSkuInfo;
import com.xatu.gmall.service.CartService;
import com.xatu.gmall.service.SkuService;
import com.xatu.gmall.annotations.LoginRequired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xatu.gmall.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @LoginRequired(loginSuccess = true)
    @RequestMapping("/toTrade")
    public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session,ModelMap modelMap){
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
        return "toTrade";
    }

    @LoginRequired(loginSuccess = false)
    @RequestMapping("/addToCart")
    public String addToCart(String skuId, int quantity,HttpServletRequest request, HttpServletResponse response) {

        //调用商品服务查询商品信息
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(BigDecimal.valueOf(pmsSkuInfo.getPrice()));
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
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

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
                omsCartItem.setMemberId(Long.valueOf(memberId));
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

        @LoginRequired(loginSuccess = false)
        @RequestMapping("/cartList")
        public String cartList(String skuId, Integer quantity, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap){

        List<OmsCartItem> omsCartItems = new ArrayList<>();
            String userId = (String)request.getAttribute("memberId");
            String nickname = (String)request.getAttribute("nickname");
        if(StringUtils.isNotBlank(userId)){
            //已经登录  查询db
            omsCartItems = cartService.carList(userId);

        }else{
           //没有登陆 查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            for (OmsCartItem omsCartItem : omsCartItems) {
                omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(BigDecimal.valueOf(omsCartItem.getQuantity())).toString());
            }

        }
        modelMap.put("cartList",omsCartItems);
        return "cartList";
        }


        @LoginRequired(loginSuccess = false)
        @RequestMapping("/checkCart")
        public String checkCart(String isChecked,String skuId,HttpServletRequest request,HttpServletResponse response,HttpSession session,ModelMap modelMap){
            String memberId = (String)request.getAttribute("memberId");
            String nickname = (String)request.getAttribute("nickname");
            //调用服务，修改状态
            cartService.checkCart(skuId,memberId,isChecked);
            //将最新的数据从缓存总查出来，渲染给内嵌页
            List<OmsCartItem> omsCartItems = cartService.carList(memberId);
            modelMap.put("cartList",omsCartItems);
            BigDecimal totalAmount = getTotalAmount(omsCartItems);
            modelMap.put("totalAccount",totalAmount);
            return "cartListInner";
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

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems){
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = new BigDecimal(omsCartItem.getTotalPrice());
            totalAmount = totalAmount.add(totalPrice);
        }
        return totalAmount;
    }
}
