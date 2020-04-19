package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xatu.gmall.entity.PmsBaseCatalog1;
import com.xatu.gmall.entity.PmsBaseCatalog2;
import com.xatu.gmall.entity.PmsBaseCatalog3;
import com.xatu.gmall.mapper.CataLog1Mapper;
import com.xatu.gmall.mapper.CataLog2Mapper;
import com.xatu.gmall.mapper.CataLog3Mapper;
import com.xatu.gmall.service.CataLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 一级分类表 服务实现类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-18
 */
@Service
public class CataLogServiceImpl implements CataLogService {

    @Autowired
    CataLog1Mapper cataLog1Mapper;
    @Autowired
    CataLog2Mapper cataLog2Mapper;
    @Autowired
    CataLog3Mapper cataLog3Mapper;


    public List<PmsBaseCatalog1> getCatalog1() {
        return cataLog1Mapper.selectList(new QueryWrapper<PmsBaseCatalog1>());
    }

    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        return cataLog2Mapper.selectList(new QueryWrapper<PmsBaseCatalog2>().eq("catalog1_id",catalog1Id));
    }

    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        return cataLog3Mapper.selectList(new QueryWrapper<PmsBaseCatalog3>().eq("catalog2_id",catalog2Id));
    }
}
