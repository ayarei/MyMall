package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.pojo.OrderItem;

public interface OrderService {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";
 
    public void add(Order c);
 
    public float add(Order c, List<OrderItem> ois);
    public void delete(int id);
    public void update(Order c);
    public Order get(int id);
    public List<Order> list();
    // TODO 添加更新库存操作，使用乐观锁实现并发控制
    // TODO 实现乐观锁重试机制
}
