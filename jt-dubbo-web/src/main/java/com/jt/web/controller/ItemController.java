package com.jt.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemCat;
import com.jt.web.pojo.ItemDesc;
import com.jt.web.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	@RequestMapping("/{itemId}")
	public String findItemById(@PathVariable Long itemId, Model model) {
		  Item item = itemService.findItemById(itemId);
		  Map<String, String> catMap = new HashMap<String,String>();
		  Long catId = item.getCid();
		  ItemCat itemCat1 = itemService.findItemCatById(catId);
		  ItemCat itemCat2 = itemService.findItemCatById(itemCat1.getParentId());
		  ItemCat itemCat3 = itemService.findItemCatById(itemCat2.getParentId());
//		  ItemCat itemCat4 = itemService.findItemCatById(itemCat3.getParentId());
		  catMap.put("catName1", itemCat1.getName());
		  catMap.put("catName2", itemCat2.getName());
		  catMap.put("catName3", itemCat3.getName());
//		  catMap.put("catName4", itemCat4.getName());
		  
		  ItemDesc itemDesc = itemService.findItemDescById(itemId);
		  model.addAttribute("item",item);
		  model.addAttribute("itemDesc", itemDesc);
		  model.addAttribute("catMap", catMap);
		  return "item";
	}
}
