package com.mor.client.dubbo.main;

import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dubbo.common.service.UserService;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		/*
		 * ApplicationContext ctx = new
		 * ClassPathXmlApplicationContext("applicationConsumer.xml"); DemoServer
		 * demoServer = (DemoServer) ctx.getBean("demoService");
		 * System.out.println("client:"+demoServer.sayHello("Wanggq"+"1:"+new
		 * Date())+"3:"+new Date());
		 */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationConsumer.xml" });
		context.start();
		int i = 0;
		while (i++ < 100) {
			UserService userService = (UserService) context.getBean("userService");
			userService.test(null);
			Thread.sleep(3000);
		}
	}

}
