package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.dubbo.cart.DubboCartService;
import com.jt.duboo.pojo.Cart;
import com.jt.web.pojo.User;
import com.jt.web.util.UserThreadLocal;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private DubboCartService cartService;
	
	
	// /cart/show.html
	@RequestMapping("/show")
	public String findCartById(Model model) {
		User user = UserThreadLocal.get();
		List<Cart> cartList = cartService.findCartByUserId(user.getId());
		model.addAttribute("cartList", cartList);
		// 返回具体购物车展示页面
		return "cart";
	}
	
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num) {
		Long userId = UserThreadLocal.get().getId(); // 后期修改
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cart.setNum(num);
		try {
			cartService.updateCartNum(cart);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "修改失败");
		}
	}
	
	// 新增购物车信息
	@RequestMapping("/add/{itemId}")
	public String saveCart(@PathVariable Long itemId, Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setItemId(itemId);
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show.html"; 
	}
	
	// 删除购物信息
	// cart/delete/1474391973
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId, Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}
	
}
