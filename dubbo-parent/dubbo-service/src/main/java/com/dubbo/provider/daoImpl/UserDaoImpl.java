package com.dubbo.provider.daoImpl;

import org.springframework.stereotype.Repository;

import com.dubbo.common.dao.UserDao;
import com.dubbo.common.mapper.UserMapper;
import com.dubbo.common.model.User;
import com.dubbo.common.model.UserExample;
import com.dubbo.provider.base.daoImpl.IBaseDaoImpl;
@Repository
public class UserDaoImpl extends IBaseDaoImpl<UserMapper,UserExample,User> implements UserDao{


}
