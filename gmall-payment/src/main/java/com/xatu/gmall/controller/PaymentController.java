package com.xatu.gmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.xatu.gmall.annotations.LoginRequired;
import com.xatu.gmall.config.AlipayConfig;
import com.xatu.gmall.entity.OmsOrder;
import com.xatu.gmall.entity.PaymentInfo;
import com.xatu.gmall.entity.PmsSearchCrumb;
import com.xatu.gmall.service.OrderService;
import com.xatu.gmall.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ObjectStreamClass;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;
    @Reference
    PaymentService paymentService;
    @Reference
    OrderService orderService;


    @RequestMapping("/index")
    @LoginRequired(loginSuccess = true)
    public String index(String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, ModelMap modelMap){
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
        modelMap.put("outTradeNo",outTradeNo);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("nickname",nickname);
        return "index";
    }




    @RequestMapping("/alipay/submit")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipay(String outTradeNo,BigDecimal totalAmount,HttpServletRequest request,ModelMap modelMap){
        String form = null;
        AlipayTradePayRequest alipayTradePayRequest = new AlipayTradePayRequest();
        //回调函数地址
        alipayTradePayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayTradePayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);

        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",outTradeNo);
        map.put("product_code","FAST_INSTANT_TRADB_PAY");
        map.put("total_amount",totalAmount);
        map.put("subject","华为p40徕卡八摄影");

        String mapJSONStr = JSON.toJSONString(map);
        alipayTradePayRequest.setBizContent(mapJSONStr);


        //获得一个支付宝请求客户端（它并不是一个连接，而是一个封装号的http的表单请求）
        try {
            form = alipayClient.pageExecute(alipayTradePayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //生成并保存用户的支付信息
        OmsOrder omsOrder = orderService.getOrderByOutTradeNo(outTradeNo);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(omsOrder.getId().toString());
        paymentInfo.setPaymentStatus("未付款");
        paymentInfo.setSubject("GMALL商城商品一件");
        paymentInfo.setTotalAmount(totalAmount);
        paymentService.savePaymentInfo(paymentInfo);

        //向消息中间件发送一个检查支付状态（支付服务消费）的延迟消息队列
        paymentService.sendDelayPaymentCheckQueue(outTradeNo,5);



    return form;
    }


    @RequestMapping("alipay/callback/return")
    @LoginRequired(loginSuccess = true)
    public String aliPayCallBackReturn(HttpServletRequest request,ModelMap modelMap){
        //回到请求中获取支付宝的参数
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");  //支付宝交易号
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String totalAmount = request.getParameter("totalAmount");
        String subject = request.getParameter("subject");
        String call_back_content = request.getQueryString();//回跳内容

        //通过支付宝的paramMap进行签名验证，（2.0版本的接口将paramMap参数去掉了，导致同步请求没法验签）
        if(StringUtils.isNotBlank(sign)){
            //验签成功
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(out_trade_no);//支付宝交易凭证号
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setCallbackContent(call_back_content);//回调请求字符串
            paymentInfo.setCallbackTime(new Date());
            paymentService.updatePaymentInfo(paymentInfo);
            //支付成功后，引起的系统服务，订单服务的更新   --》库存服务  --》物流服务
            //更新用户的支付数据

        }

        return "finish";
    }

}
