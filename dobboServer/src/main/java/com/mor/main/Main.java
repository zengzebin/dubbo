/**
 * 
 */
package com.mor.main;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wanggengqi
 * @email wanggengqi@chinasofti.com
 * @date 2014年10月23日 下午1:56:05
 */
public class Main {
	private static Log logger = LogFactory.getLog(Main.class);

	/**
	 * @author wanggengqi
	 * @date 2014年10月23日 下午1:56:05
	 * @param args
	 * @return void
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * ClassPathXmlApplicationContext context = new
		 * ClassPathXmlApplicationContext(new String[] {
		 * "applicationProvider.xml" }); context.start();
		 * System.out.println("按任意键退出"); System.in.read();
		 */
		System.out.println("=======================");
		System.out.println("        Core包启动          ");
		SystemDetails.outputDetails();
		System.out.println("=======================");

		getLocalip();
		// 初始化spring
		logger.info("开始初始化core服务");
		BeanFactoryUtil.init();

		try {
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得本机ip地址 注意：Spring RmiServiceExporter取得本机ip的方法：InetAddress.getLocalHost()
	 */
	private static void getLocalip() {
		try {
			System.out.println("服务暴露的ip: " + java.net.InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
