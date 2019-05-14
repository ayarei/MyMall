package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.UserMapper;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.pojo.UserExample;
import com.ltr.mymall.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Override
	public void add(User user) {
		userMapper.insert(user);

	}

	@Override
	public void delete(int id) {
		userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(User user) {
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public User get(int id) {

		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<User> list() {
		UserExample example = new UserExample();
		example.setOrderByClause("id desc");
		return userMapper.selectByExample(example);
	}

	@Override
	public boolean isExist(String name) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(name);
		List<User> userList = userMapper.selectByExample(example);
		if (userList.isEmpty()) {
			return false;
		}
		return true;

	}

	@Override
	public User get(String name, String password) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
		
		List<User> userList = userMapper.selectByExample(example);
		
		if(userList.isEmpty()) {
			return null;
		}
		return userList.get(0);
	}

}


















