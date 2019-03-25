package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.ProductMapper;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.ProductExample;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.ProductService;
/**
 * 
 * 对于每个查询出来的Product都会带上相应的分类
 * pojo.Product.java中需要加上Category成员变量
 */
@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductMapper productMapper;
	@Autowired
    CategoryService categoryService;
	
	@Override
	public void add(Product product) {
		productMapper.insert(product);
	}

	@Override
	public void delete(int id) {
		productMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Product product) {
		productMapper.updateByPrimaryKeySelective(product);
	}
	
	/*后续前端显示需要通过product定位categry信息*/
	public void setCategory(Product product) {
		int cid = product.getCid();
		Category category = categoryService.get(cid);
		product.setCategory(category);
	}

	/*后续前端显示需要通过product定位categry信息*/
	public void setCategory(List<Product> ps) {
		for(Product e : ps) {
			setCategory(e);
		}
	}
	
	@Override
	public Product get(int id) {
		Product product = productMapper.selectByPrimaryKey(id);
		setCategory(product);
		return product;
	}

	@Override
	public List list(int cid) {
		ProductExample example = new ProductExample();
		example.createCriteria().andCidEqualTo(cid);
		example.setOrderByClause("id desc");
		
		/*为每一个product带上category*/
		/*后续前端显示需要通过product定位categry信息*/
		List productList = productMapper.selectByExample(example);
		setCategory(productList);
		
		return productList;
	}

}














