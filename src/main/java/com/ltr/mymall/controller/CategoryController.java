package com.ltr.mymall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ltr.mymall.mapper.ProductMapper;
import com.ltr.mymall.mapper.PropertyMapper;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.Product;
import com.ltr.mymall.pojo.ProductExample;
import com.ltr.mymall.pojo.Property;
import com.ltr.mymall.pojo.PropertyExample;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.service.ProductService;
import com.ltr.mymall.util.ImageUtil;
import com.ltr.mymall.util.Page;
import com.ltr.mymall.util.UploadedImageFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * Category数据表表 primary key: id key: name
 *
 */
@Controller
@RequestMapping("")
public class CategoryController {
	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductMapper productMapper;

	@Autowired
	PropertyMapper propertyMapper;

	/**
	 * 使用PageHelper分页显示
	 * 
	 * @param model
	 * @param page  获取页面相关数据
	 * @return
	 */
	@RequestMapping("admin_category_list")
	public String list(Model model, Page page) {
		PageHelper.offsetPage(page.getStart(), page.getCount());
		List<Category> cs = categoryService.list();
		int total = (int) new PageInfo<>(cs).getTotal();
		page.setTotal(total);

		model.addAttribute("cs", cs);
		model.addAttribute("page", page);
		return "admin/listCategory";
	}

	/**
	 * 添加分类、上传分类图片
	 * 
	 * @param category          接受页面提交的分类名
	 * @param session           获取当前应用的路径
	 * @param uploadedImageFile 接收上传的图片
	 * @throws IOException
	 * 
	 */
	@RequestMapping("admin_category_add")
	public String add(Category category, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
		categoryService.add(category);

		// 确定上传图片的文件夹位置
		File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, category.getId() + ".jpg");

		// 如果“img/category”文件夹不存在则创建
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		// System.out.println(uploadedImageFile);
		// System.out.println(uploadedImageFile.getImage());
		// System.out.println(file);

		// 图片保存在“img/category”
		uploadedImageFile.getImage().transferTo(file);

		// 确保图片是.jpg格式
		BufferedImage img = ImageUtil.change2jpg(file);
		ImageIO.write(img, "jpg", file);

		return "redirect:/admin_category_list";
	}

	/**
	 * 根据表单提交的id删除分类
	 * 
	 * 当且仅当一个分类没有属性与产品是才能删除
	 * 
	 * 否则跳到删除错误界面
	 * 
	 * @param id 接受表单注入的id
	 * 
	 */
	@RequestMapping("admin_category_delete")
	public String delete(int id, HttpSession session, Model model) throws IOException {

		// 标注这是一个分类删除操作
		String delete_check = "category_delete";
		/**
		 * 查找分类下所有产品
		 */
		ProductExample product_example = new ProductExample();
		product_example.createCriteria().andCidEqualTo(id);
		List<Product> product_result = productMapper.selectByExample(product_example);

		/**
		 * 查找分类下所有属性
		 */
		PropertyExample property_example = new PropertyExample();
		property_example.createCriteria().andCidEqualTo(id);
		List<Property> property_result = propertyMapper.selectByExample(property_example);

		if (0 == product_result.size() && 0 == property_result.size()) {
			categoryService.delete(id);
			// 图片删除
			File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
			File file = new File(imageFolder, id + ".jpg");
			file.delete();

			return "redirect:/admin_category_list";

		} else {
			Category category = categoryService.get(id);

			model.addAttribute("delete_instance", category);
			model.addAttribute("delete_check", delete_check);
			return "admin/delete_error";
		}
	}

	/**
	 * 根据表单提交的id编辑分类
	 * 
	 * @param id 接受表单注入的id
	 * 
	 */
	@RequestMapping("admin_category_edit")
	public String edit(Model model, int id) {
		Category category = categoryService.get(id);

		model.addAttribute("c", category);
		return "admin/editCategory";
	}

	/**
	 * 更新数据
	 * 
	 * @param category          接受表单注入的分类信息
	 * @param session           获取当前应用路径
	 * @param uploadedImageFile 接收上传的图片
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("admin_category_update")
	public String update(Category category, HttpSession session, UploadedImageFile uploadedImageFile)
			throws IOException {
		categoryService.update(category);

		// 获取图片的二进制形式
		MultipartFile image = uploadedImageFile.getImage();

		// 更改过图片再执行
		if (null != image && !image.isEmpty()) {
			File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
			File file = new File(imageFolder, category.getId() + ".jpg");
			image.transferTo(file);
			BufferedImage img = ImageUtil.change2jpg(file);
			ImageIO.write(img, "jpg", file);
		}
		return "redirect:/admin_category_list";
	}
}
