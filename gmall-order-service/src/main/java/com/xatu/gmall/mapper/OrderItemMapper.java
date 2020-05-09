package com.xatu.gmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xatu.gmall.entity.OmsOrder;
import com.xatu.gmall.entity.OmsOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.PostMapping;

@Mapper
public interface OrderItemMapper extends BaseMapper<OmsOrderItem> {
}
