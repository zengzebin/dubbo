package com.dubbo.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubbo.common.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	UserService userService ;
	
	@RequestMapping("get")
	@ResponseBody
	public Object get(){
		return userService.getById(1);
	}
	
}
