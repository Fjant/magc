package com.xincheng.errorObserver.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.errorObserver.dao.ErrorProcessingDao;
import com.xincheng.errorObserver.dao.ErrorRecordDao;
import com.xincheng.errorObserver.model.ErrorObserver;
import com.xincheng.errorObserver.model.ErrorProcessing;
import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.model.FunctionModular;
import com.xincheng.errorObserver.service.ErrorRecordService;
@Service
public class ErrorRecordServiceImpl implements ErrorRecordService {

	Logger logger = LoggerFactory.getLogger(ErrorRecordServiceImpl.class);
	
	@Autowired
	private ErrorRecordDao errorRecordDao;
	@Autowired
	private ErrorProcessingDao errorProcessingDao;
	
	
	/**
	 * @param 
	 * 		functionModularId   功能模块ID                  NO NULL
	 *  	operatorId          操作人ID                   IS NULL
	 *  	errorMessage  		程序的异常信息 e.getMessage()  NO NULL
	 *  	errorMemo     		异常描述,要发送给管理员的信息                       NO NULL
	 *  	errorLevel	      	异常等级,1-10级,级别越高,问题越严重,超过8级的异常将会通知管理员           NO NULL
	 *  	errorTime     		发生异常的时间                                                       NO NULL
	 * @return 
	 * 		-1 参数不正确 
	 * 		0 保存失败  
	 * 		1 保存成功 
	 */
	@Override
	public Integer saveRecord(Integer functionModularId , String operatorId , String errorMessage , String errorMemo , Integer errorLevel , Date errorTime) {
		logger.info("errorObserver  #################  保存异常开始");
		Map<String, Object> filter = new HashMap<String, Object>();
		Integer row = 0;
		if(functionModularId != null && checkStringIsNull(errorMessage) && checkStringIsNull(errorMemo) && errorLevel != null && (errorLevel >= 1 || errorLevel <= 10) && errorTime != null){
			logger.info("errorObserver  #################  参数正常");
			try {
				filter.put("functionModularId",functionModularId);
				filter.put("operatorId",operatorId);
				filter.put("errorMessage",errorMessage);
				filter.put("errorMemo",errorMemo);
				filter.put("errorLevel",errorLevel);
				filter.put("errorTime",errorTime);
				
				row = errorRecordDao.saveRecord(filter);
				
			} catch (Exception e) { 
				logger.info("errorObserver  #################  调用数据库异常 = "+e.getMessage());
				e.printStackTrace();
				return row;
			}
			logger.info("errorObserver  #################  保存异常信息正常");
			return row;
		}else{
			logger.info("errorObserver  #################  参数不正确");
			return -1;
		}
	}

	private boolean checkStringIsNull(String param){
		if(param != null && !"".equals(param.trim()) ){
			return true;
		}
		return false;
	}

	
	@Override
	public List<ErrorObserver> pageQuery(Map<String, Object> filter) {
		List<ErrorRecord> list;
		try {
			list = errorRecordDao.pageQuery(filter);
		} catch (Exception e) {
			return null;
		}
		List<ErrorObserver> ObserverList = new ArrayList<ErrorObserver>();
		for(ErrorRecord error : list){
			ErrorObserver observer = new ErrorObserver();
			FunctionModular fun;
			try {
				fun = errorProcessingDao.selectFunctionModular(error.getFunctionModularId());
			} catch (Exception e) {
				continue;
			}
			Map<String,String> param = new HashMap<String, String>();
			param.put("errorId", "("+error.getId()+")");
			ErrorProcessing processing;
			try {
				processing = errorProcessingDao.selectByErrorId(param);
			} catch (Exception e) {
				continue;
			}
			observer.setId(error.getId());
			if(fun != null){
				observer.setFunctionModular(fun.getName());
			}
			observer.setOperatorId(error.getOperatorId());
			observer.setErrorMessage(error.getErrorMessage());
			observer.setErrorMemo(error.getErrorMemo());
			observer.setErrorLevel(error.getErrorLevel());
			observer.setErrorTime(error.getErrorTime());
			observer.setState(error.getState());
			if(processing != null){
				observer.setSendContent(processing.getSendContent());
				observer.setNotifierPhone(processing.getNotifierPhone());
				observer.setNotifierEmail(processing.getNotifierEmail());
				observer.setNotifierWeixin(processing.getNotifierWeixin()+"");
			}
			ObserverList.add(observer);
		}
		return ObserverList;
	}

	@Override
	public Long pageCount(Map<String, Object> filter) throws Exception {
		// TODO Auto-generated method stub
		return errorRecordDao.pageCount(filter);
	}

	@Override
	public void updateState(Map<String, Object> filter) throws Exception {
		// TODO Auto-generated method stub
		errorRecordDao.updateState(filter);
	}  

	@Override
	public void save(ErrorRecord record) {

		if (record.getFunctionModularId() != null && checkStringIsNull(record.getErrorMessage()) && record.getErrorLevel() != null && (record.getErrorLevel() >= 1 || record.getErrorLevel() <= 10) && record.getErrorTime() != null) {
			try {
				errorRecordDao.insert(record);
			} catch (Exception e) {
				logger.error("保存监控记录失败。", e);
			}
		}
	}

}
