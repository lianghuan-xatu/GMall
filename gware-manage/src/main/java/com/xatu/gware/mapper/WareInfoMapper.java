package com.xatu.gware.mapper;

import com.xatu.gware.bean.WareInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @param
 * @return
 */

public interface WareInfoMapper extends Mapper<WareInfo> {


    public List<WareInfo> selectWareInfoBySkuid(String skuid);



}
