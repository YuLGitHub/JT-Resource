package com.jt.web.util;

import com.jt.web.pojo.User;

public class UserThreadLocal {
	// 创建本地线程
	private static ThreadLocal<User> threadLocal = new ThreadLocal<User>();
	
	public static User get() {
		return threadLocal.get();
	}
	public static void set(User user) {
		threadLocal.set(user);
	}
	
}
