package com.ltr.mymall.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ltr.mymall.mapper.ProductMapper;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ProductServiceImplTest {

	@Autowired
	ProductService productService;
	// 测试多线程下更新库存的一致性
	@Test
	public void testUpdateStock() {
		// fail("Not yet implemented");
		Product product1 = productService.get(999);
		Product product2 = productService.get(999);
		int buyNumber = 3;
		
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				
				int ret1 = productService.updateStock(product1, buyNumber);
				System.out.println(ret1);
			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				int ret2 = productService.updateStock(product2, buyNumber);
				System.out.println(ret2);
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t1.start();
		t2.start();
	}
	
	@Test
	public void test1() {
		for(int i = 0; i < 3;i++) {
			Product product =productService.normalGet(999);
			System.out.println(product.getStock());
		}
		
	}

	
}
