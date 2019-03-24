package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.PropertyMapper;
import com.ltr.mymall.pojo.Property;
import com.ltr.mymall.pojo.PropertyExample;
import com.ltr.mymall.service.PropertyService;

@Service
public class PropertyServiceImpl implements PropertyService{

	@Autowired
	PropertyMapper propertyMapper;
	
	/**
	 * cid是具体分类的外键
	 */
	@Override
	public List<Property> list(int cid) {
		PropertyExample example = new PropertyExample();
		example.createCriteria().andCidEqualTo(cid);
		example.setOrderByClause("id desc");
		return propertyMapper.selectByExample(example);
	}

	@Override
	public void add(Property property) {
		propertyMapper.insert(property);
	}

	@Override
	public void delete(int id) {
		propertyMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public Property get(int id) {	
		return propertyMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(Property property) {
		propertyMapper.updateByPrimaryKeySelective(property);	
	}

}
