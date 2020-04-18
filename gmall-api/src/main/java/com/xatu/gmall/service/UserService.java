package com.xatu.gmall.service;

import com.xatu.gmall.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xatu.gmall.entity.MemberReceiveAddress;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-15
 */
@Service
public interface UserService extends IService<Member> {

    List<Member> selectUserById(Integer i);

    List<MemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
