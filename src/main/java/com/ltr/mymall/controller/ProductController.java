package com.ltr.mymall.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ltr.mymall.mapper.ProductImageMapper;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.ProductImage;
import com.ltr.mymall.pojo.ProductImageExample;
import com.ltr.mymall.pojo.Property;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.ProductImageService;
import com.ltr.mymall.service.ProductService;
import com.ltr.mymall.util.Page;

/**
 * Product数据表 
 * primary key: id 
 * foreign key: cid 
 * key: 		name 
 * key: 		subTitle 
 * key: 		originalPrice 
 * key: 		promotePrice 
 * key: 		stock 
 * key: 		createDate
 */
@Controller
@RequestMapping("")
public class ProductController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductImageService productImageService;
	
	@Autowired
	ProductImageMapper productImageMapper;

	/**
	 * 在对应分类下添加产品
	 * 
	 * @param property
	 *            接收表单提交的属性
	 * @return
	 */
	@RequestMapping("admin_product_add")
	public String add(Model model, Product product) {
		product.setCreateDate(new Date());
		productService.add(product);
		return "redirect:admin_product_list?cid=" + product.getCid();
	}

	/**
	 * 根据表单提供的id删除分类下的产品
	 * 这是一个危险的操作，此处将会删除该产品相关的所有数据
	 * 2019/4/10 更新：目前的删除方式可能会对前台数据产生不可预料的错误。例如顾客若想查找被下架的商品会发生错误。
	 * 产品最好还是不要删除，设定成无效商品也许更好。
	 * 
	 * @param id 接收表单提供的产品id
	 * @return
	 */
	@RequestMapping("admin_product_delete")
	public String delete(int id, HttpSession session) {

		/* 每个product对象包含了其所在的category的信息 */
		Product product = productService.get(id);
		
		/**
		 * 删除产品所有图片(文件）
		 * 产品id(对应图片pid) ——> 图片id ——> 遍历删除
		 */
		ProductImageExample example = new ProductImageExample();
		example.createCriteria().andPidEqualTo(id);
		List<ProductImage> result = productImageMapper.selectByExample(example);
		for (ProductImage pi : result) {
			String fileName = pi.getId() + ".jpg";
			String imageFolder;
			String imageFolder_small = null;
			String imageFolder_middle = null;

			imageFolder = session.getServletContext().getRealPath("img/productSingle");
			imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
			imageFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");
			File imageFileSingle = new File(imageFolder, fileName);
			File f_small = new File(imageFolder_small, fileName);
			File f_middle = new File(imageFolder_middle, fileName);
			imageFileSingle.delete();
			f_small.delete();
			f_middle.delete();

			imageFolder = session.getServletContext().getRealPath("img/productDetail");
			File imageFileDetail = new File(imageFolder, fileName);
			imageFileDetail.delete();
		}
		
		/**
		 * 因为有外键的约束，不能够直接根据产品主键id删除产品 必须把产品下的外键约束都删除 包括图片和属性值
		 * 
		 * 此处删除产品所有相关(图片、属性值)数据库记录
		 */
		productService.delete(id);

		return "redirect:admin_product_list?cid=" + product.getCid();
	}

	/**
	 * 根据表单提供的id编辑当前已经存在的产品
	 * 
	 * @param model
	 * @param id
	 *            接收表单提供的id
	 * @return
	 */
	@RequestMapping("admin_product_edit")
	public String edit(Model model, int id) {
		Product product = productService.get(id);

		Category category = categoryService.get(product.getCid());
		product.setCategory(category);
		model.addAttribute("p", product);
		return "admin/editProduct";
	}

	@RequestMapping("admin_product_update")
	public String update(Product product) {
		productService.update(product);
		return "redirect:admin_product_list?cid=" + product.getCid();
	}

	/**
	 * 带分页的属性查询功能
	 * 
	 * @param model
	 * @param page
	 * @param cid
	 *            用于获取对应的分类信息
	 * @return
	 */
	@RequestMapping("admin_product_list")
	public String list(Model model, Page page, int cid) {
		Category category = categoryService.get(cid);
		PageHelper.offsetPage(page.getStart(), page.getCount());
		List<Product> productList = productService.list(cid);

		int total = (int) new PageInfo<>(productList).getTotal();
		page.setTotal(total);
		page.setParam("&cid=" + category.getId());

		model.addAttribute("ps", productList);
		model.addAttribute("c", category);
		model.addAttribute("page", page);
		return "admin/listProduct";
	}
}
