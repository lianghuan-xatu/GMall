package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xatu.gmall.entity.PmsProductImage;
import com.xatu.gmall.entity.PmsProductInfo;
import com.xatu.gmall.entity.PmsProductSaleAttr;
import com.xatu.gmall.entity.PmsProductSaleAttrValue;
import com.xatu.gmall.mapper.ImageMapper;
import com.xatu.gmall.mapper.SaleAttrMapper;
import com.xatu.gmall.mapper.SaleAttrValueMapper;
import com.xatu.gmall.mapper.SpuMapper;
import com.xatu.gmall.service.SpuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-20
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, PmsProductInfo> implements SpuService {

    @Autowired
    SpuMapper spuMapper;
    @Autowired
    SaleAttrMapper saleAttrMapper;
    @Autowired
    SaleAttrValueMapper saleAttrValueMapper;
    @Autowired
    ImageMapper imageMapper;

    public List<PmsProductInfo> spuList(String catalog3Id) {
        return spuMapper.selectList(new QueryWrapper<PmsProductInfo>().eq("catalog3_id",catalog3Id));
    }

    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        spuMapper.insert(pmsProductInfo);
        Long id = pmsProductInfo.getId();
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for(PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList){
            //添加SPU单元的销售属性
            pmsProductSaleAttr.setProductId(id);
            saleAttrMapper.insert(pmsProductSaleAttr);
            //添加SPU单元的销售属性值
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for(PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList){
                pmsProductSaleAttrValue.setProductId(id);
                saleAttrValueMapper.insert(pmsProductSaleAttrValue);
            }
        }
        //添加SPU单元的图片集合
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for(PmsProductImage pmsProductImage : spuImageList){
            pmsProductImage.setProductId(pmsProductInfo.getId());
            imageMapper.insert(pmsProductImage);
        }
    }

    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrList = saleAttrMapper.selectList(new QueryWrapper<PmsProductSaleAttr>().eq("product_id", spuId));
        for(PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList){
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = saleAttrValueMapper.selectList(new QueryWrapper<PmsProductSaleAttrValue>().eq("product_id", spuId).eq("sale_attr_id",pmsProductSaleAttr.getSaleAttrId()));
            pmsProductSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);

        }
        return pmsProductSaleAttrList;
    }

    public List<PmsProductImage> spuImageList(String spuId) {
        List<PmsProductImage> pmsProductImageList = imageMapper.selectList(new QueryWrapper<PmsProductImage>().eq("product_id", spuId));
        return pmsProductImageList;
    }


    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,Long skuId) {
        /*List<PmsProductSaleAttr> pmsProductSaleAttrList = saleAttrMapper.selectList(new QueryWrapper<PmsProductSaleAttr>().eq("product_id", productId));
        for(PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList){
            Long saleAttrId = pmsProductSaleAttr.getSaleAttrId();
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = saleAttrValueMapper.selectList(new QueryWrapper<PmsProductSaleAttrValue>().eq("product_id", productId).eq("sale_attr_id", saleAttrId));
            pmsProductSaleAttr.setSpuSaleAttrValueList(spuSaleAttrValueList);
        }*/
        List<PmsProductSaleAttr> pmsProductSaleAttrList = saleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
        return pmsProductSaleAttrList;
    }
}
