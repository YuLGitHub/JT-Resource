package com.jt.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.ItemCatResult;
import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;

@Controller
@RequestMapping("/web/itemCat")
public class WebItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
//	private ObjectMapper objectMapper = new ObjectMapper();
	
	/*@RequestMapping("/web/itemcat/all")
	public void findItemCatAll(String callback, HttpServletResponse response) {
		try {
			ItemCatResult catResult = itemCatService.findItemCatAll();
			System.out.println(catResult);
			String jsonData = objectMapper.writeValueAsString(catResult);
			String result = callback + "(" + jsonData +")";  
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	@RequestMapping("/all")
	@ResponseBody
	public Object findItemCatAll(String callback) {
		ItemCatResult catResult = itemCatService.findCacheItemCatAll();
		MappingJacksonValue jacksonValue = new MappingJacksonValue(catResult);
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	@RequestMapping("/findItemCatById/{itemCatId}")
	@ResponseBody
	public ItemCat findItemCatById(@PathVariable Long itemCatId) {
		ItemCat cat = itemCatService.findItemCatById(itemCatId);
		return cat;
	}
	
	
}
