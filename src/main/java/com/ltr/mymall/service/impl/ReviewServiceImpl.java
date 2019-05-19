package com.ltr.mymall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltr.mymall.mapper.ReviewMapper;
import com.ltr.mymall.pojo.Review;
import com.ltr.mymall.pojo.ReviewExample;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.ReviewService;
import com.ltr.mymall.service.UserService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	ReviewMapper reviewMapper;
	@Autowired
	UserService userService;
	
	
	@Override
	public void add(Review review) {
		reviewMapper.insert(review);
	}

	@Override
	public void delete(int id) {
		reviewMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Review review) {
		reviewMapper.updateByPrimaryKeySelective(review);
	}

	@Override
	public List<Review> list(int pid) {
		ReviewExample example = new ReviewExample();
		example.createCriteria().andPidEqualTo(pid);
		example.setOrderByClause("id desc");
		List<Review> result = reviewMapper.selectByExample(example);
		setUser(result);
		return result;
	}

	/**
	 * 为每个评论带上用户的属性
	 * @param review
	 */
	public void setUser(List<Review> review) {
		for(Review e : review) {
			setUser(e);
		}
	}
	
	private void setUser(Review review) {
		int uid = review.getUid();
		User user = userService.get(uid);
		review.setUser(user);
	}
	
	@Override
	public Review get(int id) {
		return reviewMapper.selectByPrimaryKey(id);
	}

	/**
	 * 计算某个商品的评论总数
	 */
	@Override
	public int getCount(int pid) {
		return list(pid).size();
	}

}
