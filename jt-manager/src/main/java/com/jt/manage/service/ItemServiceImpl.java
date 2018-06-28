package com.jt.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	// 查询所有的商品信息
	@Override
	public List<Item> findItemAll() {
		return itemMapper.findItemAll();
	}

	@Override
	public EasyUIResult findItemByPage(Integer page, Integer rows) {
		// 获取记录总数
		int total = itemMapper.findItemCount();
		
		// 计算分页的起始位置
		int begin = (page - 1) * rows;

		List<Item> itemList = itemMapper.findItemByPage(begin, rows);
		return new EasyUIResult(total, itemList);
	}

	@Override
	public String findItemCatName(Long itemCatId) {
		return itemMapper.findItemCatName(itemCatId);
	}

	@Override
	public void saveItem(Item item, String desc) {
		
		// 补齐item数据
		item.setStatus(1); // 启用
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		
		// 利用通用mapper实现入库操作
		itemMapper.insert(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public void updateItem(Item item, String desc) {
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item); // 动态更新
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		
		itemDescMapper.updateByPrimaryKeySelective(itemDesc); // 动态更新
		
		// 当对象数据进行修改时，应该将redis的缓存数据删除
		jedisCluster.del("ITEM_" + item.getId());
	}
	
	/**
	 * 删除操作时，应该删除从表的数据，然后删除主表的数据
	 */
	@Override
	public void deleteItems(Long[] ids) {
		// 从表
		itemDescMapper.deleteByIDS(ids);
		// 主表
		itemMapper.deleteByIDS(ids);
		// 删除缓存操作
		for (Long id : ids) {
			jedisCluster.del("ITEM_" + id);
		}
	}

	@Override
	public void updateStatus(int status, Long[] ids) {
		/**
		 * 方案一
		 */
		/*
		for(Long id : ids) {
			Item item = new Item();
			item.setId(id);
			item.setStatus(status);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		*/
		/**
		 * 方案2
		 */
		itemMapper.updateStatus(status,ids);
		
		
	}

	
	/**
	 * 查询商品描述信息
	 */
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		Item item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}
	
	

}
