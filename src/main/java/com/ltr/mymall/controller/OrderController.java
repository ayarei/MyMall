package com.ltr.mymall.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.druid.stat.TableStat.Mode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ltr.mymall.pojo.Order;
import com.ltr.mymall.service.OrderItemService;
import com.ltr.mymall.service.OrderService;
import com.ltr.mymall.util.Page;

@RequestMapping("")
@Controller
public class OrderController {
	@Autowired
	OrderService orderService;

	@Autowired
	OrderItemService orderItemService;

	@RequestMapping("admin_order_list")
	public String list(Model model, Page page) {
		PageHelper.offsetPage(page.getStart(), page.getCount());

		List<Order> orderList = orderService.list();

		int total = (int) new PageInfo<>(orderList).getTotal();
		page.setTotal(total);

		orderItemService.fill(orderList);

		model.addAttribute("os", orderList);
		model.addAttribute("page", page);

		return "admin/listOrder";
	}
	
	/**
	 *  发货
	 * @param o
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("admin_order_delivery")
    public String delivery(Order o) throws IOException {
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return "redirect:admin_order_list";
    }
}




















