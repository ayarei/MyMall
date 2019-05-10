package com.ltr.mymall.pojo;

import java.util.List;

public class Category {
    private Integer id;

    private String name;

    /**
     * 在前台分类列表中，一个分类列表下有其他分类列表
     * 所以需要这两个字段
     */
    private List<Product> products;
    
    private List<List<Product>> productsByRow;
    
    public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<List<Product>> getProductsByRow() {
		return productsByRow;
	}

	public void setProductsByRow(List<List<Product>> productByRow) {
		this.productsByRow = productByRow;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}