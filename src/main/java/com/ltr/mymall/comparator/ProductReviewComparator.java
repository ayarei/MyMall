package com.ltr.mymall.comparator;

import java.util.Comparator;

import com.ltr.mymall.pojo.Product;

public class ProductReviewComparator implements Comparator<Product>{

	/**
	 * 将商品按照评论数降序排列
	 */
	@Override
	public int compare(Product o1, Product o2) {
		
		return o2.getReviewCount() - o1.getReviewCount();
	}
	
	
}