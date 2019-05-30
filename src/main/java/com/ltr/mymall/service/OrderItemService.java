package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.pojo.OrderItem;

public interface OrderItemService {

	public void add(OrderItem c);

	public void delete(int id);

	public void update(OrderItem c);

	public OrderItem get(int id);

	public List<OrderItem> list();

	public void fill(List<Order> os);

	public void fill(Order o);
	
	public int getSaleCount(int pid);
	
	public List<OrderItem> listByUser(int uid);
}
