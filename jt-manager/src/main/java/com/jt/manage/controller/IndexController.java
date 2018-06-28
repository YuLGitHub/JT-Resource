package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	// 实现页面转向
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	/**
	 * 问题：在index页面上需要跳转多个页面，则需要编辑多个Controller方法用来处理请求
	 * 要求：采用一个方法来实现页面的通用跳转
	 * 方法：采用RESTful结构实现页面通用跳转
	 * 
	 * 解决方法：
	 * 	@PathVariable作用：将{module}的数据作为参数传递
	 * 
	 * 总结：
	 * 	1.RESTful结构参数使用{}包裹
	 * 	2.参数与参数之间使用/分割
	 * 	3.接收的参数与变量需一致
	 * 	4.使用@PathVariable进行参数
	 * @return
	 */
	
	// 跳转到页面通用
	@RequestMapping("/page/{module}")
	public String toItemAdd(@PathVariable String module) {
		return module;
	}
	
	
}
