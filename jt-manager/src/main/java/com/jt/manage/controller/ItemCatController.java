package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;

@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	// 通用Mappers的入门案例、
	
	/**
	 * 若value中的id为null， 默认才会启用defaultValue
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<ItemCat> findItemCat(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		// Long parentId = 0L; // 定义一级菜单的父级
		
		// 根据我的parentID查询商品的分类信息
		return itemCatService.findItemCatByParentId(parentId);
	}
	
}
