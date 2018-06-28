package com.jt.order.mapper;

import java.util.List;

import com.jt.common.mapper.SysMapper;
import com.jt.duboo.pojo.OrderItem;

public interface OrderItemMapper extends SysMapper<OrderItem>{

	List<OrderItem> selectOrderItemsByOrderId(Long orderId);

}
