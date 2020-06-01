package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.PmsProductImage;
import com.xatu.gmall.entity.PmsProductInfo;
import com.xatu.gmall.entity.PmsProductSaleAttr;
import com.xatu.gmall.service.SpuService;
import com.xatu.gmall.util.PmsUploadUtil;
import org.csource.fastdfs.ClientGlobal;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-20
 */
@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;


    @RequestMapping("/spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){
        List<PmsProductInfo> spuList=spuService.spuList(catalog3Id);
        return spuList;
    }

    @RequestMapping("/fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        //将图片或者音视频上传到分布式文件系统
        String imageUrl = PmsUploadUtil.uploadImage(multipartFile);
        //将图片的存储路径返回给页面
        return imageUrl;
    }


    @RequestMapping("/saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping("/spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
       List<PmsProductSaleAttr> spuSaleAttrList = spuService.spuSaleAttrList(spuId);
       return spuSaleAttrList;
    }

    @RequestMapping("/spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){
       List<PmsProductImage> spuImageList = spuService.spuImageList(spuId);
       return spuImageList;
    }


}

