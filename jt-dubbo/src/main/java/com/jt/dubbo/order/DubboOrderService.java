package com.jt.dubbo.order;

import java.util.List;

import com.jt.duboo.pojo.Order;
import com.jt.duboo.pojo.OrderItem;

public interface DubboOrderService {

	String saveOrder(Order order);

	Order findOrderById(String orderId);

	void updatePayStatusById(String r6_Order, Integer i);

	List<OrderItem> findOrderItemsById(String orderId);

	List<Order> findOrdersByUserId(Long userId, Integer page, Integer rows);

}
