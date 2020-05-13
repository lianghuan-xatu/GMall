package com.xatu.gmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.PaymentInfo;
import com.xatu.gmall.service.PaymentService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.Map;

@Component
public class PaymentServiceMqListener
{
    @Reference
    PaymentService paymentService;

    @JmsListener(destination = "DELAYPAYMENT_CHECK_QUENE",containerFactory = "jmsQueueListener")
    public void consumePaymentCheckResult(MapMessage mapMessage) throws JMSException {
        {
            String out_trade_no = mapMessage.getString("out_trade_no");
            int count = mapMessage.getInt("count");
            //调用paymentService的支付宝检查接口

            Map<String, String> resultMap = paymentService.checkAlipayPayment(out_trade_no);
            if (resultMap!=null && !resultMap.isEmpty()){
                String trade_status = (String)resultMap.get("trade_status");
                //根据查询的支付结果，判断是否进行下一次的延迟任务还是支付成功后更新数据和后续任务
                if(trade_status.equals("TRADE_SUCCESS")) {
                    //支付成功更新paymentinfo，更新支付发送支付队列


                    PaymentInfo paymentInfo = new PaymentInfo();
                    paymentInfo.setOrderSn(out_trade_no);
                    paymentInfo.setPaymentStatus("已支付");
                    paymentInfo.setAlipayTradeNo(resultMap.get("trade_no"));//支付宝的交易凭证号
                    paymentInfo.setCallbackContent(resultMap.get("call_back_content"));//回调请求字符串
                    paymentInfo.setCallbackTime(new Date());

                    System.out.println("支付成功，调用支付服务，修改支付信息和发送支付成功队列消息");
                    paymentService.updatePaymentInfo(paymentInfo);

                    return;
                }
            }
            if(count > 0){
                //继续发送延迟检查任务，计算延迟时间等
                count--;
                paymentService.sendDelayPaymentCheckQueue(out_trade_no,count);
            }else{

        }
        }
    }
}
