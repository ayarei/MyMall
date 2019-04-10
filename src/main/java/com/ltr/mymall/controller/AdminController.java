package com.ltr.mymall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import com.ltr.mymall.pojo.Admin;
import com.ltr.mymall.service.AdminService;


@Controller
@RequestMapping("")
public class AdminController {

	@Autowired
	AdminService AdminService;
	
	//MD5盐值
	private final String slat = "jkdo%@*gFG%^jkG637^%UJ@fxHhsd124$%&*$TUacdF";
	
	@RequestMapping("adminregister")
	public String register(Model model, Admin admin,String special) {
		if(!special.equals("ltr")) {
			String message ="注册码错误，请填写正确的注册码";
	        model.addAttribute("msg", message);
	        model.addAttribute("user", null);
	        return "admin/registerPage";
		}
		String name = admin.getName();
		//名字转义，防止注入攻击
		name = HtmlUtils.htmlEscape(name);
		admin.setName(name);
		boolean exist = AdminService.isExist(name);
		if(exist) {
			String message ="管理员用户名已经被使用,不能使用";
	        model.addAttribute("msg", message);
	        model.addAttribute("user", null);
	        return "admin/registerPage";
		}
		String base = admin.getPassword() + slat; 
		admin.setPassword(DigestUtils.md5DigestAsHex(base.getBytes()));
		AdminService.add(admin);
		return "redirect:admin_registerSuccessPage";
	}
	
	
    @RequestMapping("admin_loginPage")
    public String loginPage() {
        return "admin/login";
    }
}
