package com.ltr.mymall.comparator;

import java.util.Comparator;

import com.ltr.mymall.pojo.Product;

public class ProductAllComparator implements Comparator<Product>{
	/**
	 * 将商品按照评论*销量降序排列
	 */
	@Override
	public int compare(Product o1, Product o2) {
		return o2.getReviewCount()*o2.getSaleCount() - o1.getReviewCount()*o2.getReviewCount();
	}
	

}
