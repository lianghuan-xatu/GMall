package com.xatu.gmall.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

public class PmsSearchSkuInfo implements Serializable {

    @TableId
    private Long id;
    private double price;
    private String skuName;
    private String skuDesc;
    private Long catalog3Id;
    private String skuDefaultImg;
    private double hotscore;
    private String productId;
    private List<PmsSkuAttrValue> skuAttrValueList;

    public Long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(Long catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public void setId(Long id) {
      this.id = id;
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
