package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jt.dubbo.search.DubboSearchService;
import com.jt.duboo.pojo.Item;
import com.jt.web.pojo.ItemCat;
import com.jt.web.service.ItemService;

@Controller
public class SearchController {
	@Autowired
	private DubboSearchService searchService;
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q") String keyword, @RequestParam("page") Integer page, Model model) {
		try {
			if (page == null) {
				page = 1; 
			}
			keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
			List<Item> itemList = searchService.findItemListByKey(keyword, page);
			model.addAttribute("query", keyword);
			model.addAttribute("itemList", itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "search";
	}
	
	//http://www.jt.com/products/83.html
	@RequestMapping("/products/{itemCatId}")
	public String searchProducts(@PathVariable Long itemCatId, Model model) {
		try {
			ItemCat itemCat = itemService.findItemCatById(itemCatId);
			List<Item> itemList = searchService.findItemListByKey(itemCat.getName(),1);
			model.addAttribute("itemList", itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "search";
	}
}
