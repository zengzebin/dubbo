package com.dubbo.utils.common;

import java.util.Calendar;
import java.util.Random;

public class DataUtil {

	public static synchronized int getNowYear(){
		Calendar now = Calendar.getInstance();  
		return now.get(Calendar.YEAR);
	}
	
	public static synchronized int getNowMonth(){
		Calendar now = Calendar.getInstance();  
		return now.get(Calendar.MONTH) + 1 ;
	}
	
	public static synchronized int getNowDay(){
		Calendar now = Calendar.getInstance();  
		return now.get(Calendar.DAY_OF_MONTH) ;
	}
	
	public static synchronized String getNowYMD(){
		return ""+getNowYear()+getNowMonth()+getNowDay();
	}
	/**
	 * 获取当前时间戳，一般用于文件命名
	 * @return
	 */
	public synchronized static String getTimstamp(){
		Random random = new Random(100);
		return (""+Calendar.getInstance().getTimeInMillis()+random.nextInt()).trim();
	}
}
