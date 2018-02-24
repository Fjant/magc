package com.xincheng.msg.service;

import java.util.List;
import java.util.Map;

import com.xincheng.msg.model.MPMsg;

public interface MPMsgService {

	/**
	 * 保存单项企业号消息
	 * @throws Exception 
	 * */
	public void insert(MPMsg entity) throws Exception;

	/**
	 * 批量保存企业号消息
	 * @throws Exception 
	 * */
	public void insertBatch(final List<MPMsg> needs) throws Exception;

	/**
	 * 根据ID获取企业号消息详情
	 * @throws Exception 
	 * */
	public MPMsg getById(long id) throws Exception;

	/**
	 * 更新单项企业号消息
	 * @throws Exception 
	 * */
	public void update(MPMsg entity) throws Exception;

	/**
	 * 获取企业号消息列表
	 * @throws Exception 
	 * */
	public List<MPMsg> getList(Map<String, Object> filters) throws Exception;

	/**
	 * 分页获取企业号消息列表
	 * @throws Exception 
	 * */
	public List<MPMsg> getByPage(Map<String, Object> filters) throws Exception;

	/**
	 * 统计企业号消息数量
	 * 
	 * @throws Exception
	 * */
	public int count(Map<String, Object> filters) throws Exception;
	
	public void removeById(long id) throws Exception;
	
	public List<MPMsg> pageQuery(Map<String, Object> filters) throws Exception;
	
}
