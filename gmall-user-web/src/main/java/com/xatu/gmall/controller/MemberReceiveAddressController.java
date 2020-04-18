package com.xatu.gmall.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员收货地址表 前端控制器
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-15
 */
@RestController
@RequestMapping("/user/memberReceiveAddress")
public class MemberReceiveAddressController {
    @Reference
    MemberReceiveAddressService memberReceiveAddressService;


}

