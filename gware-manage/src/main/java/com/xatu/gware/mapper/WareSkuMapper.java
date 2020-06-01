package com.xatu.gware.mapper;

import com.xatu.gware.bean.WareSku;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @param
 * @return
 */

public interface WareSkuMapper extends Mapper<WareSku> {


    public Integer selectStockBySkuid(String skuid);

    public int incrStockLocked(WareSku wareSku);

    public int selectStockBySkuidForUpdate(WareSku wareSku);

    public int  deliveryStock(WareSku wareSku);

    public List<WareSku> selectWareSkuAll();
}
