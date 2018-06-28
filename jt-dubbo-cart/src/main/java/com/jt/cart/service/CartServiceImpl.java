package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.cart.mapper.CartMapper;
import com.jt.common.service.BaseService;
import com.jt.dubbo.cart.DubboCartService;
import com.jt.duboo.pojo.Cart;

public class CartServiceImpl extends BaseService<Cart> implements DubboCartService {

	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartByUserId(Long id) {
		Cart cart = new Cart();
		cart.setUserId(id);
		return cartMapper.select(cart);
	}

	@Override
	public void updateCartNum(Cart cart) {
		cartMapper.updateCartNum(cart);
	}

	/**
	 * 新增购物车思路
	 * 1.首先查询Item和userID查询购物车信息
	 * 2.若查询结果不为null，表示已经有购物车，应该更新商品数量
	 * 3.若为null，入库
	 */
	@Override
	public void saveCart(Cart cart) {
		Cart cartDB = new Cart();
		cartDB.setUserId(cart.getUserId());
		cartDB.setItemId(cart.getItemId());
		// 如果调用的是父级的方法，需要调用super进行标识
		Cart tempCart = super.queryByWhere(cartDB);
		if (tempCart != null) {
			int countNum = tempCart.getNum() + cart.getNum();
			tempCart.setNum(countNum);
			tempCart.setUpdated(new Date());
			// 首先更新操作
			cartMapper.updateByPrimaryKeySelective(tempCart);
		} else {
			// 入库操作
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}
		
	}

	@Override
	public void deleteCart(Cart cart) {
		cartMapper.delete(cart);
	}

	
}
