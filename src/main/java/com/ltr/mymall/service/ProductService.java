package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Product;

public interface ProductService {

	public void add(Product product);	
	public void delete(int id);	
	public void update(Product product);	
	public Product get(int id);	
	public List<Product> list(int cid);
	public void setFirstProductImage(Product p);
	
	//为分类填充产品集合
	public void fill(List<Category> categories);
	public void fill(Category category);
	//为多个分类填充“推荐产品集合”
	public void fillByRow(List<Category> categories);
}
