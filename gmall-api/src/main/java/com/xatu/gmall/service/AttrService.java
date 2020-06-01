package com.xatu.gmall.service;

import com.xatu.gmall.entity.PmsBaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xatu.gmall.entity.PmsBaseAttrValue;
import com.xatu.gmall.entity.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 属性表 服务类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-19
 */
public interface AttrService extends IService<PmsBaseAttrInfo> {

    /**
     * 查询所有平台属性
     * @param catalog3Id
     * @return
     */
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    /**
     * 添加平台属性和平台属性值
     * @param pmsBaseAttrInfo
     */
    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    /**
     * 查询所有平台的属性值
     *
     */
    List<PmsBaseAttrValue> getAttrValueList(String attrId);


    /**
     * 查询平台规定的基础销售属性
     * @return
     */
    List<PmsBaseSaleAttr> baseSaleAttrList();

    /**
     * 根据属性值id查询出对应的属性
     * @param attrValueIdSet
     * @return
     */
    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> attrValueIdSet);
}
