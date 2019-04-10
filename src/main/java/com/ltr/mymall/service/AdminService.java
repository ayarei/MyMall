package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Admin;


public interface AdminService {
	public void add(Admin admin);
	public void delete(int id);
	public void update(Admin admin);
	
	public Admin get(int id);
	public List<Admin> list();
	
	public boolean isExist(String name);

	public Admin get(String name,String password);
}
