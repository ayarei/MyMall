package com.ltr.mymall.mapper;

import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.util.Page;

import java.util.List;

public interface CategoryMapper {
	 //分页查询
	public List<Category> list(Page page);
	
	//获取数据总数
	public int total();
	
	//根据表单提供的数据添加
	public void add(Category category);
	
	//根据id删除相应元素
	public void delete(int id);
	
	//根据id获取相应元素
	public Category get(int id);
	
	//更新内容
	public void update(Category category);
}
