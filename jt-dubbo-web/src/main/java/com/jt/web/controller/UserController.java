package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@RequestMapping("/register")
	public String register() {
		return "register";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	// http://www.jt.com/service/user/doRegister
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			String username = userService.saveUser(user);
			return SysResult.oK(username);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新增失败");
		}
	}
	
	/**
	 * /service/user/doLogin
	 * 1.登录操作
	 * 2.获取ticket信息
	 * 3.将ticket信息存入cookie中
	 * 4.将cookie写入后正确返回
	 * @param user
	 * @return
	 */
	// 
	@RequestMapping("doLogin")
	@ResponseBody
	public SysResult doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		try {
			String ticket = userService.findUserByUP(user);
			// 判断ticket信息是否有效
			if (StringUtils.isEmpty(ticket)) {
				// 为空，表示ticket没有获取
				// 1.前端业务代码出错 返回null
				// 2.SSO系统出错
				return SysResult.build(201, "用户登录失败");
			} else {
				// 用户信息不为空，写入cookie中
				CookieUtils.setCookie(request, response, "JT_TICKET", ticket);
				return SysResult.oK();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "登录失败");
		}
	}
	
	// /logout
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String ticket = CookieUtils.getCookieValue(request, "JT_TICKET");
		jedisCluster.del(ticket);
		CookieUtils.deleteCookie(request, response, "JT_TICKET");
		return "redirect:/index.html";
	}
	
	
}
