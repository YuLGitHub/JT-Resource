package com.jt.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.duboo.pojo.Order;

public interface OrderMapper extends SysMapper<Order>{

	// 修改超时订单状态
	void updateState(Date agoDate);

	List<Order> selectOrdersByPage(@Param("userId") Long userId, @Param("begin") Integer start, @Param("rows") Integer rows);

}
