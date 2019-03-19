package com.ltr.mymall.pojo;

public class Category {
	//防止id为null
	private Integer id;
	private String name;
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
		
	}
	
	public void setId(Integer id) {
        this.id = id;
    }
	
	public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}
