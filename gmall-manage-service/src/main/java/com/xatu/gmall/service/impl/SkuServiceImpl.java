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
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    Redisson redisson;

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

                //设置分布式锁
                // String OK = jedis.set("sku:" + skuId + ":lock",token,"nx","px",10*1000); //拿到10秒控制权限
                String token = UUID.randomUUID().toString();
                String OK = jedis.set("sku:" + skuId + ":lock",token,"nx","px",10*1000); //拿到10秒控制权限

                if(StringUtils.isNotBlank(OK)&&OK.equals("OK")){
                    //设置成功，有权在10秒的过期时间内访问数据库
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
                    //在访问mysql之后，将分布式锁释放
                    //jedis.del("sku:" + skuId + ":lock");
                    String lockToken = jedis.get("sku:" + skuId + ":lock");
                    if(StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)){
                        //用token确认删除的是自己的锁
                        //jedis.eval("lua"):可以使用lua脚本，在查询key的同时删除该key，防止高并发下的意外发生
                        jedis.del("sku:" + skuId + ":lock");
                    }


                }else{
                    //设置失败自旋（该线程睡眠几秒后，重新尝试访问）
                    try{
                        Thread.sleep(3000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return getSkuById(skuId);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return pmsSkuInfo;




//        //Redisson锁是包装的JUC内的锁策略，是Java代码层面的分布式锁
//        RLock lock = redisson.getLock("anyLock");//声明锁
//        // 加锁以后10秒钟自动解锁
//        // 无需调用unlock方法手动解锁
//        PmsSkuInfo pmsSkuInfo = null;
//        //链接缓存
//        Jedis jedis = RedisUtil.getJedis();
//        //查询缓存
//        String skuKey = "sku:"+skuId+":info";
//        String skuJson = jedis.get(skuKey);
//        try{
//            if(StringUtils.isNotBlank(skuJson)){
//                pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
//            }else{
//                //如果缓存没有，查询mysql
//                //设置分布式锁
//            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
//            boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
//            if (res) {
//                try {
//                    pmsSkuInfo = skuInfoMapper.selectById(skuId);
//                    List<PmsSkuImage> skuImageList = skuImageMapper.selectList(new QueryWrapper<PmsSkuImage>().eq("sku_id", skuId));
//                    pmsSkuInfo.setSkuImageList(skuImageList);
//                    //mysql查询结果存入redis
//                    if(pmsSkuInfo!=null){
//                        jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
//                    }else{
//                        //数据库中不存在该sku
//                        //为了防止缓存穿透，null或者空字符串设置给redis
//                        jedis.setex("sku:"+skuId+":info",60*3, JSON.toJSONString(""));
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                } finally {
//                    lock.unlock();
//                }
//                     }else{
//                return getSkuById(skuId); //如果得不到锁，重新访问
//            }
//
//                 }
//           }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            jedis.close();
//        }
//        return pmsSkuInfo;

        }

    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {
        List<PmsSkuInfo> skuSaleAttrValueListBySpu = skuInfoMapper.selectSkuSaleAttrValueListBySpu(spuId);
        return skuSaleAttrValueListBySpu;
    }


    public List<PmsSkuInfo> selectAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfos = skuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            Long skuId = pmsSkuInfo.getId();
            List<PmsSkuAttrValue> pmsSkuAttrValueList = skuAttrValueMapper.selectList(new QueryWrapper<PmsSkuAttrValue>().eq("sku_id", skuId));
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValueList);
        }
        return pmsSkuInfos;
    }


    public List<PmsSkuInfo> selectAll(String catalog3Id) {
        return null;
    }


}
