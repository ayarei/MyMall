package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.OrderItemMapper;
import com.ltr.mymall.mapper.OrderMapper;
import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.pojo.OrderItem;
import com.ltr.mymall.pojo.OrderItemExample;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.service.OrderItemService;
import com.ltr.mymall.service.ProductService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	ProductService productService;

	@Autowired
	OrderItemMapper orderItemMapper;

	@Override
	public void add(OrderItem c) {
		orderItemMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		orderItemMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(OrderItem c) {
		orderItemMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public OrderItem get(int id) {
		OrderItem result = orderItemMapper.selectByPrimaryKey(id);
		return result;
	}

	@Override
	public List<OrderItem> list() {
		OrderItemExample example = new OrderItemExample();
		example.setOrderByClause("id desc");
		return orderItemMapper.selectByExample(example);
	}

	/**
	 * MyBatis的逆向工程没有一对多的关系
	 * 所以这里使用集合进行二次开发
	 */
	@Override
	public void fill(List<Order> os) {
		for (Order o : os) {
			fill(o);
		}
	}

	/**
	 * 计算总价、商品总数、商品列表,并将结果返还给Order
	 */
	@Override
	public void fill(Order o) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andOidEqualTo(o.getId());
		example.setOrderByClause("id desc");
		List<OrderItem> orderItemList = orderItemMapper.selectByExample(example);
		setProduct(orderItemList);

		float totalPrice = 0;
		int totalNumber = 0;
		for (OrderItem e : orderItemList) {
			totalNumber = totalNumber + e.getNumber();
			totalPrice = totalPrice + e.getNumber() * e.getProduct().getPromotePrice();
		}

		o.setTotal(totalPrice);
		o.setTotalNumber(totalNumber);
		o.setOrderItems(orderItemList);
	}

	public void setProduct(List<OrderItem> ois) {
		for (OrderItem e : ois) {
			setProduct(e);
		}
	}

	private void setProduct(OrderItem oi) {
		Product product = productService.get(oi.getPid());
		oi.setProduct(product);
	}

	/**
	 * 计算销量
	 */
	@Override
	public int getSaleCount(int pid) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andPidEqualTo(pid).andOidIsNotNull();
		List<OrderItem> ois = orderItemMapper.selectByExample(example);
		int count = 0;
		for (OrderItem e : ois) {
			count += e.getNumber();
		}
		return count;
	}

	//查找用户购物车中所有产品
	@Override
	public List<OrderItem> listByUser(int uid) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andUidEqualTo(uid).andOidIsNull();
		List<OrderItem> result  =orderItemMapper.selectByExample(example);
		setProduct(result);
		return result;			
	}
}
