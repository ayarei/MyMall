package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.PropertyValue;

public interface PropertyValueService {
	
	void init(Product p);
    void update(PropertyValue pv);
 
    PropertyValue get(int ptid, int pid);
    List<PropertyValue> list(int pid);
}
