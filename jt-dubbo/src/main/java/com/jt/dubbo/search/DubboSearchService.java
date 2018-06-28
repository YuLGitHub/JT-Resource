package com.jt.dubbo.search;

import java.util.List;

import com.jt.duboo.pojo.Item;

public interface DubboSearchService {

	List<Item> findItemListByKey(String keyword, Integer page);

//	List<Item> findItemListByCid(Long itemCatId);

}
