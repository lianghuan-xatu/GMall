package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xatu.gmall.config.ActiveMQConfig;
import com.xatu.gmall.entity.PaymentInfo;
import com.xatu.gmall.mapper.PaymentMapper;
import com.xatu.gmall.service.PaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xatu.gmall.util.ActiveMQUtil;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付信息表 服务实现类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-05-10
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, PaymentInfo> implements PaymentService {



    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    ActiveMQUtil activeMQUtil;
    @Autowired
    AlipayClient alipayClient;


    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentMapper.insert(paymentInfo);
    }

    @Override
    public void updatePaymentInfo(PaymentInfo paymentInfo)  {


        //进行幂等性检查
        PaymentInfo paymentInfoResult = paymentMapper.selectOne(new QueryWrapper<PaymentInfo>().eq("alipay_trade_no",paymentInfo.getOrderSn()));
        if(StringUtils.isNotBlank(paymentInfoResult.getPaymentStatus()) &&paymentInfoResult.getPaymentStatus().equals("已支付")){
            return;
        }else{


            Connection connection = null;
            Session session = null;


            try{
                connection = activeMQUtil.getConnectionFactory().createConnection();
                session = connection.createSession(true,Session.SESSION_TRANSACTED);
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                paymentMapper.update(paymentInfo,new UpdateWrapper<PaymentInfo>().eq("order_sn",paymentInfo.getOrderSn()));
                //支付成功后，引起的系统服务，订单服务的更新   --》库存服务  --》物流服务
                //调用mq发送支付成功的消息
                Queue payment_success_quene = session.createQueue("PAYMENT_SUCCESS_QUENE");
                MessageProducer producer = session.createProducer(payment_success_quene);
                TextMessage textMessage = new ActiveMQTextMessage();//字符串文本
                MapMessage mapMessage = new ActiveMQMapMessage();//hash结构
                mapMessage.setString("out_trade_no",paymentInfo.getOrderSn());
                session.commit();
            }catch (Exception e){
                //消息回滚
                try{
                    session.rollback();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }finally {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }




        }

    }

    @Override
    public void sendDelayPaymentCheckQueue(String outTradeNo,int count) {

        Connection connection = null;
        Session session = null;


        try{
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true,Session.SESSION_TRANSACTED);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{

            Queue delaypayment_check_quene = session.createQueue("DELAYPAYMENT_CHECK_QUENE");
            MessageProducer producer = session.createProducer(delaypayment_check_quene);
            TextMessage textMessage = new ActiveMQTextMessage();//字符串文本
            MapMessage mapMessage = new ActiveMQMapMessage();//hash结构
            mapMessage.setString("out_trade_no",outTradeNo);
            mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*30);
            session.commit();
        }catch (Exception e){
            //消息回滚
            try{
                session.rollback();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        }

    @Override
    public Map<String, String> checkAlipayPayment(String out_trade_no) {

        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("out_trade_no",out_trade_no);
        alipayTradeQueryRequest.setBizContent(JSON.toJSONString(requestMap));
        AlipayTradeQueryResponse alipayTradeQueryResponse = null;
        Map<String,String> resultMap = new HashMap<>();
        try {
            alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(alipayTradeQueryResponse.isSuccess()){
            //调用成功
            resultMap.put("out_trade_no",alipayTradeQueryResponse.getOutTradeNo());
            resultMap.put("trade_no",alipayTradeQueryResponse.getTradeNo());
            resultMap.put("trade_status",alipayTradeQueryResponse.getTradeStatus());
            resultMap.put("call_back_content",alipayTradeQueryResponse.getMsg());
        }else{
            //调用失败  可能交易未创建

        }

        return resultMap;
    }
}

