package com.ltr.mymall.service;

import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.util.Page;

import java.util.List;

public interface CategoryService {
	public List<Category> list(Page page);
	
	public int total();
}
