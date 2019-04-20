package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.AdminMapper;
import com.ltr.mymall.pojo.Admin;
import com.ltr.mymall.pojo.AdminExample;
import com.ltr.mymall.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminMapper adminMapper;

	@Override
	public void add(Admin admin) {
		adminMapper.insert(admin);
	}

	@Override
	public void delete(int id) {
		adminMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Admin admin) {
		adminMapper.updateByPrimaryKeySelective(admin);
	}

	@Override
	public Admin get(int id) {

		return adminMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Admin> list() {
		AdminExample example = new AdminExample();
		example.setOrderByClause("id desc");
		return adminMapper.selectByExample(example);
	}

	@Override
	public boolean isExist(String name) {
		AdminExample example = new AdminExample();
		example.createCriteria().andNameEqualTo(name);
		List<Admin> result = adminMapper.selectByExample(example);
		// result不空，说明已经有同名用户
		if (!result.isEmpty())
			return true;
		return false;
	}

	/**
	 * 登录验证
	 */
	@Override
	public Admin get(String name, String password) {

		AdminExample example = new AdminExample();
		example.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
		List<Admin> result = adminMapper.selectByExample(example);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

}
