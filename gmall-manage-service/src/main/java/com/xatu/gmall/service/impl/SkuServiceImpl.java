package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xatu.gmall.entity.PmsSkuAttrValue;
import com.xatu.gmall.entity.PmsSkuImage;
import com.xatu.gmall.entity.PmsSkuInfo;
import com.xatu.gmall.entity.PmsSkuSaleAttrValue;
import com.xatu.gmall.mapper.SkuAttrValueMapper;
import com.xatu.gmall.mapper.SkuImageMapper;
import com.xatu.gmall.mapper.SkuInfoMapper;
import com.xatu.gmall.mapper.SkuSaleAttrValueMapper;
import com.xatu.gmall.service.SkuService;
import com.xatu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

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
        /*PmsSkuInfo pmsSkuInfo = skuInfoMapper.selectById(skuId);
        List<PmsSkuImage> skuImageList = skuImageMapper.selectList(new QueryWrapper<PmsSkuImage>().eq("sku_id", skuId));
        pmsSkuInfo.setSkuImageList(skuImageList);*/
        PmsSkuInfo pmsSkuInfo = null;
        //链接缓存
        Jedis jedis = RedisUtil.getJedis();
        //查询缓存
        String skuKey = "sku:"+skuId+":info";
        String skuJson = jedis.get(skuKey);
        try{
            if(StringUtils.isNotBlank(skuJson)){
                pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
            }else{
                //如果缓存没有，查询mysql
                pmsSkuInfo = skuInfoMapper.selectById(skuId);
                List<PmsSkuImage> skuImageList = skuImageMapper.selectList(new QueryWrapper<PmsSkuImage>().eq("sku_id", skuId));
                pmsSkuInfo.setSkuImageList(skuImageList);
                //mysql查询结果存入redis
                if(pmsSkuInfo!=null){
                    jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
                }else{
                    //数据库中不存在该sku
                    //为了防止缓存穿透，null或者空字符串设置给redis
                    jedis.setex("sku:"+skuId+":info",60*3, JSON.toJSONString(""));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {
        List<PmsSkuInfo> skuSaleAttrValueListBySpu = skuInfoMapper.selectSkuSaleAttrValueListBySpu(spuId);
        return skuSaleAttrValueListBySpu;
    }
}
