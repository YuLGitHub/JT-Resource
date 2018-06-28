package com.jt.sso.mapper;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.sso.pojo.User;

public interface UserMapper extends SysMapper<User> {

	// 查询验证用户名、邮箱、手机号是否已存在
	int findCheckUser(@Param("cloumn") String cloumn, @Param("param") String param);

	// 根据用户名密码查询用户信息
	User findUserByUP(@Param("username") String username, @Param("password") String password);
	
}
