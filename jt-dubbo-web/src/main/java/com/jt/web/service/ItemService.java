package com.jt.web.service;

import java.io.File;

import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemCat;
import com.jt.web.pojo.ItemDesc;

public interface ItemService {

	Item findItemById(Long itemId);

	ItemCat findItemCatById(Long itemCatId);

	ItemDesc findItemDescById(Long itemId);


}
