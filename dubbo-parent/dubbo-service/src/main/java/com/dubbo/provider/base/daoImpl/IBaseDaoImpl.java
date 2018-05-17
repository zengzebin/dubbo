package com.dubbo.provider.base.daoImpl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import com.dubbo.common.base.dao.IBaseDao;
import com.dubbo.common.base.mapper.BaseMapper;
@SuppressWarnings("unchecked")
public abstract class IBaseDaoImpl<TMapper,TExample,TPojo> extends BasicSqlSupport implements IBaseDao<TExample,TPojo>{

	
	public TMapper getMapper(){
		return this.session.getMapper(getClassType());
	}
	
	//获取传入mapper类型（即DAO类）
	
	public Class<TMapper> getClassType(){
		ParameterizedType pt = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		return(Class<TMapper>) pt.getActualTypeArguments()[0];
	}
	
	@Override
	public int countByExample(TExample example) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).countByExample(example);
	}

	@Override
	public int deleteByExample(TExample example) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).countByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(TPojo record) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).insert(record);
	}

	@Override
	public int insertSelective(TPojo record) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).insertSelective(record);
	}

	@Override
	public List<TPojo> selectByExample(TExample example) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).selectByExample(example);
	}

	@Override
	public TPojo selectByPrimaryKey(Integer id) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).selectByPrimaryKey(id);
	}

	@Override
	public int updateByExampleSelective(TPojo record, TExample example) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).updateByExampleSelective(record,example);
	}

	@Override
	public int updateByExample(TPojo record, TExample example) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).updateByExample(record,example);
	}

	@Override
	public int updateByPrimaryKeySelective(TPojo record) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TPojo record) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).updateByPrimaryKey(record);
	}

	@Override
	public List<Map<String, Object>> getMaps(Map<String, Object> map) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).getMaps(map);
	}

	@Override
	public List<TPojo> getList(Map<String, Object> map) {
		return ((BaseMapper<TExample, TPojo>)getMapper()).getList(map);
	}

}
