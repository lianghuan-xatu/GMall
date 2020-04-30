package com.xatu.gmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.*;
import com.xatu.gmall.service.AttrService;
import com.xatu.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import java.util.*;

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
        List<PmsBaseAttrInfo> attrInfovalueList = attrService.getAttrValueListByValueId(attrValueIdSet);
        //对平台属性集合进一步处理，去掉当前条件中valueId所在的属性
        List<String> valueIds = pmsSearchParam.getValueId();
        //面包屑
        //pmsSearchParam
        //valueIds
        List<PmsSearchCrumb> pmsSearchCrumbs =new ArrayList<>();
        if(valueIds!=null){
            for (String vId : valueIds) {
                Iterator<PmsBaseAttrInfo> iterator = attrInfovalueList.iterator(); //使用迭代器防止for循环删除越界
                // 如果valueIds参数不为空,说明当前请求中包含属性的参数，每一个属性参数，都会生成一个面包屑
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setValueId(vId);
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam,vId));
            while (iterator.hasNext()){
                PmsBaseAttrInfo attrInfo = iterator.next();
                List<PmsBaseAttrValue> attrValueList = attrInfo.getAttrValueList();
                for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                    String id = pmsBaseAttrValue.getId().toString();
                        if(id.equals(vId)){
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            //删除该属性值所在的属性组
                            iterator.remove();
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
        }
        modelMap.put("attrList",attrInfovalueList);

        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam",urlParam);
        modelMap.put("attrValueSelectedList",pmsSearchCrumbs);
        return "list";
    }

    //获取当前请求路径上的参数
    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        List<String> valueIdList = pmsSearchParam.getValueId();

        String urlParam = "";
        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam + "&";
            }
             urlParam += "catalog3Id="+catalog3Id;
        }
        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam += "keyword="+keyword;
        }
        if(valueIdList!=null){
            for (String valueId : valueIdList) {
                urlParam += "&valueId="+valueId;
            }
        }
        return urlParam;

    }


    /**
     * 获取每个面包屑的url字符串
     * 减去当前valueId的urlParam
     * @param pmsSearchParam
     * @param
     * @return
     */
    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam,String delValueId) {
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        List<String> valueIdList = pmsSearchParam.getValueId();

        String urlParam = "";
        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam += "catalog3Id="+catalog3Id;
        }
        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam += "keyword="+keyword;
        }
        if(valueIdList!=null){
            for (String valueId : valueIdList) {
                if(!valueId.equals(delValueId)){
                    urlParam += "&valueId="+valueId;
                }
            }
        }
        return urlParam;

    }

}

