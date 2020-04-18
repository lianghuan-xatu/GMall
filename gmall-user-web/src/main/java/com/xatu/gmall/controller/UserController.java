package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.Member;
import com.xatu.gmall.entity.MemberReceiveAddress;
import com.xatu.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-15
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;

    /**
     * 查询所有会员信息
     * @return
     */
    @RequestMapping("/selectAllUser")
    @ResponseBody
    public String selectAllUser(){
        List<Member> members=userService.list();
        System.out.println(members);
        return Arrays.toString(members.toArray());
    }

    /**
     * 根据用户id查出收货地址
     * @return
     */
    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<MemberReceiveAddress> getMemberReceiveAddressByMemberId(String memberId){
        List<MemberReceiveAddress> memberReceiveAddresses=userService.getReceiveAddressByMemberId(memberId);
        System.out.println(Arrays.toString(memberReceiveAddresses.toArray()));
        return memberReceiveAddresses;
    }

}

