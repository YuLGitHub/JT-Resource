package com.jt.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemDescService;

@Controller
@RequestMapping("/web/itemDesc")
public class WebItemDescController {
	
	@Autowired
	private ItemDescService itemDescService;
	
	@RequestMapping("/findItemDescById/{itemId}")
	@ResponseBody
	public ItemDesc findItemDescById(@PathVariable Long itemId) {
		try {
			ItemDesc itemDesc = itemDescService.findItemDescById(itemId);
			if (itemDesc != null) {
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
