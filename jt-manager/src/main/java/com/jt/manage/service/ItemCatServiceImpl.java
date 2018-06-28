package com.jt.manage.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.ItemCat;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	private ItemMapper itemMapper;
	/*
	// 注入jedis对象
	@Autowired
	private Jedis jedis;
	*/
	/*
	 *注解原因：替换为JedisCluster
	@Autowired
	private RedisService redisService;
	*/
	
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 使用通用Mapper(JPA),传入的对象最终充当了查询的where条件
	 * 
	 * 总结：ItemCat对象会将不为Null的属性充当where条件，
	 * 如果需要添加查询条件，只需要给对象的属性赋值
	 */
	@Override
	public List<ItemCat> findItemCat() {
		return itemCatMapper.select(null); // 查询所有
	}

	@Override
	public List<ItemCat> findItemCatByParentId(Long parentId) {
		
		// 创建List集合
		List<ItemCat> itemCatList = new ArrayList<ItemCat>();
		// 定义key
		String key = "ITEM_CAT_" + parentId;
		
		// 从缓存中获取数据
//		String jsonData = jedis.get(key);
		String jsonData = jedisCluster.get(key);
		
		try {
			// 判断返回值是否为空
			if (StringUtils.isEmpty(jsonData)) {
				// 查询数据库
				ItemCat itemCat = new ItemCat();
				itemCat.setParentId(parentId);
				itemCat.setStatus(1); // 正常的分类信息
				itemCatList = itemCatMapper.select(itemCat);
				// 将itemCatList集合转化为JSON串
				String resultJSON = objectMapper.writeValueAsString(itemCatList);
				// 将查询的数据存入缓存
				jedisCluster.set(key, resultJSON);
				
			} else {
				// 若缓存中有值，则取出并返回一个list集合
				ItemCat[] itemCats = objectMapper.readValue(jsonData, ItemCat[].class);
				itemCatList = Arrays.asList(itemCats); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemCatList;
	}

	@Override
	public ItemCatResult findItemCatAll() {
		// 查询全部商品分类信息
		ItemCat tempItemCat = new ItemCat();
		tempItemCat.setStatus(1); // 查询正常的商品分类信息
		List<ItemCat> itemCatLists = itemCatMapper.select(tempItemCat);
		
		// 整理数据
		Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
		for (ItemCat itemCat : itemCatLists) {
			if (itemCatMap.containsKey(itemCat.getParentId())) {
				// 表示已经含有parentId,做数据追加操作
				itemCatMap.get(itemCat.getParentId()).add(itemCat);
			} else {
				// 为父级创建List集合
				List<ItemCat> itemCatList = new ArrayList<ItemCat>();
				itemCatList.add(itemCat);
				itemCatMap.put(itemCat.getParentId(), itemCatList);
			}
		}
		
		// 构建返回的对象
		ItemCatResult itemCatResult = new ItemCatResult();
		// 封装一级商品分类菜单
		List<ItemCatData> itemCatDataList = new ArrayList<ItemCatData>();
		for (ItemCat itemCat1 : itemCatMap.get(0L)) {
			// 一级商品分类对象
			ItemCatData itemCatData1 = new ItemCatData();
			itemCatData1.setUrl("/products/" + itemCat1.getId() + ".html");
			itemCatData1.setName("<a href='" + itemCatData1.getUrl() + "'>"+ itemCat1.getName() +"</a>");
			
			// 封装二级菜单
			List<ItemCatData> itemCatDataList2 = new ArrayList<ItemCatData>();
			for (ItemCat itemCat2 : itemCatMap.get(itemCat1.getId())) {
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/" + itemCat2.getId());
				itemCatData2.setName(itemCat2.getName());
				
				// 准备三级商品分类
				List<String> itemCatDataList3 = new ArrayList<String>();
				// 将三级菜单注入到二级中
				for (ItemCat itemCat3 : itemCatMap.get(itemCat2.getId())) {
					itemCatDataList3.add("/products/" + itemCat3.getId() + "|" + itemCat3.getName());
				}
				
				itemCatData2.setItems(itemCatDataList3);
				itemCatDataList2.add(itemCatData2);
			}
			
			itemCatData1.setItems(itemCatDataList2);
			itemCatDataList.add(itemCatData1);
			if (itemCatDataList.size() > 13) {
				break;
			}
		}
		
		itemCatResult.setItemCats(itemCatDataList);
		return itemCatResult;
	}
	
	// 实现三级商品分类缓存操作
	/**
	 * 1.先查缓存
	 * 2.没有缓存，执行业务操作查询
	 * 3.将查询结果返回，将结果存入缓存中
	 * 4.缓存中含有该数据
	 * 5.将缓存数据转化为对象返回，满足编程规范
	 * @return
	 */
	@Override
	public ItemCatResult findCacheItemCatAll() {
		String key = "ITEM_CAT_ALL";
		String jsonData = jedisCluster.get(key);
		try {
			// 判断数据是否为空
			if (StringUtils.isEmpty(jsonData)) {
				ItemCatResult itemCatResult = findItemCatAll();
				String resultJSON = objectMapper.writeValueAsString(itemCatResult);
				// 将数据存入redis
				jedisCluster.set(key, resultJSON);
				return itemCatResult;
			} else {
				ItemCatResult itemCatResult = objectMapper.readValue(jsonData, ItemCatResult.class);
				return itemCatResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		return itemCatMapper.selectByPrimaryKey(itemCatId);
	}

}
