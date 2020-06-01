package com.xatu.gmall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存单元表
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-22
 */
@TableName("pms_sku_info")
public class PmsSkuInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 库存id(itemID)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品id
     */
    @TableField("product_id")
    private String spuId;


    /**
     * 价格
     */
    private Double price;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 商品规格描述
     */
    private String skuDesc;

    private Double weight;

    /**
     * 品牌(冗余)
     */
    private Long tmId;

    /**
     * 三级分类id（冗余)
     */
    private Long catalog3Id;

    /**
     * 默认显示图片(冗余)
     */
    private String skuDefaultImg;

    /**
     * 库存商品图片
     */
    @TableField(exist = false)
    private List<PmsSkuImage> skuImageList;
    /**
     * 库存属性
     */
    @TableField(exist = false)
    private List<PmsSkuAttrValue> skuAttrValueList;
    /**
     * 库存属性值
     */
    @TableField(exist = false)
    private List<PmsSkuSaleAttrValue> skuSaleAttrValueList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    public Long getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(Long catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getSkuDefaultImg() {
        return skuDefaultImg;
    }

    public void setSkuDefaultImg(String skuDefaultImg) {
        this.skuDefaultImg = skuDefaultImg;
    }


    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public List<PmsSkuImage> getSkuImageList() {
        return skuImageList;
    }

    public void setSkuImageList(List<PmsSkuImage> skuImageList) {
        this.skuImageList = skuImageList;
    }

    public List<PmsSkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public void setSkuAttrValueList(List<PmsSkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }

    public List<PmsSkuSaleAttrValue> getSkuSaleAttrValueList() {
        return skuSaleAttrValueList;
    }

    public void setSkuSaleAttrValueList(List<PmsSkuSaleAttrValue> skuSaleAttrValueList) {
        this.skuSaleAttrValueList = skuSaleAttrValueList;
    }

    @Override
    public String toString() {
        return "PmsSkuInfo{" +
                "id=" + id +
                ", spuId=" + spuId +
                ", price=" + price +
                ", skuName='" + skuName + '\'' +
                ", skuDesc='" + skuDesc + '\'' +
                ", weight=" + weight +
                ", tmId=" + tmId +
                ", catalog3Id=" + catalog3Id +
                ", skuDefaultImg='" + skuDefaultImg + '\'' +
                ", skuImageList=" + skuImageList +
                ", skuAttrValueList=" + skuAttrValueList +
                ", skuSaleAttrValueList=" + skuSaleAttrValueList +
                '}';
    }
}
