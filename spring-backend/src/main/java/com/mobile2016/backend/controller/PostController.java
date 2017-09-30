package com.mobile2016.backend.controller;
import java.util.List;

import com.mobile2016.backend.model.Post;
import com.mobile2016.backend.model.Target;
import com.mobile2016.backend.service.PostService;
import com.mobile2016.backend.service.TargetService;
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
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private TargetService targetService;
	
	/**
	 * 文章分类列表
	 * @param target
	 * @param pageCurrent
	 * @param pageSize
	 * @param pageCount
	 * @param model
	 * @return
	 */
	@RequestMapping("/target_{pageCurrent}_{pageSize}_{pageCount}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String targets(Target target, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount, Model model) {
		//判断
		if(pageSize == 0) pageSize = 10;
		if(pageCurrent == 0) pageCurrent = 1;
		int rows = targetService.count();
		if(pageCount == 0) pageCount = rows%pageSize == 0 ? (rows/pageSize) : (rows/pageSize) + 1;
		
		//查询
		target.setStart((pageCurrent - 1)*pageSize);
		target.setEnd(pageSize);
		List<Target> list = targetService.list(target);
		
		//输出
		model.addAttribute("targets", list);
		String pageHTML = PageUtil.getPageContent("target_{pageCurrent}_{pageSize}_{pageCount}?name="+target.getName(), pageCurrent, pageSize, pageCount);
		model.addAttribute("pageHTML",pageHTML);
		model.addAttribute("target",target);
		return "target_list";
	}


	/**
	 *
	 * @param model
	 * @param target
	 * @return
	 */
	@GetMapping("/targetUpdate")
	public String toTarget(Model model,Target target) {


		if(target!=null&&target.getId()!=0){

			Target  cate= targetService.findById(target.getId());
			model.addAttribute("target",cate);
		}

		return "target_update";
	}

	/**
	 * @param model
	 * @param target
	 * @return
	 */
	@PostMapping("/targetUpdate")
	public String targetUpdate(Model model,Target target) {

		if(target!=null&&target.getId()==0){
			targetService.insert(target);
			model.addAttribute("result","添加成功");
		}else{
			targetService.updateCategory(target);
			model.addAttribute("result","更新成功");
		}


		//return "news/categoryDel";
		return "redirect:target_0_0_0";
	}

	/**
	 * 分类删除
	 * @param model
	 * @param target
	 * @return
	 */
	@PostMapping("/targetDel")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String targetDel(Model model,Target target) {
		if(target!=null&&target.getId()!=0){
			target.setEnabled(0);
			targetService.delById(target);
			model.addAttribute("result","删除成功");
		}
		//return "news/categoryDel";
		return "redirect:target_0_0_0";
	}



	/////////////////////////////////////////////////////////////////////////


	/**
	 * 文章分类列表
	 * @param post
	 * @param pageCurrent
	 * @param pageSize
	 * @param pageCount
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_{pageCurrent}_{pageSize}_{pageCount}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String posts(Post post, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount, Model model) {
		//判断
		if(pageSize == 0) pageSize = 10;
		if(pageCurrent == 0) pageCurrent = 1;
		int rows = postService.count(post);
		if(pageCount == 0) pageCount = rows%pageSize == 0 ? (rows/pageSize) : (rows/pageSize) + 1;

		//查询
		post.setStart((pageCurrent - 1)*pageSize);
		post.setEnd(pageSize);
		List<Post> list = postService.list(post);

		//Post分类
		Target c = new Target();
		c.setStart(0);
		c.setEnd(Integer.MAX_VALUE);
		List<Target> categorys = targetService.list(c);

		//输出
		model.addAttribute("post_targets", categorys);

		//输出
		model.addAttribute("posts", list);
		String pageHTML = PageUtil.getPageContent("post_{pageCurrent}_{pageSize}_{pageCount}?title="+post.getTitle(), pageCurrent, pageSize, pageCount);
		model.addAttribute("pageHTML",pageHTML);
		model.addAttribute("post",post);
		return "post_list";
	}



	/**
	 * @return
	 */
	@GetMapping("/postUpdate")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String toPost(Model model,Post post) {


		//Post分类
		Target c = new Target();
		c.setStart(0);
		c.setEnd(Integer.MAX_VALUE);
		List<Target> categorys = targetService.list(c);

		//输出
		model.addAttribute("post_targets", categorys);


		if(post!=null&&post.getId()!=0){
			Post art= postService.findById(post.getId());
			model.addAttribute("post",art);
		}

		return "post_update";
	}

	/**
	 * 更新分类
	 * @param model
	 * @param article
	 * @return
	 */
	@PostMapping("/postUpdate")
	public String postUpdate(Model model,Post article) {

		if(article!=null&&article.getId()==0){
			postService.insert(article);
			model.addAttribute("result","添加成功");
		}else{
			postService.updateArticle(article);
			model.addAttribute("result","更新成功");
		}


		//return "news/categoryDel";
		return "redirect:post_0_0_0";
	}


	/**
	 * POST删除
	 * @param model
	 * @param post
	 * @return
	 */
	@PostMapping("/postDel")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String postDel(Model model,Post post) {
		if(post!=null&&post.getId()!=0){
			post.setEnabled(0);
			postService.delById(post);
			model.addAttribute("result","删除成功");
		}
		return "redirect:post_0_0_0";
	}
	
}
