package com.ltr.mymall.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import com.ltr.mymall.pojo.Admin;
import com.ltr.mymall.service.AdminService;

/**
 * @author 刘添瑞
 * 管理员注册与登录验证
 * 使用MD5加密
 * 注册码“ltr”
 */
@Controller
@RequestMapping("")
public class AdminController {

	@Autowired
	AdminService adminService;

	// MD5盐值
	private final String slat = "jkdo%@*gFG%^jkG637^%UJ@fxHhsd124$%&*$TUacdF";
	
	/**
	 * 
	 * @param model
	 * @param admin
	 * @param special  用户填写的注册码
	 * 注册管理员
	 * @return
	 */
	@RequestMapping("adminRegister")
	public String register(Model model, Admin admin, String special) {

		// 注册码
		String registerCode = "ltr";

		if (!special.equals(registerCode)) {
			String message = "注册码错误，请填写正确的注册码";
			model.addAttribute("msg", message);
			model.addAttribute("admin", null);
			return "admin/registerPage";
		}
		String name = admin.getName();

		// 名字转义，防止带特殊符号的恶意用户名
		name = HtmlUtils.htmlEscape(name);
		admin.setName(name);
		boolean exist = adminService.isExist(name);
		if (exist) {
			String message = "此管理员用户名已经被使用,请换一个用户名";
			model.addAttribute("msg", message);
			model.addAttribute("admin", null);
			return "admin/registerPage";
		}
		String base = admin.getPassword() + slat;
		admin.setPassword(DigestUtils.md5DigestAsHex(base.getBytes()));
		adminService.add(admin);
		return "redirect:adminRegisterSuccessPage";
	}

	/**
	 * 管理员登陆验证
	 */
	@RequestMapping("adminLogin")
	public String loginPage(Model model, Admin admin, HttpSession session) {

		admin.setName(HtmlUtils.htmlEscape(admin.getName()));
		String base = admin.getPassword() + slat;
		String passWord = DigestUtils.md5DigestAsHex(base.getBytes());

		//adminService.get()：根据name与password查找管理员实例
		Admin now = adminService.get(admin.getName(), passWord);
		if (null == now) {
			model.addAttribute("msg", "账号或密码错误");
			return "admin/loginPage";
		}
		model.addAttribute("admin", now);
		session.setAttribute("admin", now);
		return "redirect:admin_category_list";
	}

	/**
	 * 管理员登出
	 */
	@RequestMapping("adminLogout")
	public String logoutPage( HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:admin_login";
    }
}





















