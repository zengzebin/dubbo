package com.dubbo.common.base.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface IBaseDao<Example,Pojo> {

    /**
     * 根据条件获取总数
     * @param example
     * @return
     */
    int countByExample(Example example);

    /**
     * 根据条件删除
     * @param example
     * @return
     */
    int deleteByExample(Example example);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增
     * @param record
     * @return
     */
    int insert(Pojo record);

    /**
     * 新增
     * @param record
     * @return
     */
    int insertSelective(Pojo record);

    /**
     * 根据pojo条件获取列表
     * @param example
     * @return
     */
    List<Pojo> selectByExample(Example example);

    /**
     * 根据id获取pojo
     * @param id
     * @return
     */
    Pojo selectByPrimaryKey(Integer id);

    /**
     * 根据条件修改（ 只修改pojo中有值的列）
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") Pojo record, @Param("example") Example example);

    /**
     * 根据条件修改
     * @param record
     * @param example
     * @return
     */ 
    int updateByExample(@Param("record") Pojo record, @Param("example") Example example);

    /**
     * 根据id修改（ 只修改pojo中有值的列）
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Pojo record);

    /**
     * 根据id修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(Pojo record);
    
    /**
     * 通过多个条件获取map列表
     * @param map
     * @return
     */
    List<Map<String,Object>> getMaps(Map<String,Object> map);
    /**
     * 通过多个条件获取pojo     * @return
     */
    List<Pojo> getList(Map<String,Object> map);
    
}
