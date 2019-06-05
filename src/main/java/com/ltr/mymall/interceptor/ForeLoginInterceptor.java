package com.ltr.mymall.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.OrderItemService;

public class ForeLoginInterceptor extends HandlerInterceptorAdapter {

	/*
	@Autowired
	CategoryService categoryService;
	@Autowired
	OrderItemService orderItemService;
	*/

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		String contextPath = session.getServletContext().getContextPath();
		
		// 进入以下页面不需要登录
		String[] noNeedAuthPage = new String[] {
				"home",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"
		};
		
		String uri = request.getRequestURI();
		uri = StringUtils.remove(uri, contextPath);
		
		if (uri.startsWith("/fore")) {
			String method = StringUtils.substringAfterLast(uri, "/fore");
			if (!Arrays.asList(noNeedAuthPage).contains(method)) {
				User user = (User) session.getAttribute("user");
				if (null == user) {
					response.sendRedirect("loginPage");
					return false;
				}
			}
		}
		return true;
	}

}
