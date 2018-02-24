package com.xincheng.errorObserver.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.config.Communication;
import com.xincheng.errorObserver.dao.ErrorProcessingDao;
import com.xincheng.errorObserver.dao.ErrorRecordDao;
import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.service.ErrorProcessingService;
import com.xincheng.utils.SendEmail;
@Service
public class ErrorProcessingServiceImpl implements ErrorProcessingService {
	
	Logger logger = LoggerFactory.getLogger(ErrorProcessingServiceImpl.class);
	
	@Autowired
	private ErrorProcessingDao errorProcessingDao;
	
	@Autowired
	private ErrorRecordDao errorRecordDao;
	
	@Autowired
	private Communication communication;
	
	@Autowired
	SendEmail sendEmail;
	
	@Override
	public void observer() { 
		HashMap<String,Object> map = null;
		logger.info("########### 正在查看是否有待处理的异常! ##########");
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("errorLevel", 8);
		filter.put("state", 0);
		List<ErrorRecord> list;
		try {
			list = errorRecordDao.selectErrorByLevel(filter);
		} catch (Exception e1) {
			return;
		}
		long[] ids = new long[list.size()];
		if(list != null && list.size() > 0){
			StringBuffer memo = new StringBuffer();
			StringBuffer buf_id = new StringBuffer();
			for(int i = 0; i < list.size(); i++){
				ErrorRecord record = list.get(i);
				ids[i] = record.getId();
				if(i == list.size()-1){
					memo.append((i+1)+"："+record.getErrorMemo());
				}else{
					memo.append((i+1)+"："+record.getErrorMemo()+"<br/>");
				}
				buf_id.append("("+record.getId()+")");
			}
			String sendContent = memo.toString();
			String errorIdArr = buf_id.toString();  
			
			try {
				boolean isSend = sendEmail.send(communication,sendContent);
				if(isSend){
					map = new HashMap<String, Object>();
					map.put("errorIdArr", errorIdArr);
					map.put("sendContent", sendContent);
					map.put("notifierEmail", communication.getNotifierEmail());
					map.put("notifierEmailResult", "已发送邮件");
					map.put("createTime", new Date());
					errorProcessingDao.saveProcessing(map);
					filter = new HashMap<String, Object>();
					filter.put("state", 1);
					filter.put("ids", ids);
					errorRecordDao.updateState(filter);
				}else{
					map = new HashMap<String, Object>();
					map.put("errorIdArr", errorIdArr);
					map.put("sendContent", sendContent);
					map.put("notifierEmail", communication.getNotifierEmail());
					map.put("notifierEmailResult", "发送邮件失败");
					map.put("createTime", new Date());
					errorProcessingDao.saveProcessing(map);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				filter = new HashMap<String, Object>();
				filter.put("functionModularId",5);
				filter.put("operatorId",null);
				filter.put("errorMessage",e.getMessage()+"");
				filter.put("errorMemo","异常监控触发器发生异常,异常时间:【"+new Date()+"】,异常信息 = "+e.getMessage());
				filter.put("errorLevel",10);
				filter.put("errorTime",new Date());
				try {
					errorRecordDao.saveRecord(filter);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			
		}
	}
}
