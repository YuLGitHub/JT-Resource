package com.jt.manage.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;
//import com.jt.manage.service.XXX;

@Controller
@RequestMapping("/item")
public class ItemController {

	// 定义日志
	private static final Logger logger = Logger.getLogger(ItemController.class);
	
	@Autowired
	private ItemService itemService;

	@RequestMapping("/findAll")
	@ResponseBody // 将对象通过JackSONjar包转化为JSON串
	public List<Item> findItemAll() {
		return itemService.findItemAll();
	}

	// 通过分页的方式实现数据的查询
	// localhost:8091/item/query?page=1&rows=20
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult findItemByPage(Integer page, Integer rows) {
		return itemService.findItemByPage(page, rows);
	}

	/**
	 * 采用传统ajax实现数据的回显
	 */
	
	@RequestMapping("/cat/queryItemCatName")
	public void findItemCatNameById(Long itemCatId, HttpServletResponse response) {
		String name = itemService.findItemCatName(itemCatId);
		// 设置字符集编码
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ResponseBody的注意事项
	 * 返回的是String，则需要处理乱码问题
	 * 
	 * SpringMVC中规定：
	 * 返回的是对象时，解析用utf-8
	 * 返回的是字符串时，解析使用ISO-8859-1
	 * 
	 * 1.封装为对象
	 * 2.设置字符集
	 * 
	 */
	/*// 注释原因：上面已经使用一种方式实现了
	@RequestMapping(value="/cat/queryItemCatName",produces="text/html;charset=utf-8")
	@ResponseBody
	public String findItemCatNameById(Long itemCatId) {
		return itemService.findItemCatName(itemCatId);
	}
	*/
	
	
	/**
	 * 商品新增
	 */
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item, String desc) {
		try {
			itemService.saveItem(item, desc);
			return SysResult.oK();
			
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新增商品失败");
		}
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item, String desc) {
		try {
			itemService.updateItem(item, desc);
			logger.info("{~~~~~~~~~~~~~更新成功}");
			return SysResult.build(200, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{！！！！！！！！！！！！更新操作失败}");
			return SysResult.build(201, "更新失败");
			
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItem(Long[] ids) {
		try {
			itemService.deleteItems(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("!!!!!!!!删除失败");
			return SysResult.build(201, "删除失败");
		
		}
	}
	
	// 上架商品
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult itemReshelf(Long[] ids) {
		try {
			int status = 1;
			itemService.updateStatus(status, ids);
			return SysResult.build(200, "上架成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "上架失败");
		}
	}
	// 下架商品
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult itemInstock(Long[] ids) {
		try {
			int status = 2;
			itemService.updateStatus(status, ids);
			return SysResult.build(200, "下架成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "下架失败");
		}
	}
	
	
	
	/**
	 * 商品描述的回显
	 * http://localhost:8091/item/query/item/desc/1474391969
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable Long itemId) {
		try {
			ItemDesc itemDesc = itemService.findItemDescById(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("!!!!!!!!查询商品描述失败");
			return SysResult.build(201, "查询商品描述失败");
		
		}
	}
	
	
	
	
}
