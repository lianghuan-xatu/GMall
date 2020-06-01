package com.xatu.gmall.service;

import com.xatu.gmall.entity.PmsSkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xatu.gmall.entity.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * sku平台属性值关联表 服务类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-22
 */

public interface SkuService extends IService<PmsSkuInfo> {

    /**
     * 保存商品库存信息
     * @param pmsSkuInfo
     */
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    /**
     * 根据库存id获取库存信息
     * @param skuId
     * @return
     */
    PmsSkuInfo getSkuById(String skuId);

    /**
     * 根据spuId来获取对应的sku集合和sku集合内的对应的属性
     * @param spuId
     * @return
     */
    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId);


    /**
     * 查询所有sku的信息
     */
    List<PmsSkuInfo> selectAllSku(String catalog3Id);


    String getSkuPriceBySkuId(Long productSkuId);

    boolean checkPrice(Long productSkuId, BigDecimal price);
}
