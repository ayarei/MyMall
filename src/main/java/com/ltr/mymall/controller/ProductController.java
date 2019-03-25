package com.ltr.mymall.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.Property;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.ProductService;
import com.ltr.mymall.util.Page;
/**
 * Product数据表
 * promary key:	id
 * foreign key: cid
 * key:			name
 * key:			subTitle
 * key:			originalPrice
 * key:			promotePrice
 * key:			stock
 * key:			createDate
 */
@Controller
@RequestMapping("")
public class ProductController {

	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	
	/**
	 * 在对应分类下添加产品
	 * @param property 接收表单提交的属性
	 * @return
	 */
	@RequestMapping("admin_product_add")
	public String add(Model model,Product product) {
		product.setCreateDate(new Date());
		productService.add(product);
		return "redirect:admin_product_list?cid=" + product.getCid();
	}
	
	/**
	 * 根据表单提供的id删除分类下的产品
	 * @param id 接收表单提供的id
	 * @return
	 */
	@RequestMapping("admin_product_delete")
	public String delete(int id) {
		
		/*每个product对象包含了其所在的category的信息*/
		Product product = productService.get(id);
		productService.delete(id);
		return "redirect:admin_product_list?cid=" + product.getCid();
	}
	
	/**
	 * 根据表单提供的id编辑当前已经存在的产品
	 * @param model 
	 * @param id 接收表单提供的id
	 * @return
	 */
	@RequestMapping("admin_product_edit")
	public String edit(Model model,int id) {
		Product product = productService.get(id);
		
		
		Category category = categoryService.get(product.getCid());
		product.setCategory(category);
		model.addAttribute("p",product);
		return "admin/editProduct";
	}
	
	
	@RequestMapping("admin_product_update")
	public String update(Product product) {
		productService.update(product);
		return "redirect:admin_product_list?cid="+product.getCid();
	}
	

	/**
	 * 带分页的属性查询功能
	 * @param model
	 * @param page
	 * @param cid 用于获取对应的分类信息
	 * @return
	 */
	@RequestMapping("admin_product_list")
	public String list(Model model, Page page, int cid) {
		Category category = categoryService.get(cid);
		PageHelper.offsetPage(page.getStart(), page.getCount());
		 List<Product> productList = productService.list(cid);
		
		int total = (int) new PageInfo<>(productList).getTotal();
		page.setTotal(total);
        page.setParam("&cid="+category.getId());
 
        model.addAttribute("ps", productList);
        model.addAttribute("c", category);
        model.addAttribute("page", page);
        return "admin/listProduct";
	}
}























