package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.ProductImage;

public interface ProductImageService {
	
	static final String type_single = "type_single";
	static final String type_detail = "type_detail";
    
    public void add(ProductImage pi);
    public void delete(int id);
    public void update(ProductImage pi);
    public ProductImage get(int id);
    public List<ProductImage> list(int pid, String type);
}
