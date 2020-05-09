package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.annotations.LoginRequired;
import com.xatu.gmall.entity.MemberReceiveAddress;
import com.xatu.gmall.entity.OmsCartItem;
import com.xatu.gmall.entity.OmsOrder;
import com.xatu.gmall.entity.OmsOrderItem;
import com.xatu.gmall.service.CartService;
import com.xatu.gmall.service.OrderService;
import com.xatu.gmall.service.SkuService;
import com.xatu.gmall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {


    @Reference
    CartService cartService;
    @Reference
    UserService userService;
    @Reference
    OrderService orderService;
    @Reference
    SkuService skuService;




    @LoginRequired(loginSuccess = true)
    @RequestMapping("/toTrade")
    public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap){

        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        //收件人地址列表
        List<MemberReceiveAddress> receiveAddressesByMemberId = userService.getReceiveAddressByMemberId(memberId);
        modelMap.put("userAddressList",receiveAddressesByMemberId);

        //将购物车集合转换为页面计算 清单集合
        List<OmsCartItem> omsCartItems = cartService.carList(memberId);

        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        for (OmsCartItem omsCartItem : omsCartItems) {
            if(omsCartItem.getIsChecked().equals("1")){
                //每循环一个购物车对象，封装一个商品的详情到OmsOrderItem
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPrice(omsCartItem.getPrice());
                omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);
            }
        }

        modelMap.put("omsOrderItems",omsOrderItems);
        BigDecimal totalAmount = getTotalAmount(omsOrderItems);
        modelMap.put("totalAmount",totalAmount.toString());


        //生成交易码，为了在提交订单时做交易码的校验
        String tradeCode = orderService.genTradeCode(memberId);
        modelMap.put("tradeCode",tradeCode);
        return "trade";
    }

    @RequestMapping("/submitOrder")
    @LoginRequired(loginSuccess = true)
    public String submitOrder(String tradeCode,String receiveAddressId, BigDecimal totalAmount, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap){
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
        //检查交易码
        String success = orderService.checkTradeCode(memberId,tradeCode);
        if(success.equals("success")){
            //订单项对象
            List<OmsOrderItem> omsOrderItems =new ArrayList<>();
            //订单对象
            OmsOrder omsOrder =new OmsOrder();
            omsOrder.setAutoConfirmDay(7);
            String outTradeNo = "gmall";
            outTradeNo = outTradeNo + System.currentTimeMillis();//将毫秒时间戳拼接到外部订单号
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMDDHHmmss");
            outTradeNo = outTradeNo + simpleDateFormat.format(new Date());//将时间字符串拼接到外部订单号
            omsOrder.setOrderSn(outTradeNo); //设置外部订单号
            omsOrder.setPayAmount(totalAmount);
            omsOrder.setOrderType(1);
            MemberReceiveAddress receiveAddressByReceiveAddressId = userService.getReceiveAddressByReceiveAddressId(receiveAddressId);
            omsOrder.setReceiverDetailAddress(receiveAddressByReceiveAddressId.getDetailAddress());
            omsOrder.setReceiverPhone(receiveAddressByReceiveAddressId.getPhoneNumber());
            omsOrder.setReceiverPostCode(receiveAddressByReceiveAddressId.getPostCode());
            omsOrder.setReceiverProvince(receiveAddressByReceiveAddressId.getProvince());
            omsOrder.setReceiverRegion(receiveAddressByReceiveAddressId.getRegion());
            //当前日期加一天
            Calendar calendar =Calendar.getInstance();
            calendar.add(Calendar.DATE,1);
            Date date = calendar.getTime();
            omsOrder.setReceiveTime(date);
            omsOrder.setSourceType(0);
            omsOrder.setStatus(0);
            omsOrder.setTotalAmount(totalAmount);



            //根据用户id获得要购买的商品列表（购物车），和总价格
            List<OmsCartItem> omsCartItems = cartService.carList(memberId);
            for (OmsCartItem omsCartItem : omsCartItems) {
                if(omsCartItem.getIsChecked().equals("1")){
                    //获得订单详情列表
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    //验价
                    BigDecimal price = omsCartItem.getPrice();
                    boolean b =skuService.checkPrice(omsCartItem.getProductSkuId(),price);
                    if(b==false){
                        return "tradeFail";
                    }
                    //验库存
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setProductName(omsCartItem.getProductName());


                    omsOrderItem.setOrderSn(outTradeNo); //外部订单号，用来和其他系统进行交互
                    omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    omsOrderItem.setRealAmount(new BigDecimal(omsCartItem.getTotalPrice()));
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    omsOrderItem.setProductSkuCode("11111111111");
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setProductSn("仓库对应的商品编号");//在仓库中对应的skuId

                    omsOrderItems.add(omsOrderItem);
                }
            }
            omsOrder.setOmsOrderItems(omsOrderItems);
            //将订单和订单详情写入数据库
            //删除购物车的对应商品
            orderService.saveOrder(omsOrder);
            //重定向到支付系统




        }

        return "tradeFail";
    }

    private BigDecimal getTotalAmount(List<OmsOrderItem> omsOrderItems){
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem omsOrdertItem : omsOrderItems) {
            BigDecimal quantity = new BigDecimal(omsOrdertItem.getProductQuantity());
            BigDecimal price = new BigDecimal(String.valueOf(omsOrdertItem.getProductPrice()));
            BigDecimal totalPrice =quantity.multiply(price);
            totalAmount = totalAmount.add(totalPrice);
        }
        return totalAmount;
    }

}
