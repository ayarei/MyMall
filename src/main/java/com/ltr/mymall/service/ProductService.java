package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Product;

public interface ProductService {

	public void add(Product product);
	public void delete(int id);
	public void update(Product product);
	public Product get(int id);
	public List<Product> list(int cid);
}
