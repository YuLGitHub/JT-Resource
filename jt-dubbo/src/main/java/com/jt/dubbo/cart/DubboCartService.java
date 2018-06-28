package com.jt.dubbo.cart;

import java.util.List;

import com.jt.duboo.pojo.Cart;

public interface DubboCartService {

	List<Cart> findCartByUserId(Long id);

	void updateCartNum(Cart cart);

	void saveCart(Cart cart);

	void deleteCart(Cart cart);

	//List<Cart> findCartList(Long userId);

}
