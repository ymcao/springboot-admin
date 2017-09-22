package com.mobile2016.backend.controller;

import com.mobile2016.backend.model.User;
import com.mobile2016.backend.service.UserService;
import com.mobile2016.common.utils.ErrorUtil;
import com.mobile2016.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;



@Controller
public class AdminController {


	@Autowired
	private UserService userService;


	@GetMapping("/admin/index")
	public String index(Model model) {
		return "index_0_0_0";
	}

	/**
	 * 获取角色列表
	 * @return
	 */
	@GetMapping("/admin/index_{pageCurrent}_{pageSize}_{pageCount}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String getUserList(User u,
							  @PathVariable Integer pageCurrent,
							  @PathVariable Integer pageSize,
							  @PathVariable Integer pageCount,
							  Model model) {
		//判断
		if(pageSize == 0) pageSize = 10;
		if(pageCurrent == 0) pageCurrent = 1;
		int rows = userService.count();
		if(pageCount == 0) pageCount = rows%pageSize == 0 ? (rows/pageSize) : (rows/pageSize) + 1;

		u.setStart((pageCurrent - 1)*pageSize);
		u.setEnd(pageSize);
		if(u.getOrderBy()==null){u.setOrderBy("ADDDATE DESC");}

		List<User> userList;
		try {
			userList =userService.loadAllUsers(u);
			if(userList!=null&&userList.size()>0){
				for(User user:userList){
					if(user.getRole().equals("ROLE_USER")){
						user.setRole("普通用户");
					}else if(user.getRole().equals("ROLE_ADMIN")){
						user.setRole("管理员");
					}
				}
			}

			String pageHTML = PageUtil.getPageContent("index_{pageCurrent}_{pageSize}_{pageCount}"+"?orderBy="+u.getOrderBy(), pageCurrent, pageSize, pageCount);
			model.addAttribute("pageHTML",pageHTML);
			model.addAttribute("user",u);
			model.addAttribute("userList",userList);

		} catch (Exception e) {

		}
		return "index";
	}


	/**
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/admin/login","/"})
	public String loginGet(Model model,
						   @RequestParam(value = "error", required = false) String error,
						   HttpServletRequest request, HttpSession session) {



		if (error != null) {
			model.addAttribute("error",
					ErrorUtil.getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}
		try {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();

			if (auth instanceof AnonymousAuthenticationToken) {
				return  "login";
			} else {
				Object  pinciba=auth.getPrincipal();
				if(pinciba instanceof  UserDetails){
					UserDetails userDetail = ((UserDetails) pinciba);
					session.setAttribute("username", userDetail.getUsername());
					//model.addAttribute("username", userDetail.getUsername());
				}
				return  "index";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  "login";
		}

	}



	@GetMapping("/admin/create")
	public String getUser(Model model) {
		return "create";
	}

	/**
	 * 创建用户
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/admin/create")
	public String postUser( Model model,User user){

		try{
			userService.save(user);
			model.addAttribute("result", "添加成功！");
			return "create";
		}catch (Exception e){
			model.addAttribute("result", "添加失败！");
			return "create";
		}

	}




	@GetMapping("/admin/403")
	public String accessDeny(Model model) {
		return "403";
	}


	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}


}
