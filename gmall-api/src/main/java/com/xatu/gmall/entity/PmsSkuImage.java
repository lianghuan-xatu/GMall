package com.xatu.gmall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存单元图片表
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-22
 */
@TableName("pms_sku_image")
public class PmsSkuImage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 图片名称（冗余）
     */
    private String imgName;

    /**
     * 图片路径(冗余)
     */
    private String imgUrl;

    /**
     * 商品图片id
     */
    @TableField("product_img_id")
    private Long spuImgId;

    /**
     * 是否默认
     */
    private String isDefault;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getSpuImgId(){
        return spuImgId;
    }

    public void setSpuImgId(Long spuImgId) {
        this.spuImgId = spuImgId;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
