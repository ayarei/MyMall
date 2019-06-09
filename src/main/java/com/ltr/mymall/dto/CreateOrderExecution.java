package com.ltr.mymall.dto;

import java.util.List;

// 封装创建订单的后的结果
public class CreateOrderExecution {

	// 若订单正常返回总价，此时noStockID为null
	// 若发生并发减库存失败则total为-1
	// 若发生库存不够的情况，则total为-2,此时noStockID为储存不够商品ID的集合
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
