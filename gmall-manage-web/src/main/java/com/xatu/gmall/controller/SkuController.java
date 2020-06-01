package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.PmsSkuInfo;
import com.xatu.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * sku平台属性值关联表 前端控制器
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-22
 */
@Controller
@CrossOrigin
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping("/saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        //处理默认图片
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if(StringUtils.isBlank(skuDefaultImg)){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }
        skuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }

}

