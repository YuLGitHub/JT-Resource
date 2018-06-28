package com.jt.order.rabbit.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.duboo.pojo.Order;
import com.jt.duboo.pojo.OrderItem;
import com.jt.duboo.pojo.OrderShipping;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;


public class OrderServiceImpl {
	
	// 将消息队列中的数据写入数据库中
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	public void saveOrder(Order order) {
		
		String orderId = order.getOrderId();
		//实现Order对象的入库操作
		order.setOrderId(orderId);
		order.setStatus(1);  //状态：1、未付款2、已付款3、未发货4、已发货5、交易成功6、交易关闭
		order.setCreated(new Date());
		order.setUpdated(order.getCreated());
		
		orderMapper.insert(order);
		System.out.println("订单商品入库成功!!!!!!");
		
		//订单物流入库
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(order.getCreated());
		orderShipping.setUpdated(order.getCreated());
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流信息入库成功!!!!!");
		
		//订单商品入库
		//1.实现批量的入库操作 自己手写sql
		//2.通过循环遍历的方式,实现多次入库操作
		List<OrderItem> orderItems = order.getOrderItems();
		
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(order.getCreated());
			orderItem.setUpdated(order.getCreated());
			orderItemMapper.insert(orderItem);
		}
		
		System.out.println("消息队列入队成功");
	}
}
