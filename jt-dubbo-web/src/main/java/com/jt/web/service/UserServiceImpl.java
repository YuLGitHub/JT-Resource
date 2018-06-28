package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	// 通过HttpClient实现跨域
	@Autowired
	private HttpClientService httpClientService;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String saveUser(User user) {
		// 跨域请求的URI
		String uri = "http://sso.jt.com/user/register";
		// 参数设定
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		try {
			String resultData = httpClientService.doPost(uri, params);
			SysResult sysResult = objectMapper.readValue(resultData, SysResult.class);
			return (String) sysResult.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 1.定义目标URI
	 * 2.准备数据
	 * 3.发送请求
	 * 4.解析数据
	 * 5.返回数据
	 */
	@Override
	public String findUserByUP(User user) {
		String uri = "http://sso.jt.com/user/login";
		Map<String, String> map = new HashMap<String, String>();
		map.put("u", user.getUsername());
		map.put("p", user.getPassword());
		
		try {
			String resultData = httpClientService.doPost(uri, map);
			
			SysResult sysResult = objectMapper.readValue(resultData, SysResult.class);
			String ticket = (String) sysResult.getData();
			return ticket;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
