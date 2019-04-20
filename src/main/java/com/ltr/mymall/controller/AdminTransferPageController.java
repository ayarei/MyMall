package com.ltr.mymall.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ltr.mymall.pojo.Admin;

/**
 * 这个控制器用于管理员页面的登陆跳转
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
