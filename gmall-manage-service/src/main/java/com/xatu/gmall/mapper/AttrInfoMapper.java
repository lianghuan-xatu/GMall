package com.xatu.gmall.mapper;

import com.xatu.gmall.entity.PmsBaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 平台属性表 Mapper 接口
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-19
 */
@Mapper
public interface AttrInfoMapper extends BaseMapper<PmsBaseAttrInfo> {

    List<PmsBaseAttrInfo> selectAttrValueListByValueId(String attrValueIdStr);
}
