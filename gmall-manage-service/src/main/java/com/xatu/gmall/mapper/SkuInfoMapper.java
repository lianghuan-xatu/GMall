package com.xatu.gmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xatu.gmall.entity.PmsSkuInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkuInfoMapper extends BaseMapper<PmsSkuInfo>
{
    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String spuId);

}
