package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.order.DubboOrderService;
import com.jt.duboo.pojo.Order;
import com.jt.duboo.pojo.OrderItem;
import com.jt.duboo.pojo.OrderShipping;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

public class OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	// 引入消息队列模板类
	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 * 1.赋值orderId号 订单号：登录用户id+当前时间戳
	 * 2.准备Order新增数据
	 * 3.准备OrderShipping新增数据
	 * 4.准备OrderItem的新增数据
	 */
	/**
	 * 通过消息队列来入队 
	 */
	@Override
	public String saveOrder(Order order) {
		
		String orderId = order.getUserId() + "" + System.currentTimeMillis();
		order.setOrderId(orderId);
		order.setCreated(new Date());
		order.setUpdated(order.getCreated());
		// 通过消息队列处理消息
		rabbitTemplate.convertAndSend("order.save", order);
		
		
		/*
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
		*/
		System.out.println("消息队列调用完成!!!!!");
		
		return orderId; // 返回orderId
	}

	/**
	 * 利用通用Mapepr实现数据查询
	 */
	@Override
	public Order findOrderById(String orderId) {
		
		//1.查询order对象
		Order order = orderMapper.selectByPrimaryKey(orderId);
		
		//2.查询orderItem对象
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		List<OrderItem> orderItems = orderItemMapper.select(orderItem);
		
		//3.查询orderShipping
		OrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId);
		
		//4.实现数据的封装
		order.setOrderShipping(orderShipping);
		order.setOrderItems(orderItems);
		
		return order;
	}

	@Override
	public void updatePayStatusById(String r6_Order, Integer i) {
		Order order = new Order();
		order.setOrderId(r6_Order);
		order.setStatus(i);
		order.setPaymentTime(new Date());
		orderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	public List<OrderItem> findOrderItemsById(String orderId) {
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		List<OrderItem> list = orderItemMapper.select(orderItem);
		return list;
	}


	@Override
	public List<Order> findOrdersByUserId(Long userId, Integer page, Integer rows) {
		Integer start = (page - 1) * rows;
		List<Order> list = orderMapper.selectOrdersByPage(userId, start, rows);
		return list;
	}
	
}
