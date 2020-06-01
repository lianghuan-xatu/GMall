package com.xatu.gmall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.catalina.LifecycleState;

/**
 * <p>
 * 一级分类表
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-18
 */
@TableName("pms_base_catalog1")
public class PmsBaseCatalog1 implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 一级分类下所包含的二级分类
     */
    @TableField(exist = false)
    private  List<PmsBaseCatalog2> catalog2List;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PmsBaseCatalog2> getCatalog2List() {
        return catalog2List;
    }

    public void setCatalog2List(List<PmsBaseCatalog2> catalog2List) {
        this.catalog2List = catalog2List;
    }

    @Override
    public String toString() {
        return "PmsBaseCatalog1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", catalog2List=" + catalog2List +
                '}';
    }
}
