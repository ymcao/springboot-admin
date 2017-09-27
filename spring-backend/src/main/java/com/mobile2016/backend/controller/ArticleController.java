package com.mobile2016.backend.controller;
import java.util.List;

import com.mobile2016.backend.model.Article;
import com.mobile2016.backend.model.Category;
import com.mobile2016.backend.service.ArticleService;
import com.mobile2016.backend.service.CategoryService;
import com.mobile2016.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 文章分类列表
	 * @param category
	 * @param pageCurrent
	 * @param pageSize
	 * @param pageCount
	 * @param model
	 * @return
	 */
	@RequestMapping("/article/category_{pageCurrent}_{pageSize}_{pageCount}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String categorys(Category category, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount, Model model) {
		//判断
		if(pageSize == 0) pageSize = 10;
		if(pageCurrent == 0) pageCurrent = 1;
		int rows = categoryService.count();
		if(pageCount == 0) pageCount = rows%pageSize == 0 ? (rows/pageSize) : (rows/pageSize) + 1;
		
		//查询
		category.setStart((pageCurrent - 1)*pageSize);
		category.setEnd(pageSize);
		List<Category> list = categoryService.list(category);
		
		//输出
		model.addAttribute("list", list);
		String pageHTML = PageUtil.getPageContent("category_{pageCurrent}_{pageSize}_{pageCount}?name="+category.getName(), pageCurrent, pageSize, pageCount);
		model.addAttribute("pageHTML",pageHTML);
		model.addAttribute("category",category);
		return "category_list";
	}


	/**
	 * @return
	 */
	@GetMapping("/article/categoryUpdate")
	public String toCategory(Model model,Category category) {


		if(category!=null&&category.getId()!=0){

			Category  cate= categoryService.findById(category.getId());
			model.addAttribute("category",cate);
		}

		return "category_update";
	}

	/**
	 * @param model
	 * @param category
	 * @return
	 */
	@PostMapping("/article/categoryUpdate")
	public String updateCategory(Model model,Category category) {

		if(category!=null&&category.getId()==0){
			categoryService.insert(category);
			model.addAttribute("result","添加成功");
		}else{
			categoryService.updateCategory(category);
			model.addAttribute("result","更新成功");
		}


		//return "news/categoryDel";
		return "redirect:category_0_0_0";
	}

	/**
	 * 分类删除
	 * @param model
	 * @param category
	 * @return
	 */
	@PostMapping("/article/categoryDel")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String categoryDel(Model model,Category category) {
		if(category!=null&&category.getId()!=0){
			categoryService.delById(category);
			model.addAttribute("result","删除成功");
		}
		//return "news/categoryDel";
		return "redirect:category_0_0_0";
	}



	/////////////////////////////////////////////////////////////////////////


	/**
	 * 文章分类列表
	 * @param article
	 * @param pageCurrent
	 * @param pageSize
	 * @param pageCount
	 * @param model
	 * @return
	 */
	@RequestMapping("/article/article_{pageCurrent}_{pageSize}_{pageCount}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String articles(Article article, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount, Model model) {
		//判断
		if(pageSize == 0) pageSize = 10;
		if(pageCurrent == 0) pageCurrent = 1;
		int rows = articleService.count(article);
		if(pageCount == 0) pageCount = rows%pageSize == 0 ? (rows/pageSize) : (rows/pageSize) + 1;

		//查询
		article.setStart((pageCurrent - 1)*pageSize);
		article.setEnd(pageSize);
		List<Article> list = articleService.list(article);


		//输出
		model.addAttribute("articles", list);
		String pageHTML = PageUtil.getPageContent("article_{pageCurrent}_{pageSize}_{pageCount}?title="+article.getTitle(), pageCurrent, pageSize, pageCount);
		model.addAttribute("pageHTML",pageHTML);
		model.addAttribute("article",article);
		return "article_list";
	}



	/**
	 * @return
	 */
	@GetMapping("/article/articleUpdate")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String toArticle(Model model,Article article) {


		//文章分类
		Category c = new Category();
		c.setStart(0);
		c.setEnd(Integer.MAX_VALUE);
		List<Category> categorys = categoryService.list(c);

		//输出
		model.addAttribute("article_categorys", categorys);


		if(article!=null&&article.getId()!=0){
			Article  art= articleService.findById(article.getId());
			model.addAttribute("article",art);
		}

		return "article_update";
	}

	/**
	 * 更新分类
	 * @param model
	 * @param article
	 * @return
	 */
	@PostMapping("/article/articleUpdate")
	public String updateArticle(Model model,Article article) {

		if(article!=null&&article.getId()==0){
			articleService.insert(article);
			model.addAttribute("result","添加成功");
		}else{
			articleService.updateArticle(article);
			model.addAttribute("result","更新成功");
		}


		//return "news/categoryDel";
		return "redirect:article_0_0_0";
	}


	/**
	 * 文章删除
	 * @param model
	 * @param article
	 * @return
	 */
	@PostMapping("/article/articleDel")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String articleDel(Model model,Article article) {
		if(article!=null&&article.getId()!=0){
			articleService.delById(article);
			model.addAttribute("result","删除成功");
		}
		//return "news/categoryDel";
		return "redirect:article_0_0_0";
	}
	
}
