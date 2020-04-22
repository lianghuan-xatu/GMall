package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xatu.gmall.entity.PmsSkuAttrValue;
import com.xatu.gmall.entity.PmsSkuImage;
import com.xatu.gmall.entity.PmsSkuInfo;
import com.xatu.gmall.entity.PmsSkuSaleAttrValue;
import com.xatu.gmall.mapper.SkuAttrValueMapper;
import com.xatu.gmall.mapper.SkuImageMapper;
import com.xatu.gmall.mapper.SkuInfoMapper;
import com.xatu.gmall.mapper.SkuSaleAttrValueMapper;
import com.xatu.gmall.service.SkuService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuInfoMapper, PmsSkuInfo> implements SkuService
{
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    SkuImageMapper skuImageMapper;

    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        //插入SkuInfo
        skuInfoMapper.insert(pmsSkuInfo);
        //插入平台属性关联
        List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for(PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValueList){
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            skuAttrValueMapper.insert(pmsSkuAttrValue);
        }
        //插入销售属性关联
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValueList){
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            skuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
        }
        //插入图片信息
        List<PmsSkuImage> pmsSkuImageList = pmsSkuInfo.getSkuImageList();
        for(PmsSkuImage pmsSkuImage : pmsSkuImageList){
            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            skuImageMapper.insert(pmsSkuImage);
        }

    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo = skuInfoMapper.selectById(skuId);
        return pmsSkuInfo;
    }
}
