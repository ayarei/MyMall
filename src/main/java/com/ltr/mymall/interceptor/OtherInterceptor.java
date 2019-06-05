package com.ltr.mymall.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.OrderItem;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.OrderItemService;

public class OtherInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	CategoryService categoryService;
	@Autowired
	OrderItemService orderItemService;

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		List<Category> cs = categoryService.list();
		request.getSession().setAttribute("cs", cs);

		HttpSession session = request.getSession();
		// 获取购物车商品总数
		User user = (User) session.getAttribute("user");
		int cartTotalItemNumber = 0;
		if (null != user) {
			List<OrderItem> ois = orderItemService.listByUser(user.getId());
			for (OrderItem e : ois) {
				cartTotalItemNumber += e.getNumber();
			}
		}
		request.getSession().setAttribute("cartTotalItemNumber", cartTotalItemNumber);
	}
}
