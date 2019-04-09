package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.PropertyValue;

public interface PropertyValueService {
	
	public void init(Product p);
	public void update(PropertyValue pv);
 
	public void delete(int pid);
	public PropertyValue get(int ptid, int pid);
	public List<PropertyValue> list(int pid);
}
