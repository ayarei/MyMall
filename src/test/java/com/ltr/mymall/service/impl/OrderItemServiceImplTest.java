package com.ltr.mymall.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ltr.mymall.pojo.OrderItem;
import com.ltr.mymall.service.OrderItemService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class OrderItemServiceImplTest {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	OrderItemService orderItemService;
	
	@Test
	public void testListByUser() {
		int uid = 5;
		List<OrderItem> list = new ArrayList<OrderItem>();
		list = orderItemService.listByUser(uid);
		//logger.info(list);
		for(OrderItem e : list) {
			System.out.print(e.getId()+" ");
			System.out.print(e.getPid()+" ");
			System.out.print(e.getNumber()+" ");
			System.out.println();
		}
	}

}
