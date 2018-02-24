package com.xincheng.errorObserver.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.ibatis.MyBatisEntityDao;
@Repository
public class ErrorRecordDao extends MyBatisEntityDao<ErrorRecord, Long> {
	
	Logger logger = LoggerFactory.getLogger(ErrorRecordDao.class);
	
	/**
	 * 保存异常记录
	 * @return 返回影响了几行数据
	 * @throws Exception 
	 */
	public Integer saveRecord(Map<String, Object> filter) throws Exception{
		return getSqlSessionTemplate().insert("ErrorRecord.insert", filter);
	}
	
	/**
	 * 更新异常记录
	 * @throws Exception 
	 */
	public void updateRecord(Map<String, Object> filter) throws Exception{
		getSqlSessionTemplate().update("ErrorRecord.update", filter);
	}
	
	/**
	 * 更新异常记录状态
	 * @throws Exception 
	 */
	public void updateState(Map<String, Object> filter) throws Exception{
		getSqlSessionTemplate().update("ErrorRecord.updateState", filter);
	}
	
	/**
	 * 删除异常记录
	 * @throws Exception 
	 */
	public void removeById(long id) throws Exception {
		getSqlSessionTemplate().delete("ErrorRecord.deleteById", id);
	}

	/**
	 * 根据ID获取异常记录
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public ErrorRecord getById(long id) throws Exception{
		return (ErrorRecord) getSqlSessionTemplate().selectOne("ErrorRecord.getById", id);
	}
	/**
	 * 分页查询异常记录
	 * @param filter  			
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<ErrorRecord> pageQuery(Map<String, Object> filter) throws Exception {
		
		return getSqlSessionTemplate().selectList("ErrorRecord.pageQuerys", filter);
	}
	/**
	 * 分页的总数据条数
	 * @param filter
	 * @return
	 * @throws Exception 
	 */
	public Long pageCount(Map<String, Object> filter) throws Exception {
		
		return (Long) getSqlSessionTemplate().selectOne("ErrorRecord.pageCount", filter);
	}
	/**
	 * 获取8级以上的异常
	 * @param filter
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<ErrorRecord> selectErrorByLevel(Map<String, Object> filter) throws Exception {
		
		return getSqlSessionTemplate().selectList("ErrorRecord.selectErrorByLevel", filter);
	}
}
