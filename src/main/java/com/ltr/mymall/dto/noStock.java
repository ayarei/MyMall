package com.ltr.mymall.dto;

import com.ltr.mymall.pojo.Product;

// 用于向前台“库存不够”页面传送数据
// 封装了库存不够的商品及用户购买对应商品的数量
public class noStock {

	private  Product noStockProduct;
	private int buyNumber;
	
	public noStock(Product noStockProduct, int buyNumber) {
		this.noStockProduct = noStockProduct;
		this.buyNumber = buyNumber;
	}
	
	public Product getNoStockProduct() {
		return noStockProduct;
	}

	public void setNoStockProduct(Product noStockProduct) {
		this.noStockProduct = noStockProduct;
	}

	public int getBuyNumber() {
		return buyNumber;
	}
	public void setBuyNumber(int buyNumber) {
		this.buyNumber = buyNumber;
	}
	
	
	
	
}
