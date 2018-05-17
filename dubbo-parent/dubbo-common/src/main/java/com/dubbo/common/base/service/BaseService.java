package com.dubbo.common.base.service;

import java.util.Map;

import com.dubbo.common.base.page.IGenericPage;

public interface BaseService<T> {

	public IGenericPage<T> findPage(Integer pageNo, Integer pageSize,T param , Map<String,Object> map);
	
	public Integer add(T t);
	
	public Integer update(T t);
	
	public Integer delete(Integer id);
	

	public T getById(Integer id);
	
	public void test(String name);
}
