package com.ltr.mymall.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ltr.mymall.mapper.CategoryMapper;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.util.Page;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	CategoryMapper categoryMapper;
	
	public List<Category> list(Page page) {
		return categoryMapper.list(page);
	}

	@Override
	public int total() {
	
		return categoryMapper.total();
	};

}
