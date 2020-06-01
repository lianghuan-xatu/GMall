package com.xatu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xatu.gmall.entity.PmsBaseAttrInfo;
import com.xatu.gmall.entity.PmsBaseAttrValue;
import com.xatu.gmall.entity.PmsBaseSaleAttr;
import com.xatu.gmall.mapper.AttrInfoMapper;
import com.xatu.gmall.mapper.AttrValueMapper;
import com.xatu.gmall.mapper.BaseSaleAttrMapper;
import com.xatu.gmall.service.AttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 平台属性表 服务实现类
 * </p>
 *
 * @author LiangHuan
 * @since 2020-04-19
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrInfoMapper, PmsBaseAttrInfo> implements AttrService {

    @Autowired
    AttrInfoMapper attrInfoMapper;
    @Autowired
    AttrValueMapper attrValueMapper;
    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {

        List<PmsBaseAttrInfo> attrInfoList = attrInfoMapper.selectList(
                new QueryWrapper<PmsBaseAttrInfo>()
                        .eq("catalog3_id", catalog3Id));

        for(PmsBaseAttrInfo pmsBaseAttrInfo : attrInfoList){
            Long id = pmsBaseAttrInfo.getId();
            List<PmsBaseAttrValue> attrValueList = attrValueMapper
                    .selectList(new QueryWrapper<PmsBaseAttrValue>()
                            .eq("attr_id", id));
            pmsBaseAttrInfo.setAttrValueList(attrValueList);
        }
        return attrInfoList;


    }

    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        Long id = pmsBaseAttrInfo.getId();
        if(id == null){
            //id为空,直接插入数据
            //保存属性
            attrInfoMapper.insert(pmsBaseAttrInfo);
            //保存属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for(PmsBaseAttrValue pmsBaseAttrValue : attrValueList){
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                attrValueMapper.insert(pmsBaseAttrValue);
            }
        }else{
            attrInfoMapper.update(pmsBaseAttrInfo,new UpdateWrapper<PmsBaseAttrInfo>().eq("id",id));
            //id不为空，修改数据：先删除后插入
            attrValueMapper.delete(new QueryWrapper<PmsBaseAttrValue>().eq("attr_id",id));
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for(PmsBaseAttrValue pmsBaseAttrValue : attrValueList){
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                attrValueMapper.insert(pmsBaseAttrValue);
            }

        }
        return "success";
    }

    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        return attrValueMapper.selectList(new QueryWrapper<PmsBaseAttrValue>().eq("attr_id",attrId));
    }

    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectList(new QueryWrapper<PmsBaseSaleAttr>());
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> attrValueIdSet) {
        String attrValueIdStr = StringUtils.join(attrValueIdSet, ",");
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = attrInfoMapper.selectAttrValueListByValueId(attrValueIdStr);
        return pmsBaseAttrInfoList;
    }


}
