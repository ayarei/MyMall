package com.ltr.mymall.service;

import java.util.List;

import com.ltr.mymall.pojo.Review;

public interface ReviewService {
	
	public void add(Review review);
	public void delete(int id);
	public void update(Review review);
	public List<Review> list(int pid);
	
	public Review get(int id);
	public int getCount(int pid);
}
