package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.OrderMapper;
import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.pojo.OrderExample;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.OrderService;
import com.ltr.mymall.service.UserService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	UserService userService;
	
	@Override
	public void add(Order c) {
		orderMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		orderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Order c) {
		orderMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public Order get(int id) {	
		return orderMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Order> list() {
		OrderExample example = new OrderExample();
		example.setOrderByClause("id desc");
		List<Order> result = orderMapper.selectByExample(example);
		setUser(result);
		return result;
	}

	public void setUser(List<Order> os) {
		for (Order o : os)
			setUser(o);
	}
    public void setUser(Order o){
        int uid = o.getUid();
        User u = userService.get(uid);
        o.setUser(u);
    }
}
















