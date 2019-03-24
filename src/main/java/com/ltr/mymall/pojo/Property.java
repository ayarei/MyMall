package com.ltr.mymall.pojo;

//物品属性的pojo
public class Property {
    private Integer id;

    private Integer cid;

    private String name;

    /*非自动生成*/
    private Category category;
    
    /*非自动生成*/
    public void setCategory(Category category) {
    	this.category = category;
    }
    
    /*非自动生成*/
    public Category getCategory() {
    	return category;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}