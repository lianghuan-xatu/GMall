package com.xatu.gmall.service;

import com.xatu.gmall.entity.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付信息表 服务类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-05-10
 */
public interface PaymentService extends IService<PaymentInfo> {

    void savePaymentInfo(PaymentInfo paymentInfo);

    void updatePaymentInfo(PaymentInfo paymentInfo);

    void sendDelayPaymentCheckQueue(String outTradeNo,int count);

    Map<String,String> checkAlipayPayment(String out_trade_no);
}
