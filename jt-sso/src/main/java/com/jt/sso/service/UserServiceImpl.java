package com.jt.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Boolean findCheckUser(String param, int type) {
		String cloumn = null;
		switch (type) {
		case 1: cloumn = "username"; break;
		case 2: cloumn = "phone"; break;
		case 3: cloumn = "email"; break;
		}
		int count = userMapper.findCheckUser(cloumn, param);
		return count != 0 ? true : false;
	}

	@Override
	public String saveUser(User user) {
		// 为user补全信息
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		user.setEmail(user.getPhone() + "@jt.com");
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		// 入库
		userMapper.insert(user);
		return user.getUsername();
	}

	/**
	 * 1.将密码进行加密处理
	 * 2.根据用户名和密码查询用户信息
	 * 3.匹配正确后生成密钥
	 * 4.将userJSON数据存入redis中
	 * 5.匹配不成功后直接返回null
	 */
	@Override
	public String findUserByUP(String username, String password) {
		String md5Password = DigestUtils.md5Hex(password);
		User user = userMapper.findUserByUP(username,md5Password);
		try {
			if (user != null) {
				// 获取用户名ticket
				String ticket = DigestUtils.md5Hex("JT_TICKET" + System.currentTimeMillis() + username);
				// 获取对象的JSON串
				String jsonData = objectMapper.writeValueAsString(user);
				// 将用户信息写入Redis中
				jedisCluster.set(ticket, jsonData);
				// 并将ticket返回浏览器，以便cookie中保存
				return ticket;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
