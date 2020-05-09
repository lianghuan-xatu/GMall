package com.xatu.gmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xatu.gmall.entity.OmsOrder;

import java.math.BigDecimal;

public interface OrderService {
    String genTradeCode(String memberId);

    String checkTradeCode(String memberId,String tradeCode);


    void saveOrder(OmsOrder omsOrder);
}
