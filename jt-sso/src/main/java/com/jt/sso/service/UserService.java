package com.jt.sso.service;

import com.jt.sso.pojo.User;

public interface UserService {

	Boolean findCheckUser(String param, int type);

	String saveUser(User user);

	String findUserByUP(String username, String password);

}
