package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Order;

public interface OrderService {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";
 
    public void add(Order c);
 
    public void delete(int id);
    public void update(Order c);
    public Order get(int id);
    public List<Order> list();
}
