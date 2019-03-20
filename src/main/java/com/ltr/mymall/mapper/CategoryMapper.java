package com.ltr.mymall.mapper;

import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.util.Page;

import java.util.List;

public interface CategoryMapper {
	public List<Category> list(Page page);
	
	public int total();
	
	public void add(Category category);
	
	
}
