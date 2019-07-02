package com.ltr.mymall.mapper.cache;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ltr.mymall.mapper.CategoryMapper;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.service.CategoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Spring/applicationContext-Redis.xml","classpath:Spring/applicationContext.xml"})
public class CategoryRedisTest {
	private int id = 60;
	@Autowired
	private CategoryRedis categoryRedis;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	CategoryService categoryService;
	
	@Test
	public void test() {
		// fail("Not yet implemented");
		Category category = categoryRedis.getCategory(id);
		
		if(category == null) {
			category = categoryMapper.selectByPrimaryKey(id);
			if(category != null) {
				String result = categoryRedis.putCategory(category);
				System.out.println(result);
				Category category2 = categoryRedis.getCategory(id);
				System.out.println(category2);
			}
		}else {
			System.out.println(category);
		}
	}
	@Test
	public void test2() {
		Category category = categoryRedis.getCategory(id);
		System.out.println(category);
		categoryRedis.clear(id);
		Category gg = categoryRedis.getCategory(id);
		System.out.println(gg);		
	}
	@Test
	public void test3() {
		List<Category> list = categoryService.list();
		//String test = categoryRedis.putCategory(list,1);
		//System.out.println(test);
	}
	
	@Test
	public void test4() {
		List<Category> list = categoryRedis.getCategoryList(1);
		for(Category e : list) {
			System.out.println(e.getName());
		}
	}
	
	@Test
	public void test5() {
		categoryRedis.clear(64);
	}
}







