/*package com.dubbo.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubbo.common.controller.BaseController;
import com.dubbo.common.service.UserService;

@Controller
@RequestMapping("user")
public class UserController extends BaseController{

	@Autowired
	private UserService userService ;
	
	@RequestMapping("getList")
	@ResponseBody
	public Object getList(){
		return userService.getById(1);
	}
}
*/