package com.ltr.mymall.comparator;

import java.util.Comparator;

import com.ltr.mymall.pojo.Product;

public class ProductDateComparator implements Comparator<Product>{

	/**
	 * 将商品按照上架时间降序排列
	 */
	@Override
	public int compare(Product o1, Product o2) {
		return o2.getCreateDate().compareTo(o1.getCreateDate());
	}
	

}
