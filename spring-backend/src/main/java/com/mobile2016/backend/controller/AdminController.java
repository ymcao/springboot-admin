package com.mobile2016.backend.controller;

import com.mobile2016.backend.model.AdminUser;
import com.mobile2016.backend.model.User;
import com.mobile2016.backend.service.UploadService;
import com.mobile2016.backend.service.UserService;
import com.mobile2016.common.utils.ErrorUtil;
import com.mobile2016.common.utils.FileUtil;
import com.mobile2016.common.utils.LoggerUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private UploadService uploadService;



	//-------->管理员用户操作<-----------

	/**
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/admin/login","/"})
	public String loginGet(Model model,
						   @RequestParam(value = "error", required = false) String error,
						   HttpServletRequest request) {


		if (error != null) {
			model.addAttribute("error",
					ErrorUtil.getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}
		return "login";

	}


	@GetMapping("/admin/index")
	public String index(HttpSession session) {

		try {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();

			if (auth instanceof AnonymousAuthenticationToken) {
				return "login";
			} else {
				Object  pinciba=auth.getPrincipal();
				if(pinciba instanceof  UserDetails){
					UserDetails userDetail = ((UserDetails) pinciba);
					session.setAttribute("username", userDetail.getUsername());
					//model.addAttribute("username", userDetail.getUsername());
				}

				return "index";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "login";
		}

	}



	@GetMapping("/admin/adminUserCreate")
	@PreAuthorize("hasAnyRole('SUPER')")
	public String addAdminUser() {
		return "adminuser_create";
	}


	/**
	 * 创建管理用户
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/admin/adminUserCreate")
	public String addAdminUser(Model model, AdminUser user){
		try{

			userService.insertAdminUser(user);
			model.addAttribute("result", "添加成功！");

			return "adminuser_create";
		}catch (Exception e){
			model.addAttribute("result", "添加失败！");
			return "adminuser_create";
		}


	}


	//-------------->普通用户操作<-----------

	/**
	 * 获取注册用户列表
	 * @return
	 */
	@GetMapping("/admin/user_{pageCurrent}_{pageSize}_{pageCount}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
	public String usersList(User u,
							  @PathVariable Integer pageCurrent,
							  @PathVariable Integer pageSize,
							  @PathVariable Integer pageCount,
							  Model model) {
		//判断
		if(pageSize == 0) pageSize = 10;
		if(pageCurrent == 0) pageCurrent = 1;
		int rows = userService.count();
		if(pageCount == 0) pageCount = rows%pageSize == 0 ? (rows/pageSize) : (rows/pageSize) + 1;
        //u.setSize(rows);
		u.setStart((pageCurrent - 1)*pageSize);
		u.setEnd(pageSize);
		if(u.getOrderBy()==null){u.setOrderBy("ADDDATE DESC");}

		u.setPageCurrent(pageCurrent);
		u.setPageSize(pageSize);
		u.setPageCount(pageCount);

		List<User> userList;
		try {
			userList =userService.loadAllUsers(u);
			String pageHTML = PageUtil.getPageContent("user_{pageCurrent}_{pageSize}_{pageCount}?mobile="+u.getMobile()+"&orderBy="+u.getOrderBy(), pageCurrent, pageSize, pageCount);
			model.addAttribute("pageHTML",pageHTML);
			model.addAttribute("user",u);
			model.addAttribute("users",userList);

		} catch (Exception e) {

		}
		return "user_list";
	}


	/**
	 * 创建普通用户
	 * @return
	 */
	@GetMapping("/admin/userCreate")
	@PreAuthorize("hasAnyRole('SUPER')")
	public String addUser() {
		return "user_create";
	}


	@PostMapping(value = "/admin/userCreate")
	public String addUser(Model model, User user){
		try{
			User u=userService.findUserByMobile(user);
			if(u!=null){
				model.addAttribute("result", "手机号已存在！");
				return "user_create";
			}else{
				model.addAttribute("result", "用户添加成功！");
				userService.insertUser(user);
				return "user_create";
			}

		}catch (Exception e){
			LoggerUtil.W(e.getMessage());
			model.addAttribute("result", "添加失败！");
			return "user_create";
		}


	}



	///////////////////////////////////////////////////////

	@GetMapping("/admin/updateUser")
	@PreAuthorize("hasAnyRole('SUPER')")
	public String updateUser(Model model,User user) {


		if(user!=null&&user.getMobile()!=null){
			User u= userService.findUserByMobile(user);
			model.addAttribute("user",u);
		}

		return "user_update";
	}



	/**
	 * 更新普通用户
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/admin/updateUser")
	public String updateUser(@RequestParam MultipartFile[] imageFile, User user){

			if(imageFile.length>0) {
				for (MultipartFile file : imageFile) {
					if (file.isEmpty()) {
						LoggerUtil.W("无文件上传");
					} else {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
							Date date = new java.util.Date();
							String strDate = sdf.format(date);

							String fileName = strDate + file.getOriginalFilename().substring(
									file.getOriginalFilename().indexOf("."),
									file.getOriginalFilename().length());

							if(file.getInputStream()!=null) {

								byte[] byteArray = FileUtil.InputStreamTOByte(file.getInputStream());
								uploadService.uploadFile(fileName, byteArray,user);

							}

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			}

		    userService.updateUser(user);
			return "redirect:user_0_0_0";
	}

	/**
	 * 删除普通该用户
	 * @param model
	 * @param u
	 * @return
	 */

	@PostMapping(value = "/admin/userDel")
	@PreAuthorize("hasAnyRole('SUPER')")
	public String  deluser(Model model,User  u){

		try{
			u.setEnabled("2");
			userService.delUserById(u);
			//model.addAttribute("result","删除成功");
			return "redirect:user_0_0_0";
		}catch (Exception e){
			//model.addAttribute("result","删除失败");
			return "redirect:user_0_0_0";
		}
	}


	@GetMapping("/admin/403")
	public String accessDeny() {
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
