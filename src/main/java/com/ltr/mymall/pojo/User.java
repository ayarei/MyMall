package com.ltr.mymall.pojo;

public class User {
    private Integer id;

    private String name;

    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
    
    /**
     * 支持匿名评价
     * 匿名部分用“*”代替
     */
    public String getAnonymousName() {
    	if(null == name) {
    		return null;
    	}
    	
    	if(name.length()<=1) {
    		return "*";
    	}
    	
    	if(name.length()==2) {
    		return name.substring(0,1) + "*";
    	}
    	
    	char[] anonymousName = new char[6];
    	anonymousName[0] = name.charAt(0);
    	anonymousName[5] = name.charAt(name.length()-1);
    	for(int i = 1; i < 5; i++) {
    		anonymousName[i] = '*';
    	}
    	return new String(anonymousName);
    }
}

















