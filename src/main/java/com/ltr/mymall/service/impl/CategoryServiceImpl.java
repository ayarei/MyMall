package com.ltr.mymall.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ltr.mymall.mapper.CategoryMapper;
import com.ltr.mymall.mapper.cache.CategoryRedis;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.CategoryExample;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.util.Page;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	CategoryMapper categoryMapper;
	@Autowired
	CategoryRedis categoryRedis;

	@Override
	public List<Category> list() {	
		CategoryExample example =new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
	}	
	
	@Override
	public void add(Category category) {
		categoryMapper.insert(category);	
		categoryRedis.clearAllList();
	}

	@Override
	public void delete(int id) {
		categoryMapper.deleteByPrimaryKey(id);
		categoryRedis.clearAllList();
		Category test = categoryRedis.getCategory(id);
		if(test != null) {
			categoryRedis.clear(id);
		}
	}

	/**
	 * 首先访问Redis缓存
	 */
	@Override
	public Category get(int id) {
		Category result = categoryRedis.getCategory(id);
		if(result == null) {
			result = categoryMapper.selectByPrimaryKey(id);
			categoryRedis.putCategory(result);
		}
		return result;
	}

	@Override
	public void update(Category category) {
		categoryMapper.updateByPrimaryKeySelective(category);
		// 如果Redis中存在此对象则移除
		categoryRedis.clearAllList();
		Category test = categoryRedis.getCategory(category.getId());
		if(test != null) {
			categoryRedis.clear(category.getId());
		}	
	}
}
