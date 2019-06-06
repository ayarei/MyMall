package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ltr.mymall.mapper.OrderMapper;
import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.pojo.OrderExample;
import com.ltr.mymall.pojo.OrderItem;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.OrderItemService;
import com.ltr.mymall.service.OrderService;
import com.ltr.mymall.service.UserService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderMapper orderMapper;	
	@Autowired
	UserService userService;	
	@Autowired
	OrderItemService orderItemService;
	
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

    /**
     * 将商品ois加入订单c，使用声明式事务
     * 失败则回滚
     */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
	public float add(Order c, List<OrderItem> ois) {
		float total = 0;
		add(c);
		
		// TODO:循环执行SQL，待改进
		for(OrderItem e : ois) {
			e.setOid(c.getId());
			orderItemService.update(e);
			total += e.getProduct().getPromotePrice()*e.getNumber();
		}

		return total;
	}
}
