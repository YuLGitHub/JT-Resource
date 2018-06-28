package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{
	
	
	/**
	 * MyBatis的接口中可以添加注解，完成特定的操作
	 * 说明：
	 * 	MyBatis中的注解根据映射标签后期开发的
	 * 	功能上与映射文件一致
	 * @return
	 */
	
	List<Item> findItemAll();

	// 查询记录总数
	int findItemCount();

	/**
	 * 说明：Mybatis原生不支持多值的传递，需要传递多值，则需要对数据进行封装
	 * 1.使用对象进行数据封装（一般在新增和修改时使用）
	 * 2.使用Map<k,v>进行数据封装（其他使用Map封装）
	 * @param begin
	 * @param rows
	 * @return
	 */
	// 根据分页实现数据查询
	List<Item> findItemByPage(@Param("begin") int begin, @Param("rows") Integer rows);

	// 查询商品分类的名称
	String findItemCatName(Long itemCatId);

	void updateStatus(@Param("status")int status, @Param("ids")Long[] ids);

	
	
}
