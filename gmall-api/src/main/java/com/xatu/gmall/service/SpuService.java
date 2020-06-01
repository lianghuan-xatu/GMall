package com.xatu.gmall.service;

import com.xatu.gmall.entity.PmsProductImage;
import com.xatu.gmall.entity.PmsProductInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xatu.gmall.entity.PmsProductSaleAttr;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-20
 */
public interface SpuService extends IService<PmsProductInfo> {

    /**
     * 查询spu信息
     * @param catalog3Id
     * @return
     */
    List<PmsProductInfo> spuList(String catalog3Id);

    /**
     * 保存一个SPU单元
     * @param pmsProductInfo
     */
    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    /**
     * 返回商品对应的平台销售属性和值
     * @param spuId
     * @return
     */
    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    /**
     * 返回商品SPU的对应图片
     * @param spuId
     * @return
     */
    List<PmsProductImage> spuImageList(String spuId);

    /**
     * 查询sku对应的销售属性列表
     * @return
     */
    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,Long skuId);
}
