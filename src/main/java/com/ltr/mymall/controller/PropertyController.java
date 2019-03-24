package com.ltr.mymall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Property;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.PropertyService;
import com.ltr.mymall.util.Page;

@Controller
@RequestMapping("")
public class PropertyController {

	@Autowired
	CategoryService categoryService;
	@Autowired
	PropertyService propertyService;
	
	/**
	 * 在对应分类下添加属性名
	 * @param property 接收表单提交的属性
	 * @return
	 */
	@RequestMapping("admin_property_add")
	public String add(Property property) {
		propertyService.add(property);
		return "redirect:admin_property_list?cid="+property.getCid();
	}
	
	/**
	 * 根据表单提供的id删除分类下的属性名
	 * @param id 接收表单提供的id
	 * @return
	 */
	@RequestMapping("admin_property_delete")
	public String delete(int id) {
		Property property = propertyService.get(id);
		propertyService.delete(id);
		return "redirect:admin_property_list?cid="+property.getCid();
	}
	
	/**
	 * 根据表单提供的id编辑当前已经存在的属性名
	 * @param model 
	 * @param id 接收表单提供的id
	 * @return
	 */
	@RequestMapping("admin_property_edit")
	public String edit(Model model, int id) {
		Property property = propertyService.get(id);
		
		/*解释一下这里使用Category的意义*/
		/**
		 * 在后面编辑时会显示提供属性名
		 * 隐式提供属性id，与属性所在分类的id
		 * 虽然分类id的确可以通过property.getCid()获取
		 * 但独立开来可以体现两者的关系，通过关系获取逻辑更清楚
		 * 所以后面写成value="${p.category.id} 而不是value="${p.getCid()}
		 * 前面pojo.Property.java中加上Category成员变量也有这方面的原因
		 * 
		 */
		Category category = categoryService.get(property.getCid());
		property.setCategory(category);
		model.addAttribute("p",property);
		return "admin/editProperty";
	}
	
	/**
	 * 根据表单提交的属性更新数据库数据
	 * @param p
	 * @return
	 */
	@RequestMapping("admin_property_update")
    public String update(Property property) {
        propertyService.update(property);
        return "redirect:admin_property_list?cid="+property.getCid();
    }
	
	/**
	 * 带分页的属性查询功能
	 * @param model
	 * @param page
	 * @param cid 用于获取对应的分类信息
	 * @return
	 */
	@RequestMapping("admin_property_list")
	public String list(Model model, Page page, int cid) {
		Category category = categoryService.get(cid);
		PageHelper.offsetPage(page.getStart(), page.getCount());
		List<Property> propertyList = propertyService.list(cid);
		
		int total = (int) new PageInfo<>(propertyList).getTotal();
		page.setTotal(total);
        page.setParam("&cid="+category.getId());
 
        model.addAttribute("ps", propertyList);
        model.addAttribute("c", category);
        model.addAttribute("page", page);
        return "admin/listProperty";
	}
}

























