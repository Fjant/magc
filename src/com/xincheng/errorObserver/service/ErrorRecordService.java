package com.xincheng.errorObserver.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xincheng.errorObserver.model.ErrorObserver;
import com.xincheng.errorObserver.model.ErrorRecord;

public interface ErrorRecordService {
	public Integer saveRecord(Integer functionModularId , String operatorId , String errorMessage , String errorMemo , Integer errorLevel , Date errorTime);
	
	public List<ErrorObserver> pageQuery(Map<String, Object> filter);
	
	public Long pageCount(Map<String, Object> filter) throws Exception;
	
	public void updateState(Map<String, Object> filter) throws Exception;
	
	public void save(ErrorRecord record);
	
}
