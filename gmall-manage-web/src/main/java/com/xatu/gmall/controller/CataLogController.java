package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.PmsBaseCatalog1;
import com.xatu.gmall.entity.PmsBaseCatalog2;
import com.xatu.gmall.entity.PmsBaseCatalog3;
import com.xatu.gmall.service.CataLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-18
 */
@Controller
@CrossOrigin
public class CataLogController {

    @Reference
    CataLogService cataLogService;

    @RequestMapping("/getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        System.out.println("数据获取中...");
        List<PmsBaseCatalog1> catalogs = cataLogService.getCatalog1();
        System.out.println(catalogs);
        return catalogs;
    }


    @RequestMapping("/getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        System.out.println("数据获取中...");
        List<PmsBaseCatalog2> catalogs = cataLogService.getCatalog2(catalog1Id);
        System.out.println(catalogs);
        return catalogs;
    }

    @RequestMapping("/getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        System.out.println("数据获取中...");
        List<PmsBaseCatalog3> catalogs = cataLogService.getCatalog3(catalog2Id);
        System.out.println(catalogs);
        return catalogs;
    }



}

