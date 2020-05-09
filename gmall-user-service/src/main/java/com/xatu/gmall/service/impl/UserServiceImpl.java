package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xatu.gmall.entity.Member;
import com.xatu.gmall.entity.MemberReceiveAddress;
import com.xatu.gmall.mapper.MemberReceiveAddressMapper;
import com.xatu.gmall.mapper.UserMapper;
import com.xatu.gmall.mapper.UserReceiveAddressMapper;
import com.xatu.gmall.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xatu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;


import javax.swing.plaf.metal.MetalMenuBarUI;
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
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserReceiveAddressMapper userReceiveAddressMapper;

    public List<Member> selectUserById(Integer i) {
        return userMapper.SelectUserById(1);
    }


    public List<MemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        return (List<MemberReceiveAddress>) memberReceiveAddressMapper.selectList(new QueryWrapper<MemberReceiveAddress>().eq("member_Id",memberId));
    }

    @Override
    public Member login(Member loginMember) {
        Jedis jedis = null;
        Member memberFromCache = null;
        Member memberFromDB = null;
        try{
           jedis = redisUtil.getJedis();
           if (jedis!=null){
               String memberStr = jedis.get("user:" + loginMember.getUsername()+loginMember.getPassword()+ ":info");
               if(memberStr!=null){
                   //密码正确
                    memberFromCache = JSON.parseObject(memberStr, Member.class);
                   return memberFromCache;
               }else{
                   //密码错误
                   //缓存中没有开启数据库
                    memberFromDB = loginFromDB(loginMember);
                   if(memberFromDB!=null){
                       jedis.setex("user:" + memberFromDB.getUsername()+memberFromDB.getPassword()+ ":info",60*60*24,JSON.toJSONString(memberFromDB));
                   }
                   return memberFromDB;
               }
           }else{
               //缓存宕机失败
               //查询数据库（分布式锁）
                memberFromDB = loginFromDB(loginMember);
               if(memberFromDB!=null){
                   jedis.setex("user:" + memberFromDB.getUsername()+memberFromDB.getPassword()+ ":info",60*60*24,JSON.toJSONString(memberFromDB));
               }
               return memberFromDB;
           }
        }finally {
            jedis.close();
        }
    }

    @Override
    public void addToken(String token, Long memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+memberId+":token",60*60*2,token);
        jedis.close();
    }

    @Override
    public Member addOauthUser(Member member) {
        userMapper.insert(member);
        return userMapper.selectOne(new QueryWrapper<Member>().eq("id",member.getId()));
    }

    @Override
    public Member checkOauthUser(Member umsCheck) {
        return userMapper.selectOne(new QueryWrapper<Member>().eq("source_uid",umsCheck.getSourceUid()));
    }

    @Override
    public MemberReceiveAddress getReceiveAddressByReceiveAddressId(String receiveAddressId) {
        return userReceiveAddressMapper.selectById(receiveAddressId);
    }

    private Member loginFromDB(Member loginMember) {

        return userMapper.selectOne(new QueryWrapper<Member>().eq("username",loginMember.getUsername()).eq("password",loginMember.getPassword()));
    }
}
