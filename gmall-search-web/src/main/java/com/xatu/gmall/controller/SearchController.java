package com.xatu.gmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.PmsBaseAttrInfo;
import com.xatu.gmall.entity.PmsSearchParam;
import com.xatu.gmall.entity.PmsSearchSkuInfo;
import com.xatu.gmall.entity.PmsSkuAttrValue;
import com.xatu.gmall.service.AttrService;
import com.xatu.gmall.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;
    @Reference
    AttrService attrService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){
        //三级分类id、关键字

        //调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList",pmsSearchSkuInfos);
        //在做商品平台属性进行聚合展示时
        // 方案一：可通过pmsSearchSkuInfos中的skuId去pms_sku_attr_value表中查出对应的平台属性值
        //方案二：使用HashSet抽取检索结果所包含的平台属性集合
        Set<String> attrValueIdSet = new HashSet();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                Long valueId = pmsSkuAttrValue.getValueId();
                attrValueIdSet.add(valueId.toString());
            }
        }
        //根据valueId获取平台属性值对象集合
        List<PmsBaseAttrInfo> attrInfoList = attrService.getAttrValueListByValueId(attrValueIdSet);
        modelMap.put("attrList",attrInfoList);
        return "list";
    }
}

