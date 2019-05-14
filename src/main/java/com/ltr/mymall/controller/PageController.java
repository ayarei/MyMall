package com.ltr.mymall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前台用户注册、登录跳转控制器
 * @author 刘添瑞
 *
 */
@RequestMapping("")
@Controller
public class PageController {

	@RequestMapping("registerPage")
	public String registerPage() {
		return "fore/register";
	}
	
	@RequestMapping("registerSuccessPage")
	public String registerSuccessPage() {
		return "fore/registerSuccess";
	}
	
	@RequestMapping("loginPage")
	public String loginPage() {
		return "fore/login";
	}
	
	@RequestMapping("forealipay")
	public String alipay() {
		return "fore/alipay";
	}
	
}
