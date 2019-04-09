package com.ltr.mymall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.PropertyValue;
import com.ltr.mymall.service.ProductService;
import com.ltr.mymall.service.PropertyValueService;

@Controller
@RequestMapping("")
public class PropertyValueController {

	@Autowired
	PropertyValueService propertyValueService;

	@Autowired
	ProductService productService;

	/**
	 * 
	 * @param model
	 * @param pid
	 * @return
	 */
	@RequestMapping("admin_propertyValue_edit")
	public String edit(Model model, int pid) {
		Product product = productService.get(pid);
		propertyValueService.init(product);
		List<PropertyValue> propertyValueList = propertyValueService.list(product.getId());

		model.addAttribute("p", product);
		model.addAttribute("pvs", propertyValueList);
		return "admin/editPropertyValue";
	}

	/**
	 * Ajax异步刷新更新属性值
	 * 
	 * @param propertyValue 封装了属性的value与id
	 * @return
	 */
	@RequestMapping("admin_propertyValue_update")
	@ResponseBody
	public String update(PropertyValue propertyValue) {
		propertyValueService.update(propertyValue);
		return "success";
	}
}
