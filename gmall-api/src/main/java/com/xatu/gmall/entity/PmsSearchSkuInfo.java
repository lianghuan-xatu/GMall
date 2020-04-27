package com.xatu.gmall.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.util.List;

public class PmsSearchSkuInfo {

    @TableId
    private String id;
    private Long price;
    private String skuName;
    private String skuDesc;
    private String catalog3Id;
    private String skuDefaultImg;
    private double hotscore;
    private String productId;
    private List<PmsSkuAttrValue> skuAttrValueList;

    public String getId() {
        return id;
    }


    public void setId(Long id) {
        String idStr = id.toString();
        this.id = idStr;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public void setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getSkuDefaultImg() {
        return skuDefaultImg;
    }

    public void setSkuDefaultImg(String skuDefaultImg) {
        this.skuDefaultImg = skuDefaultImg;
    }

    public double getHotscore() {
        return hotscore;
    }

    public void setHotscore(double hotscore) {
        this.hotscore = hotscore;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<PmsSkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public void setSkuAttrValueList(List<PmsSkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }
}
