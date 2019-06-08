package com.ltr.mymall.dto;

import java.util.List;

import com.ltr.mymall.pojo.Product;

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
