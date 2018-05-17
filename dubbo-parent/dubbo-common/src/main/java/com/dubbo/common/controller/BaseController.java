package com.dubbo.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dubbo.common.base.controller.BaseException;

public abstract class BaseController {

	private static Logger logger ;

	protected void setLogger(Logger outLogger){
		logger = outLogger ;
	}
	
	
	//全局异常处理
	@ExceptionHandler
	public String exception(HttpServletRequest request,
			HttpServletResponse response, Exception e) {
		// 这里进行通用处理，如日志记录�?..
		StackTraceElement[] es = e.getStackTrace();
		for (StackTraceElement s : es) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			logger.info(df.format(new Date()) + "--" + s.toString());
		}
		// 如果是json格式的ajax请求
		if (request.getHeader("accept").indexOf("application/json") > -1
				|| (request.getHeader("X-Requested-With") != null && request
						.getHeader("X-Requested-With")
						.indexOf("XMLHttpRequest") > -1)) {
			response.setStatus(500);
			response.setContentType("application/json;charset=utf-8");
			try {
				PrintWriter writer = response.getWriter();
				writer.write(e.getMessage());
				writer.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		} else {// 如果是普通请�?
			request.setAttribute("exceptionMessage", e.getMessage());
			// 根据不同的异常类型可以返回不同界�?
			if (e instanceof SQLException || e instanceof BaseException) {
				return "error/testerror";
			} // ...
			else {
				return "error";
			}
		}
	}
}
// 异常类型，需要再添加
// try {
//
// result = actionInvocation.invoke();
//
// } catch (DataAccessException ex) {
//
// throw new SystemException("数据库操作失败！");
//
// } catch (NullPointerException ex) {
//
// throw new SystemException("空指针，调用了未经初始化或�?是不存在的对象！");
//
// } catch (IOException ex) {
//
// throw new SystemException("IO读写异常�?);
//
// } catch (ClassNotFoundException ex) {
//
// throw new SystemException("指定的类不存在！");
//
// } catch (ArithmeticException ex) {
//
// throw new SystemException("数学运算异常�?);
//
// } catch (ArrayIndexOutOfBoundsException ex) {
//
// throw new SystemException("数组下标越界�?);
//
// } catch (IllegalArgumentException ex) {
//
// throw new SystemException("调用方法的参数错误！");
//
// } catch (ClassCastException ex) {
//
// throw new SystemException("类型强制转换错误�?);
//
// } catch (SecurityException ex) {
//
// throw new SystemException("违背安全原则异常�?);
//
// } catch (SQLException ex) {
//
// throw new SystemException("操作数据库异常！");
//
// } catch (NoSuchMethodError ex) {
//
// throw new SystemException("调用了未定义的方法！");
//
// } catch (InternalError ex) {
//
// throw new SystemException("Java虚拟机发生了内部错误�?);
//
// } catch (Exception ex) {
//
// throw new SystemException("程序内部错误，操作失败！");
//
// }