package com.ltr.mymall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ltr.mymall.pojo.Admin;

/**
 * @author 刘添瑞 
 * 管理员登陆拦截器 
 * 拦截所有“admin_”开头的uri
 *
 */
public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String contextPath = session.getServletContext().getContextPath();

		String uri = request.getRequestURI();
		uri = StringUtils.remove(uri, contextPath);

		if (uri.startsWith("/admin_")) {
			Admin admin = (Admin) session.getAttribute("admin");
			if (null == admin) {
				response.sendRedirect("adminLoginPage");
				return false;
			}
		}
		return true;

	}

}
