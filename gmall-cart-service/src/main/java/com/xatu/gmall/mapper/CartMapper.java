package com.xatu.gmall.mapper;

import com.xatu.gmall.entity.OmsCartItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-30
 */
@Mapper
public interface CartMapper extends BaseMapper<OmsCartItem> {

    void updateIsChecked(@Param("skuId") String skuId, @Param("memberId") String memberId, @Param("isChecked") String isChecked);
}
