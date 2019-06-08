package com.ltr.mymall.dto;

import java.util.List;

// 封装创建订单的后的结果
public class CreateOrderExecution {

	private float total;
	private List<Integer> noStockID;
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public List<Integer> getNoStockID() {
		return noStockID;
	}
	public void setNoStockID(List<Integer> noStockID) {
		this.noStockID = noStockID;
	}
	
	public CreateOrderExecution(float total, List<Integer> noStockID) {
		this.total = total;
		this.noStockID = noStockID;
	}
	
}
