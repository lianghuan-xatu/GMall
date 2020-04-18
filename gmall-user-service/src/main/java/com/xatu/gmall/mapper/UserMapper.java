package com.xatu.gmall.mapper;

import com.xatu.gmall.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-15
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<Member> {

    List<Member> SelectUserById(Integer i);
}
