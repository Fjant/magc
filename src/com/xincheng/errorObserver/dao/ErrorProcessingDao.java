package com.xincheng.errorObserver.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.errorObserver.model.ErrorProcessing;
import com.xincheng.errorObserver.model.FunctionModular;
import com.xincheng.ibatis.MyBatisEntityDao;

@Repository
public class ErrorProcessingDao extends MyBatisEntityDao<ErrorProcessing, Long> {
	
	/**
	 * 保存异常处理记录
	 * @throws Exception 
	 */
	public Integer saveProcessing(Map<String, Object> filter) throws Exception{
		return getSqlSessionTemplate().insert("ErrorProcessing.insert", filter);
	}
	
	/**
	 * 更新异常处理记录
	 * @throws Exception 
	 */
	public void updateProcessing(Map<String, Object> filter) throws Exception{
		getSqlSessionTemplate().update("ErrorProcessing.update", filter);
	}
	
	/**
	 * 删除异常处理记录
	 * @throws Exception 
	 */
	public void removeById(long id) throws Exception {
		getSqlSessionTemplate().delete("ErrorProcessing.deleteById", id);
	}

	/**
	 * 根据ID获取异常处理记录
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public ErrorProcessing getById(long id) throws Exception{
		return (ErrorProcessing) getSqlSessionTemplate().selectOne("ErrorProcessing.getById", id);
	}
	/**
	 * 分页查询异常处理记录
	 * @param filter  			
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<ErrorProcessing> pageQuery(Map<String, Object> filter) throws Exception {
		
		return getSqlSessionTemplate().selectList("ErrorProcessing.pageQuerys", filter);
	}
	/**
	 * 根据异常ID获取处理记录
	 * @param errorId  			
	 * @return
	 * @throws Exception 
	 */
	public ErrorProcessing selectByErrorId(Map<String,String> param) throws Exception {
		
		return (ErrorProcessing) getSqlSessionTemplate().selectOne("ErrorProcessing.selectByErrorId", param);
	}
	/**
	 * 分页的总数据条数
	 * @param filter
	 * @return
	 * @throws Exception 
	 */
	public Long pageCount(Map<String, Object> filter) throws Exception {
		
		return (Long) getSqlSessionTemplate().selectOne("ErrorProcessing.pageCount", filter);
	}
	
	/**
	 * 获取功能模块
	 * @param filter
	 * @return
	 * @throws Exception 
	 */
	public FunctionModular selectFunctionModular(Integer id) throws Exception {
		
		return (FunctionModular) getSqlSessionTemplate().selectOne("FunctionModular.getById", id);
	}
}
