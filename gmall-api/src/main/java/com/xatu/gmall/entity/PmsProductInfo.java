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
 * 
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-20
 */
@TableName("pms_product_info")
public class PmsProductInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品描述(后台简述）
     */
    private String description;

    /**
     * 三级分类id
     */
    private Long catalog3Id;

    /**
     * 品牌id
     */
    private Long tmId;

    @TableField(exist = false)
    private List<PmsProductSaleAttr> spuSaleAttrList;

    @TableField(exist = false)
    private List<PmsProductImage> spuImageList;

    public List<PmsProductImage> getSpuImageList() {
        return spuImageList;
    }

    public void setSpuImageList(List<PmsProductImage> spuImageList) {
        this.spuImageList = spuImageList;
    }

    public List<PmsProductSaleAttr> getSpuSaleAttrList() {
        return spuSaleAttrList;
    }

    public void setSpuSaleAttrList(List<PmsProductSaleAttr> spuSaleAttrList) {
        this.spuSaleAttrList = spuSaleAttrList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(Long catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    @Override
    public String toString() {
        return "PmsProductInfo{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", catalog3Id=" + catalog3Id +
                ", tmId=" + tmId +
                '}';
    }
}
