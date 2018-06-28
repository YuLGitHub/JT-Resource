package com.jt.search.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.search.DubboSearchService;
import com.jt.duboo.pojo.Item;

public class SearchServiceImpl implements DubboSearchService {

	// 利用全文检索进行查询
	@Autowired
	private HttpSolrServer httpSolrServer;
	
	@Override
	public List<Item> findItemListByKey(String keyword, Integer page) {
		
		// 准备全文检索对象
		SolrQuery query = new SolrQuery(keyword);
		int rows = 20; // 每次20行
		if (page == null) {
			page = 1;
		}
		int start = (page-1) * rows;
		// 分页设置
		query.setStart(start); // 设置初始位置
		query.setRows(rows); 
		
		try {
			// 获取一个返回值对象
			QueryResponse response = httpSolrServer.query(query);
			// 获取返回值结果
			List<Item> itemList = response.getBeans(Item.class);
			return itemList;
			
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*@Override
	public List<Item> findItemListByCid(Long itemCatId) {
		SolrQuery query = new SolrQuery("cid:" + itemCatId);
		query.setStart(0); // 设置初始位置
		query.setRows(20); 
		try {
			QueryResponse response = httpSolrServer.query(query);
			// 获取返回值结果
			List<Item> itemList = response.getBeans(Item.class);
			return itemList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
