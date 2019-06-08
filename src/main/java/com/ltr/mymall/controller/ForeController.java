package com.ltr.mymall.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
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
import com.ltr.mymall.dto.CreateOrderExecution;
import com.ltr.mymall.dto.noStock;
import com.ltr.mymall.exception.OutOfStockException;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Order;
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
	private final Logger logger = Logger.getLogger(ForeController.class);

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
	 * @param 
	 * sort review： 评论数倒序 
	 * saleCount：   销量倒序 
	 * date：        上架时间倒序 
	 * price：       价格正序
	 * all：         销量*评论数倒序
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
			 * 分段截取全表查询的数据 注意边界情况
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
	 * 
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
	 * 用户在产品页面立即购买，此时并不会将购物车里相同的产品添加结算
	 * 
	 * @param pid       商品ID
	 * @param buyNumber 购买数量
	 * @param session
	 * @return
	 */
	@RequestMapping("forebuyone")
	public String forebuyone(int pid, @RequestParam("num") int buyNumber, HttpSession session) {
		int orderItemId = 0;

		User user = (User) session.getAttribute("user");

		OrderItem orderItem = new OrderItem();
		orderItem.setUid(user.getId());
		orderItem.setNumber(buyNumber);
		orderItem.setPid(pid);
		orderItemService.add(orderItem);
		orderItemId = orderItem.getId();

		return "redirect:forebuy?oiid=" + orderItemId;
	}

	/**
	 * 添加购物车
	 * 
	 * @param pid       商品ID
	 * @param buyNumber 购买数量
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("foreaddCart")
	@ResponseBody
	public String addCart(int pid, @RequestParam("num") int buyNumber, Model model, HttpSession session) {
		Product product = productService.get(pid);
		User user = (User) session.getAttribute("user");
		boolean found = false; // 是否在购物车中找到相应的产品

		// 列出购物车中所有商品
		List<OrderItem> orderItemList = orderItemService.listByUser(user.getId());

		// 若购物车已经有对应的产品则更新数量
		for (OrderItem e : orderItemList) {
			if (e.getProduct().getId().intValue() == product.getId()) {
				found = true;
				e.setNumber(e.getNumber() + buyNumber);
				orderItemService.update(e);
				break;
			}
		}
		// 若购物车没有对应产品则添加产品进购物车
		if (!found) {
			OrderItem orderItem = new OrderItem();
			orderItem.setUid(user.getId());
			orderItem.setNumber(buyNumber);
			orderItem.setPid(pid);
			orderItemService.add(orderItem);
		}
		return "success";

	}
	
	/**
	 * 用户查看购物车
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("forecart")
	public String foreCart(HttpSession session,Model model) {
		User user = (User) session.getAttribute("user");
		List<OrderItem> orderItemList = orderItemService.listByUser(user.getId());
		model.addAttribute("ois", orderItemList);
		return "fore/cart";
	}
	
	/**
	 * 用户修改购物车某件商品的数量
	 * 
	 * @param session
	 * @param productId
	 * @param buyNumber
	 * @return
	 */
	@RequestMapping("forechangeOrderItem")
	@ResponseBody
	public String changeOrderItem(HttpSession session, @RequestParam("pid") int productId,
			@RequestParam("number") int buyNumber) {
		User user = (User) session.getAttribute("user");
		// 再次判断是否登录，防止session失效造成的问题
		if(null == user) return "fail";
		
		List<OrderItem> orderItemList = orderItemService.listByUser(user.getId());
		for(OrderItem e : orderItemList) {
			if(e.getProduct().getId().intValue() == productId) {
				e.setNumber(buyNumber);
				orderItemService.update(e);
				break;
			}
		}
		return "success";
	}
	
	/**
	 * 用户删除购物车中的商品
	 * 
	 * @param session
	 * @param orderItemId
	 * @param model
	 * @return
	 */
	@RequestMapping("foredeleteOrderItem")
	@ResponseBody
	public String deleteOrderItem(HttpSession session, @RequestParam("oiid") int orderItemId, Model model) {
		User user = (User) session.getAttribute("user");
		// 再次判断是否登录，防止session失效造成的问题
		if (null == user)
			return "fail";

		orderItemService.delete(orderItemId);
		return "success";
	}

	/**
	 * 商品结算
	 * 
	 * @param model
	 * @param session
	 * @param orderItemId 一个ID对应一种商品
	 * @return
	 */
	@RequestMapping("forebuy")
	public String buy(Model model, HttpSession session, @RequestParam("oiid") String[] orderItemId) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		float total = 0; // 订单总价

		for (String e : orderItemId) {
			int id = Integer.parseInt(e);
			OrderItem orderItem = orderItemService.get(id);
			Product buyproduct = productService.get(orderItem.getPid());
			orderItem.setProduct(buyproduct);
			total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
			orderItemList.add(orderItem);
		}

		session.setAttribute("ois", orderItemList);
		model.addAttribute("total", total);
		return "fore/buy";
	}
	
	/**
	 * 用户创建订单，order中带有订单信息（收件人、电话号码、收件地址....）
	 * @param model
	 * @param session
	 * @param order
	 * @return
	 */
	@RequestMapping("forecreateOrder")
	public String createOrder(Model model, HttpSession session, Order order) {
		User user = (User) session.getAttribute("user");
		float total = -1;
		CreateOrderExecution execution = null;
		
		try {
			Date createDate = new Date();
			// 订单号由下单时间加上4位随机数组成
			String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(createDate) + RandomUtils.nextInt(10000);
			
			order.setOrderCode(orderCode);
			order.setCreateDate(createDate);
			order.setUid(user.getId());
			order.setStatus(OrderService.waitPay);
			List<OrderItem> ois= (List<OrderItem>)session.getAttribute("ois");
			
			// 乐观锁重试机制
			// 当购买商品库存为0或者购买商品数多于库存时抛出OutOfStockException
			// total == -1，并发购买冲突
			// total == -2，有售罄商品
			while(total == -1) {
				execution = orderService.add(order, ois);
				total = execution.getTotal();
			}
			
			if(total == -2) {
				throw new OutOfStockException("订单包含售罄商品");
			}
			return "redirect:forealipay?oid="+order.getId()+"&total="+total;
			
		} catch (OutOfStockException e) {
			logger.error("ID："+user.getId()+"，用户名："+user.getName()+"，"+e.getMessage());
			// 从OrderItemList中取出售罄商品、用户购买数量
			List<OrderItem> ois= (List<OrderItem>)session.getAttribute("ois");
			List<noStock> noStockProductList = new ArrayList<noStock>();

			for(Integer p : execution.getNoStockID()) {
				for(OrderItem k : ois) {
					if(k.getProduct().getId() == p) {
						noStockProductList.add(new noStock(k.getProduct(), k.getNumber()));
					}
				}
			}			
			model.addAttribute("noStockProductList",noStockProductList);
			return "fore/nostock";
		}	
	}
	
	/**
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("forebought")
	public String bought(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		// 查询出未被删除的订单
		List<Order> orderList = orderService.list(user.getId(), OrderService.delete);
		orderItemService.fill(orderList);
		model.addAttribute("os", orderList);
		return "fore/bought";
	}
	
	/**
	 * 扫码支付
	 * @param oid
	 * @param total
	 * @param model
	 * @return
	 */
	@RequestMapping("forepayed")
	public String payed(int oid, float total, Model model) {
	    Order order = orderService.get(oid);
	    order.setStatus(OrderService.waitDelivery);
	    order.setPayDate(new Date());
	    orderService.update(order);
	    model.addAttribute("o", order);
	    return "fore/payed";
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
