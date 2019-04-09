package com.ltr.mymall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.UserService;
import com.ltr.mymall.util.Page;

/**
 * 后台管理员只允许查看注册的用户 
 * 没有修改、删除的权限
 * 
 * @author 刘添瑞
 *
 */
@Controller
@RequestMapping("")
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping("admin_user_list")
	public String list(Model model, Page page) {
		PageHelper.offsetPage(page.getStart(), page.getCount());

		List<User> user = userService.list();

		int total = (int) new PageInfo<>(user).getTotal();
		page.setTotal(total);

		model.addAttribute("user", user);
		model.addAttribute("page", page);

		return "admin/listUser";
	}
}
