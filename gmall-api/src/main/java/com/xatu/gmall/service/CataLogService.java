package com.xatu.gmall.service;

import com.xatu.gmall.entity.PmsBaseCatalog1;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xatu.gmall.entity.PmsBaseCatalog2;
import com.xatu.gmall.entity.PmsBaseCatalog3;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-18
 */
public interface CataLogService   {

    /**
     * 查询所有一级分类
     * @return
     */
    List<PmsBaseCatalog1> getCatalog1();

    /**
     * 查询所有二级分类
     * @return
     */
    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    /**
     * 查询所有三级分类
     * @param catalog2Id
     * @return
     */

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
