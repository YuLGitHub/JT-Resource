package com.jt.web.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemCat;
import com.jt.web.pojo.ItemDesc;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private HttpClientService httpClientService;

	@Autowired
	private JedisCluster jedisCluster;

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Value("${redis.item.key}")
	private String classify;
	/**
	 * 京淘前台的业务层调用后台的业务代码 解决方式：跨域
	 */
	@Override
	public Item findItemById(Long itemId) {

		// 添加缓存
		String key = classify + itemId;
		String resultData = jedisCluster.get(key);
		try {
			if (StringUtils.isEmpty(resultData)) {
				String uri = "http://manage.jt.com/web/item/findItemById/" + itemId;
				String jsonData = httpClientService.doGet(uri);
				if (!StringUtils.isEmpty(jsonData)) {
					// jsonz转为对象 
					Item item = objectMapper.readValue(jsonData, Item.class);
					
					// 将数据写入redis
					String itemJSON = objectMapper.writeValueAsString(item);
					jedisCluster.set(key, itemJSON);
					return item;
				}
			} else {
				Item item = objectMapper.readValue(resultData, Item.class);
				return item;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		try {
			String uri = "http://manage.jt.com/web/itemCat/findItemCatById/" + itemCatId;
			String jsonData = httpClientService.doGet(uri);
			if (!StringUtils.isEmpty(jsonData)) {
				ItemCat data = objectMapper.readValue(jsonData, ItemCat.class);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		try {
			String uri = "http://manage.jt.com/web/itemDesc/findItemDescById/" + itemId;
			String jsonData = httpClientService.doGet(uri);
			if (!StringUtils.isEmpty(jsonData)) {
				ItemDesc itemDesc = objectMapper.readValue(jsonData, ItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
