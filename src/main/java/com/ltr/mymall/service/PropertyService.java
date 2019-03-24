package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Property;

public interface PropertyService {

		/**
		 * 根据外键指向相应分类下的属性列表
		 * @param cid 具体分类的外键
		 * @return
		 */
		public List<Property> list(int cid);
		
		//根据表单提供的数据添加
		public void add(Property property);
		
		//根据id删除相应元素
		public void delete(int id);
		
		//根据id获取相应元素
		public Property get(int id);
		
		//更新内容
		public void update(Property property);
}
