package com.jt.manage.service;

import java.util.List;

import com.jt.common.vo.EasyUIResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

public interface ItemService {
	List<Item> findItemAll();

	EasyUIResult findItemByPage(Integer page, Integer rows);

	String findItemCatName(Long itemCatId);

	void saveItem(Item item, String desc);

	void updateItem(Item item, String desc);

	// 批量删除数据
	void deleteItems(Long[] ids);

	void updateStatus(int status, Long[] ids);


	ItemDesc findItemDescById(Long itemId);

	// 根据id获取对象
	Item findItemById(Long itemId);
}
