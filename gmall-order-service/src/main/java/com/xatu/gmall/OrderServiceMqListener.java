package com.xatu.gmall;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.service.OrderService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

@Component
public class OrderServiceMqListener
{
    @Reference
    OrderService orderService;

    @JmsListener(destination = "PAYMENT_SUCCESS_QUENE",containerFactory = "jmsQueneListener")
    public void consumePaymentResult(MapMessage mapMessage) throws JMSException {
        //更新订单业务状态
        String out_trade_no = mapMessage.getString("out_trade_no");
        //更新订单状态
        orderService.updateOrderStatus(out_trade_no);



    }
}
