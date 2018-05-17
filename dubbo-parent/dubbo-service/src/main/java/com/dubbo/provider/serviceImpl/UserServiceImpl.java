package com.dubbo.provider.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dubbo.common.base.page.IGenericPage;
import com.dubbo.common.dao.UserDao;
import com.dubbo.common.model.User;
import com.dubbo.common.service.UserService;

public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao ;
	
	@Override
	public IGenericPage<User> findPage(Integer pageNo, Integer pageSize,
			User param, Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer add(User t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(User t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delete(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getById(Integer id) {
		return userDao.selectByPrimaryKey(id);
	}

	@Override
	public void test(String name) {
		// TODO Auto-generated method stub
		System.out.println("############各位大家好呀############");
	}

}
