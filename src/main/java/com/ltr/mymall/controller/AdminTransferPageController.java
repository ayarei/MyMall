package com.ltr.mymall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 这个控制器用于管理员页面的登陆跳转
 * @author 刘添瑞
 *
 */
@Controller
@RequestMapping("")
public class AdminTransferPageController {

	@RequestMapping("admin_register")
	public String adminRegister() {
		return "admin/registerPage";
	}
	
	@RequestMapping("admin_registerSuccessPage")
	public String adminRegisterSuccess() {
		return "admin/admin_registerSuccessPage";
	}
}
