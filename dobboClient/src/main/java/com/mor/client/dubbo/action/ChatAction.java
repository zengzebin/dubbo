package com.mor.client.dubbo.action;

import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dubbo.common.service.UserService;

 

public class ChatAction {
	/**
	 * 
	 * @author wanggengqi
	 * @date 2014年10月23日 下午3:13:04
	 */
	public void SayHello() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationConsumer.xml" });
		context.start();
		UserService userService = (UserService) context.getBean("userService");
		userService.test("你妹的");
	}

}
