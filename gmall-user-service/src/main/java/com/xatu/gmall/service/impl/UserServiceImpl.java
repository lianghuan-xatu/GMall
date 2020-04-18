package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xatu.gmall.entity.Member;
import com.xatu.gmall.entity.MemberReceiveAddress;
import com.xatu.gmall.mapper.MemberReceiveAddressMapper;
import com.xatu.gmall.mapper.UserMapper;
import com.xatu.gmall.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, Member> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MemberReceiveAddressMapper memberReceiveAddressMapper;


    public List<Member> selectUserById(Integer i) {
        return userMapper.SelectUserById(1);
    }


    public List<MemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        return (List<MemberReceiveAddress>) memberReceiveAddressMapper.selectList(new QueryWrapper<MemberReceiveAddress>().eq("member_Id",memberId));
    }
}
