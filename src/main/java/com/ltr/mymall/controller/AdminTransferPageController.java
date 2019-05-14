package com.ltr.mymall.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ltr.mymall.pojo.Admin;

/**
 * 后台管理员注册登录跳转控制器
 * @author 刘添瑞
 *
 */
@Controller
@RequestMapping("")
public class AdminTransferPageController {

	@RequestMapping("adminRegisterPage")
	public String adminRegister() {
		return "admin/registerPage";
	}
	
	/**
	 * 访问登录页面时查看session是否有admin的对象
	 * 如果有则直接跳转到此admin的后台
	 * 如果没有则进行登录 
	 */
	@RequestMapping("adminLoginPage")
	public String adminLogin(HttpServletRequest request,
												HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		Admin admin = (Admin) session.getAttribute("admin");
		if(null != admin) {
			response.sendRedirect("admin_category_list");
		}
		return "admin/loginPage";
	}
	
	@RequestMapping("adminRegisterSuccessPage")
	public String adminRegisterSuccess() {
		return "admin/admin_registerSuccessPage";
	}
}
