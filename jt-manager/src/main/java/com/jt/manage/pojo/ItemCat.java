package com.jt.manage.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;

/**
 * 通过通用Mapper的形式操作数据库
 * 说明：
 * 	1.需要将pojo对象与数据表一一对应
 * 	2.定义数据表的主键
 * 	3.如果有自增需要设定
 * @author Tarena-java
 *
 */

@Table(name="tb_item_cat")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ItemCat extends BasePojo{
	private static final long serialVersionUID = -8884548477708215563L;
	@Id // 关联了主键信息
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 主键自增
	private Long id;		// 商品分类的ID
	// @Column(name="parent_id") // 注释掉原因：已开启自动驼峰映射
	private Long parentId;	// 上级ID
	private String name;	// 商品分类名称
	private Integer status;	// 1正常，2删除
	private Integer sortOrder; // 排序号
	private Boolean	isParent;	// 0,1判断是否为父级菜单
	
	/**
	 * 为了满足树形结构，添加get方法
	 * 
	 * @return
	 */
	public String getText() {
		return name;
	}
	// 根据菜单级别不同，open和closed是不同的
	public String getState() {
		/*
		if (isParent) {
			return "closed";
		} else {
			return "open";
		}
		*/
		return isParent ? "closed" : "open";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
}
