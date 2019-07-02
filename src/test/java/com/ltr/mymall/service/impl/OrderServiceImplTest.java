package com.ltr.mymall.service.impl;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ltr.mymall.dto.CreateOrderExecution;
import com.ltr.mymall.exception.OutOfStockException;
import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.pojo.OrderItem;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.service.OrderItemService;
import com.ltr.mymall.service.OrderService;
import com.ltr.mymall.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:Spring/applicationContext.xml")
public class OrderServiceImplTest {

	@Autowired
	OrderService orderService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ProductService productService;

	@Test
	public void testAddOrderListOfOrderItem() {
		// fail("Not yet implemented");
		Order order1 = new Order();
		order1.setOrderCode("201906061725020414457");
		List<OrderItem> ois1 = new ArrayList<OrderItem>();
		OrderItem oi1 = orderItemService.get(39);
		ois1.add(oi1);

		Order order2 = new Order();
		order2.setOrderCode("201906061725020414457");
		List<OrderItem> ois2 = new ArrayList<OrderItem>();
		OrderItem oi2 = orderItemService.get(40);
		ois2.add(oi2);

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				float ret1 = -1;
				CreateOrderExecution excution;
				try {
					while(ret1 == -1) {
						excution = orderService.add(order1, ois1);
						ret1 = excution.getTotal();
						System.out.println(ret1);
						Product p = productService.normalGet(999);
						System.out.println("AfterChange1:" + p.getStock());
					}
					if(ret1 == -2) {
						throw new OutOfStockException("商品售罄"); 
					}
					System.out.println(this.getClass() + "购买成功");
					
				} catch (OutOfStockException e) {
					System.out.println(e.getMessage());
				}

			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				float ret2 = -1;
				CreateOrderExecution excution;
				try {
					while(ret2 == -1) {
						excution = orderService.add(order2, ois2);
						ret2 = excution.getTotal();
						System.out.println(ret2);
						Product p = productService.normalGet(999);
						System.out.println("AfterChange2:" + p.getStock());
					}	
					if(ret2 == -2) {
						throw new OutOfStockException("商品售罄"); 
					}
					System.out.println(this.getClass() + "购买成功");
				} catch (OutOfStockException e) {
					System.out.println(e.getMessage());
				}

			}
		});		
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
