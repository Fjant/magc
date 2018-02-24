package com.xincheng.msg.service;

import java.util.List;
import java.util.Map;

import com.xincheng.msg.model.EntMsg;
import com.xincheng.msg.model.MPMsg;

public interface EntMsgService {

	/**
	 * 保存单项企业号消息
	 * @throws Exception 
	 * */
	public void insert(EntMsg entity) throws Exception;

	/**
	 * 批量保存企业号消息
	 * @throws Exception 
	 * */
	public void insertBatch(final List<EntMsg> needs) throws Exception;

	/**
	 * 根据ID获取企业号消息详情
	 * @throws Exception 
	 * */
	public EntMsg getById(long id) throws Exception;

	/**
	 * 更新单项企业号消息
	 * @throws Exception 
	 * */
	public void update(EntMsg entity) throws Exception;

	/**
	 * 获取企业号消息列表
	 * @throws Exception 
	 * */
	public List<EntMsg> getList(Map<String, Object> filters) throws Exception;

	/**
	 * 分页获取企业号消息列表
	 * @throws Exception 
	 * */
	public List<EntMsg> getByPage(Map<String, Object> filters) throws Exception;

	/**
	 * 统计企业号消息数量
	 * 
	 * @throws Exception
	 * */
	public int count(Map<String, Object> filters) throws Exception;
	
	public void removeById(long id) throws Exception;
}
