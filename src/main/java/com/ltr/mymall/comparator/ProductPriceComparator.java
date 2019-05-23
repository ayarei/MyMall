package com.ltr.mymall.comparator;

import java.util.Comparator;

import com.ltr.mymall.pojo.Product;

public class ProductPriceComparator implements Comparator<Product>{

	/**
	 * 将商品按照促销价升序排列
	 */
	@Override
	public int compare(Product o1, Product o2) {
		return (int) (o1.getPromotePrice() - o2.getPromotePrice());
	}
	

}
