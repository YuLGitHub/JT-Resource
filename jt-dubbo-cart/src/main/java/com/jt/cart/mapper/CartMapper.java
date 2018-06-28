package com.jt.cart.mapper;

import com.jt.common.mapper.SysMapper;
import com.jt.duboo.pojo.Cart;

public interface CartMapper extends SysMapper<Cart> {

	void updateCartNum(Cart cart);

}
