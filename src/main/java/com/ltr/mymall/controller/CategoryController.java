package com.ltr.mymall.controller;

import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.service.CategoryService;
import com.ltr.mymall.util.ImageUtil;
import com.ltr.mymall.util.Page;
import com.ltr.mymall.util.UploadedImageFile;

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

@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    
    
    //分页显示
    @RequestMapping("admin_category_list")
    public String list(Model model, Page page){
        List<Category> cs= categoryService.list(page);
        
        int total = categoryService.total();
        page.setTotal(total);
        
        model.addAttribute("cs", cs);
        model.addAttribute("page",page);
        return "admin/listCategory";
    }
    
   
    /**
     * 添加分类、上传图片
     * @param category 接受页面提交的分类名
     * @param session  获取当前应用的路径
     * @param uploadedImageFile 接受上传的图片
     * @throws IOException
     * 
     */
    @RequestMapping("admin_category_add")
    public String add(Category category,HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
      	categoryService.add(category);
      	
      	//确定上传图片的文件夹位置
    	File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
    	File file = new File(imageFolder, category.getId() + ".jpg");
    	
    	//如果“img/category”文件夹不存在则创建
    	if(!file.getParentFile().exists())
    		file.getParentFile().mkdirs();
    	
    	//System.out.println(uploadedImageFile);
        //System.out.println(uploadedImageFile.getImage());
        //System.out.println(file);
    	
    	//保存图片在“img/category”
    	uploadedImageFile.getImage().transferTo(file);
    	
    	//确保图片是.jpg格式
    	BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        
        return "redirect:/admin_category_list";
    }
    
    /**
     * @param id 接受表单注入的id
     * 根据id删除分类
     */
    @RequestMapping("admin_category_delete")
    public String delete(int id, HttpSession session) throws IOException{
    	categoryService.delete(id);
    	//图片删除
    	File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
    	File file = new File(imageFolder, id + ".jpg");
    	file.delete();
    	
    	return "redirect:/admin_category_list";
    }
    
    /**
     * @param id 接受表单注入的id
     * 根据id编辑分类
     */
    @RequestMapping("admin_category_edit")
    public String edit(int id, Model model) {
    	Category category = categoryService.get(id);
    	
    	model.addAttribute("c",category);
    	return "admin/editCategory";
    }
}

































