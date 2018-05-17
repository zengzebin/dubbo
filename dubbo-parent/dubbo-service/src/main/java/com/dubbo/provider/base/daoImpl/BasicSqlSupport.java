package com.dubbo.provider.base.daoImpl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class BasicSqlSupport {
	@Resource
	protected SqlSession session;
	@Resource
	protected SqlSessionFactory sqlSessionFactory;
	

    public SqlSession getSession() {
        return session;
    }
    public void setSession(SqlSession session) {
        this.session = session;
    }
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	} 
    
    
}
