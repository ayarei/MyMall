package com.ltr.mymall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.ProductImageMapper;
import com.ltr.mymall.mapper.ProductMapper;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.ProductExample;
import com.ltr.mymall.pojo.ProductImage;
import com.ltr.mymall.pojo.ProductImageExample;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.OrderItemService;
import com.ltr.mymall.service.ProductImageService;
import com.ltr.mymall.service.ProductService;
import com.ltr.mymall.service.PropertyValueService;
import com.ltr.mymall.service.ReviewService;

/**
 * 
 * 对于每个查询出来的Product都会带上相应的分类 pojo.Product.java中需要加上Category成员变量
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	volatile ProductMapper productMapper;
	
	@Autowired
	volatile PropertyValueService propertyValueService;
	
	@Autowired
	volatile CategoryService categoryService;
	
	@Autowired
	volatile ProductImageService productImageService;
	
	@Autowired
	volatile ProductImageMapper productImageMapper;
	
	@Autowired
	OrderItemService orderItemService;
	
	@Autowired
	ReviewService reviewService;

	@Override
	public void add(Product p) {
		productMapper.insert(p);
	}

	/**
	 * 因为有外键的约束，不能够直接根据产品主键id删除产品 
	 * 必须把产品下的外键约束都删除 
	 * 包括图片和属性值
	 * volatile用于防止指令重排，删除产品记录必须在最后执行
	 * 此处只删除数据库记录
	 * ProductController中删除图片文件
	 */
	@Override
	public void delete(int id) {
		/**
		 * 删除产品所有属性值记录
		 */
		propertyValueService.delete(id);

		/**
		 * 删除产品所有图片数据库记录
		 */
		ProductImageExample example = new ProductImageExample();
		example.createCriteria().andPidEqualTo(id);
		List<ProductImage> result = productImageMapper.selectByExample(example);
		for(ProductImage pi : result) {
			productImageService.delete(pi.getId());
		}
		
		/**
		 * 最后删除产品本身记录
		 */
		productMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Product p) {
		productMapper.updateByPrimaryKeySelective(p);
	}

	@Override
	public Product get(int id) {
		Product p = productMapper.selectByPrimaryKey(id);
		setFirstProductImage(p);
		setCategory(p);
		return p;
	}

	public void setCategory(List<Product> ps) {
		for (Product p : ps)
			setCategory(p);
	}

	public void setCategory(Product p) {
		int cid = p.getCid();
		Category c = categoryService.get(cid);
		p.setCategory(c);
	}

	@Override
	public List<Product> list(int cid) {
		ProductExample example = new ProductExample();
		example.createCriteria().andCidEqualTo(cid);
		example.setOrderByClause("id desc");
		List<Product> result = productMapper.selectByExample(example);
		setCategory(result);
		setFirstProductImage(result);
		return result;
	}

	@Override
	public void setFirstProductImage(Product p) {
		List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
		if (!pis.isEmpty()) {
			ProductImage pi = pis.get(0);
			p.setFirstProductImage(pi);
		}
	}

	public void setFirstProductImage(List<Product> ps) {
		for (Product p : ps) {
			setFirstProductImage(p);
		}
	}

	@Override
	public void fill(List<Category> categories) {
		for(Category c : categories) {
			fill(c);
		}
	}

	@Override
	public void fill(Category category) {
		List<Product> productList = list(category.getId());
		category.setProducts(productList);
	}

	@Override
	public void fillByRow(List<Category> categories) {
		// 八个产品为一行显示
		int productNumberEachRow = 8;
		for (Category c : categories) {
			List<Product> products = c.getProducts();
			List<List<Product>> productsByRow = new ArrayList<>();
			for (int i = 0; i < products.size(); i += productNumberEachRow) {
				int size = i + productNumberEachRow;
				size = size > products.size() ? products.size() : size;
				List<Product> productsOfEachRow = products.subList(i, size);
				productsByRow.add(productsOfEachRow);
			}
			c.setProductsByRow(productsByRow);
		}
	}

	/**
	 * 为产品对象带上评论总数与销量
	 */
	@Override
	public void setReviewAndSaleNumber(Product product) {
		int saleCount = orderItemService.getSaleCount(product.getId());
		int reviewCount = reviewService.getCount(product.getId());
		
		product.setSaleCount(saleCount);
		product.setReviewCount(reviewCount);
	}

	@Override
	public void setReviewAndSaleNumber(List<Product> productList) {
		for(Product e : productList) {
			setReviewAndSaleNumber(e);
		}
	}

	
}









