package com.ltr.mymall.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ltr.mymall.comparator.ProductAllComparator;
import com.ltr.mymall.comparator.ProductDateComparator;
import com.ltr.mymall.comparator.ProductPriceComparator;
import com.ltr.mymall.comparator.ProductReviewComparator;
import com.ltr.mymall.comparator.ProductSaleCountComparator;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.OrderItem;
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
import com.ltr.mymall.util.Page;

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
	 * 
	 * @param pid
	 * @param model
	 * @return
	 */
	@RequestMapping("foreproduct")
	public String product(int pid, Model model) {
		Product product = productService.get(pid);

		List<ProductImage> productSingleImages = productImageService.list(product.getId(),
				ProductImageService.type_single);
		List<ProductImage> productDetailImages = productImageService.list(product.getId(),
				ProductImageService.type_detail);

		product.setProductSingleImages(productSingleImages);
		product.setProductDetailImages(productDetailImages);

		List<PropertyValue> propertyValueList = propertyValueService.list(product.getId());
		List<Review> reviewList = reviewSerive.list(product.getId());
		productService.setReviewAndSaleNumber(product);
		model.addAttribute("reviews", reviewList);
		model.addAttribute("p", product);
		model.addAttribute("pvs", propertyValueList);
		return "fore/product";

	}

	/**
	 * 前台产品页排序
	 * 
	 * @param cid
	 * @param sort 
	 * review：    评论数倒序 
	 * saleCount： 销量倒序 
	 * date：      上架时间倒序 
	 * price：     价格正序 
	 * all：       销量*评论数倒序
	 * 
	 * @return
	 */
	@RequestMapping("forecategory")
	public String foreCategory(int cid, String sort, Model model, Page page) {
		// 设置分类页每页显示的产品数
		final int productCount = 2;

		// 默认情况下排序
		if (null == sort) {

			page.setCount(productCount);

			Category category = categoryService.get(cid);
			PageHelper.offsetPage(page.getStart(), page.getCount());
			List<Product> products = productService.list(cid);
			int total = (int) new PageInfo<>(products).getTotal();

			category.setProducts(products);
			page.setParam("cid=" + category.getId() + "&");
			productService.setReviewAndSaleNumber(category.getProducts());

			page.setTotal(total);
			model.addAttribute("c", category);
			model.addAttribute("page", page);
			return "fore/category";

		// 条件排序
		} else {
			Category category = categoryService.get(cid);
			List<Product> products = productService.list(cid);
			List<Product> foreShow = new ArrayList<Product>(productCount);
			int total = products.size();
			page.setCount(productCount);
			page.setTotal(total);

			switch (sort) {
			case "review":
				Collections.sort(products, new ProductReviewComparator());
				page.setParam("cid=" + category.getId() + "&sort=review&");
				break;
			case "date":
				Collections.sort(products, new ProductDateComparator());
				page.setParam("cid=" + category.getId() + "&sort=date&");
				break;

			case "saleCount":
				Collections.sort(products, new ProductSaleCountComparator());
				page.setParam("cid=" + category.getId() + "&sort=saleCount&");
				break;

			case "price":
				Collections.sort(products, new ProductPriceComparator());
				page.setParam("cid=" + category.getId() + "&sort=price&");
				break;

			case "all":
				Collections.sort(products, new ProductAllComparator());
				page.setParam("cid=" + category.getId() + "&sort=all&");
				break;
			}
			/**
			 * 分段截取全表查询的数据
			 * 注意边界情况
			 */
			if (page.getStart() + productCount <= total) {
				for (int i = page.getStart(); i < page.getStart() + productCount; i++)
					foreShow.add(products.get(i));
			} else {
				for (int i = page.getStart(); i < page.getStart() + total % productCount; i++)
					foreShow.add(products.get(i));
			}

			category.setProducts(foreShow);
			productService.setReviewAndSaleNumber(category.getProducts());
			model.addAttribute("c", category);
			model.addAttribute("page", page);
			return "fore/category";

		}
	}

	/**
	 * 前台搜索功能，截取满足条件的前20个产品
	 * @param keyword 搜索关键字
	 * @param model
	 * @return
	 */
	@RequestMapping("foresearch")
	public String foresearch(String keyword, Model model) {
		PageHelper.offsetPage(0, 20);
		List<Product> productList = productService.search(keyword);
		productService.setReviewAndSaleNumber(productList);
		model.addAttribute("ps", productList);
		return "fore/searchResult";
	}
	
	/**
	 * 用户在产品页面立即购买，同时会将购物车里同样的产品一起结账
	 * 
	 * @param pid
	 * @param buyNumber
	 * @param session
	 * @return
	 */
	@RequestMapping("forebuyone")
	public String forebuyone(int pid, @RequestParam("num")int buyNumber, HttpSession session) {
		Product product = productService.get(pid);
		int orderItemId = 0;

		User user = (User) session.getAttribute("user");
		
		boolean found = false;  //是否在购物车中有相同产品
		List<OrderItem> orderItemList = orderItemService.listByUser(user.getId());
		//检查是否有相同产品
		for (OrderItem e : orderItemList) {
			if (e.getProduct().getId().intValue() == product.getId().intValue()) {
				e.setNumber(e.getNumber() + buyNumber);
				orderItemService.update(e);
				found = true;
				orderItemId = e.getId();
			}
		}
		if (!found) {
			OrderItem orderItem = new OrderItem();
			orderItem.setUid(user.getId());
			orderItem.setNumber(buyNumber);
			orderItem.setPid(pid);
			orderItemService.add(orderItem);
			orderItemId = orderItem.getId();
		}
		return "redirect:forebuy?oiid=" + orderItemId;
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
		
		user.setPassword(getMD5(user.getPassword()));
		userService.add(user);
		return "redirect:registerSuccessPage";

	}

	/**
	 * 用户登录
	 * 
	 * @param model
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping("forelogin")
	public String login(Model model, User user, HttpSession session) {
		user.setName(HtmlUtils.htmlEscape(user.getName()));
		String password = getMD5(user.getPassword());

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
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("forelogout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:forehome";
	}

	/**
	 * 未登录时直接购买或者加入购物车时检查是否登录
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("forecheckLogin")
	@ResponseBody
	public String checkLogin(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (null != user) {
			return "success";
		}
		return "fail";
	}

	/**
	 * 模态页面登录检查
	 * 
	 * @param name
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping("foreloginAjax")
	@ResponseBody
	public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password,
			HttpSession session) {
		name = HtmlUtils.htmlEscape(name);
		password = getMD5(password);

		User user = userService.get(name, password);
		if (null == user) {
			return "fail";
		}
		session.setAttribute("user", user);
		return "success";
	}
	
	private String getMD5(String password) {
		String base = password + slat;
		String passWord = DigestUtils.md5DigestAsHex(base.getBytes());
		return passWord;
	}
}














