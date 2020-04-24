package com.xatu.gmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xatu.gmall.entity.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SPU属性代理
 */
@Mapper
public interface SaleAttrMapper extends BaseMapper<PmsProductSaleAttr> {

    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("productId") String productId, @Param("skuId") Long skuId);


}
