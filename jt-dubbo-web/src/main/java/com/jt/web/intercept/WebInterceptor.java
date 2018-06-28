package com.jt.web.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.util.CookieUtils;
import com.jt.web.pojo.User;
import com.jt.web.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

// 定义SpringMVC拦截器
public class WebInterceptor implements HandlerInterceptor {

	@Autowired
	private JedisCluster jedisCluster;
	private static ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果用户未登录该系统，应该在点击购物车按钮时，访问用户后台数据前生效
		String ticket = CookieUtils.getCookieValue(request, "JT_TICKET");
		if (!StringUtils.isEmpty(ticket)) {
			String userJSON = jedisCluster.get(ticket);
			if (!StringUtils.isEmpty(userJSON)) {
				User user = objectMapper.readValue(userJSON, User.class);
				// 将User信息保存之后被调用者使用
				// 通过ThreadLocal实现数据传输
				UserThreadLocal.set(user);
				return true;
			}
		}
		// 让用户再次登录一次
		response.sendRedirect("/user/login.html");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
