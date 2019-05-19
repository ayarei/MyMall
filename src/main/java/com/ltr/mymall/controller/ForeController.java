package com.ltr.mymall.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.ProductImage;
import com.ltr.mymall.pojo.PropertyValue;
import com.ltr.mymall.pojo.Review;
import com.ltr.mymall.pojo.User;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.OrderItemService;
import com.ltr.mymall.service.OrderService;
import com.ltr.mymall.service.ProductImageService;
import com.ltr.mymall.service.ProductService;
import com.ltr.mymall.service.PropertyValueService;
import com.ltr.mymall.service.ReviewService;
import com.ltr.mymall.service.UserService;

/**
 * 前台页面Controller
 * 
 * @author 刘添瑞
 *
 */
@Controller
@RequestMapping("")
public class ForeController {

	// 注册与登录的MD5盐值
	private static final String slat = "kjag15&*%FJD92tvjabES$%@dj8ah43dchFRe*^$#";

	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	UserService userService;
	@Autowired
	ProductImageService productImageService;
	@Autowired
	PropertyValueService propertyValueService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ReviewService reviewSerive;

	/**
	 * 为前台的分类填充产品集合
	 * 
	 */
	@RequestMapping("forehome")
	public String home(Model model) {
		List<Category> categoryList = categoryService.list();
		productService.fill(categoryList);
		productService.fillByRow(categoryList);
		model.addAttribute("cs", categoryList);
		return "fore/home";

	}
	
	/**
	 * 进入产品详情页
	 * @param pid
	 * @param model
	 * @return
	 */
	@RequestMapping("foreproduct")
	public String product(int pid,Model model) {
		Product product = productService.get(pid);
		
		List<ProductImage> productSingleImages = productImageService.list(product.getId(), productImageService.type_single);
		List<ProductImage> productDetailImages = productImageService.list(product.getId(), productImageService.type_detail);
	
		product.setProductSingleImages(productSingleImages);
		product.setProductDetailImages(productDetailImages);
		
		List<PropertyValue> propertyValueList = propertyValueService.list(product.getId());
		List<Review> reviewList = reviewSerive.list(product.getId());
		productService.setReviewAndSaleNumber(product);
		model.addAttribute("reviews",reviewList);
		model.addAttribute("p", product);
		model.addAttribute("pvs", propertyValueList);
		return "fore/product";
	
	}

	/**
	 * 用户注册
	 * 
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping("foreregister")
	public String register(Model model, User user) {
		String name = user.getName();

		// 名字转义，防止带特殊符号的恶意用户名
		name = HtmlUtils.htmlEscape(name);
		user.setName(name);
		boolean exist = userService.isExist(name);

		if (exist) {
			String message = "此用户名已经被注册，请换一个用户名";
			model.addAttribute("msg", message);
			model.addAttribute("user", null);
			return "fore/register";
		}
		// 将密码MD5加密
		String base = user.getPassword() + slat;
		user.setPassword(DigestUtils.md5DigestAsHex(base.getBytes()));
		userService.add(user);
		return "redirect:registerSuccessPage";

	}

	/**
	 * 用户登录
	 * @param model
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping("forelogin")
	public String login(Model model, User user, HttpSession session) {
		user.setName(HtmlUtils.htmlEscape(user.getName()));
		String base = user.getPassword() + slat;
		String password = DigestUtils.md5DigestAsHex(base.getBytes());

		User now = userService.get(user.getName(), password);

		if (null == now) {
			model.addAttribute("msg", "账号或密码错误，请重新输入");
			return "fore/login"; 
		}

		model.addAttribute("user", now);
		session.setAttribute("user", now);
		return "redirect:forehome"; 
	}
	
	/**
	 * 用户登出
	 * @param session
	 * @return
	 */
	@RequestMapping("forelogout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:forehome";
	}
	

}





















