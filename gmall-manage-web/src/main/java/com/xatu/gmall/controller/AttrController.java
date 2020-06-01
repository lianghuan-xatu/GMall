package com.xatu.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.entity.PmsBaseAttrInfo;
import com.xatu.gmall.entity.PmsBaseAttrValue;
import com.xatu.gmall.entity.PmsBaseSaleAttr;
import com.xatu.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 属性表 前端控制器
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-19
 */
@Controller
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;

    @RequestMapping("/attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){
        List<PmsBaseAttrInfo> attrInfoList = attrService.attrInfoList(catalog3Id);
        return attrInfoList;
    }

    @RequestMapping("/saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }

    @RequestMapping("/getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){
        List<PmsBaseAttrValue> attrValueList= attrService.getAttrValueList(attrId);
        return attrValueList;
    }



    @RequestMapping("/baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        List<PmsBaseSaleAttr> baseSaleAttrList = attrService.baseSaleAttrList();
        return baseSaleAttrList;

    }

}

