package com.ltr.mymall.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ltr.mymall.mapper.CategoryMapper;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	CategoryMapper categoryMapper;
	
	public List<Category> list() {
		return categoryMapper.list();
	};

}
