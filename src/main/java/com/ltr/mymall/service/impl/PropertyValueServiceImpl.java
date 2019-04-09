package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.PropertyValueMapper;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.Property;
import com.ltr.mymall.pojo.PropertyValue;
import com.ltr.mymall.pojo.PropertyValueExample;
import com.ltr.mymall.service.PropertyService;
import com.ltr.mymall.service.PropertyValueService;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

	@Autowired
	PropertyValueMapper propertyValueMapper;

	@Autowired
	PropertyService propertyService;

	/**
	 * 初始化PropertyValue
	 * 
	 * 首先根据产品获取分类，然后获取这个分类下的所有属性集合
	 * 
	 * 然后用属性和id产品id去查询，看看这个属性和这个产品，是否已经存在属性值了。
	 * 
	 * 如果不存在，那么就创建一个属性值，并设置其属性和产品，接着插入到数据库中。
	 * 
	 */
	@Override
	public void init(Product p) {
		List<Property> pts = propertyService.list(p.getCid());

		for (Property pt : pts) {
			PropertyValue pv = get(pt.getId(), p.getId());
			if (null == pv) {
				pv = new PropertyValue();
				pv.setPid(p.getId());
				pv.setPtid(pt.getId());
				propertyValueMapper.insert(pv);
			}
		}
	}

	@Override
	public void update(PropertyValue pv) {

		propertyValueMapper.updateByPrimaryKeySelective(pv);
	}

	@Override
	public PropertyValue get(int ptid, int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);
		List<PropertyValue> pvs = propertyValueMapper.selectByExample(example);
		if (pvs.isEmpty())
			return null;
		return pvs.get(0);
	}

	@Override
	public List<PropertyValue> list(int pid) {
		PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> result = propertyValueMapper.selectByExample(example);
        for (PropertyValue pv : result) {
            Property property = propertyService.get(pv.getPtid());
            pv.setProperty(property);
        }
        return result;
	}

	@Override
	public void delete(int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPidEqualTo(pid);
		List<PropertyValue> result = propertyValueMapper.selectByExample(example);
		for(PropertyValue pv : result) {
			propertyValueMapper.deleteByPrimaryKey(pv.getId());
		}
	}

}















