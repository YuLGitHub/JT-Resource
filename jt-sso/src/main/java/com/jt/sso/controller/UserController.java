package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	// /user/check/
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object findCheckUser(@PathVariable String param, @PathVariable int type, String callback) {
		try {
			Boolean flag = userService.findCheckUser(param, type);
			MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.oK(flag));
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			String username = userService.saveUser(user);
			return SysResult.oK(username);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新增用户失败");
		}
		
	}
	
	// http://sso.jt.com/user/login
	@RequestMapping("/login")
	@ResponseBody
	public SysResult doLogin(@RequestParam("u") String username, @RequestParam("p") String password) {
		try {
			String ticket = userService.findUserByUP(username, password);
			return SysResult.oK(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "用户登录失败");
		}
	}
	
	// http://sso.jt.com/user/query/{ticket}
	// JSONP格式
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public Object findUserByTicket(@PathVariable String ticket, String callback) {
		try {
			String jsonData = jedisCluster.get(ticket);
			if (StringUtils.isEmpty(jsonData)) {
				MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.build(201, "用户未登录"));
				// 设置回调函数名
				jacksonValue.setJsonpFunction(callback);
				return jacksonValue;
			} else {
				MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.oK(jsonData));
				jacksonValue.setJsonpFunction(callback);
				return jacksonValue;
			}
		} catch (Exception e) {
			MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.build(201, "查询失败"));
			// 设置回调函数名
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
	}
	
	
}
