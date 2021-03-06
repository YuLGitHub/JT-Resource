package com.jt.manage.pojo;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 8564137889476580491L;
	// 定义pojo对象
	private Integer id;
	private String name;
	private Integer age;
	private String sex;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
