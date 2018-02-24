package com.xincheng.monitor.interceptors;

import java.util.Date;
import java.util.Map;
import java.util.UUID;



import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.xincheng.config.SystemConfig;
import com.xincheng.monitor.model.FunctionEntity;
import com.xincheng.monitor.service.FunctionService;

public class FuncInvokeInterceptor {

	@Autowired
	private FunctionService funcService;

	@Autowired
	private SystemConfig systemConfig;

	public static final Logger logger = Logger.getLogger(FuncInvokeInterceptor.class);

	public Object interceptMapParamsFunc(ProceedingJoinPoint call, Map<String, Object> filter) throws Throwable {
		String bizId = "";
		String agntNum = "";
		FunctionEntity funcEntity = new FunctionEntity();

		try {
			if (filter == null || !filter.containsKey("interceptBizId")) {
				bizId = UUID.randomUUID().toString().replace("-", "");
				filter.put("interceptBizId", bizId);
			} else {
				bizId = filter.get("interceptBizId").toString();
			}

			if (filter != null && filter.containsKey("agntNum")) {
				agntNum = (String) filter.get("agntNum");
			}
		} catch (Throwable ex) {
			funcEntity.setResult(1);
			throw ex;
		}

		funcEntity.setBizId(bizId);
		funcEntity.setAgntNum(agntNum);
		funcEntity.setFuncInterface(call.getSignature().toString());
		funcEntity.setStartTime(new Date());
		funcEntity.setFromServer(ServletActionContext.getRequest().getLocalAddr());
		Object result = null;

		try {
			result = call.proceed();
		} catch (Exception ex) {
			funcEntity.setResult(1);
			throw ex;
		} finally {
			funcEntity.setEndTime(new Date());
			funcEntity.setConsumeMillTime(funcEntity.getEndTime().getTime() - funcEntity.getStartTime().getTime());
			funcEntity.setCreateTime(new Date());
			funcService.save(funcEntity);
		}

		return result;
	}

	public Object interceptNonParamsFunc(ProceedingJoinPoint call) throws Throwable {
		String bizId = UUID.randomUUID().toString().replace("-", "");
		String agntNum = "";
		FunctionEntity funcEntity = new FunctionEntity();

		try {
			funcEntity.setBizId(bizId);
			funcEntity.setAgntNum(agntNum);
			funcEntity.setFuncInterface(call.getSignature().toString());
			funcEntity.setStartTime(new Date());
			funcEntity.setFromServer(ServletActionContext.getRequest().getLocalAddr());
		} catch (Throwable ex) {
			funcEntity.setResult(1);
		}

		Object result = null;

		try {
			result = call.proceed();
		} catch (Exception ex) {
			funcEntity.setResult(1);
			throw ex;
		} finally {
			funcEntity.setEndTime(new Date());
			funcEntity.setConsumeMillTime(funcEntity.getEndTime().getTime() - funcEntity.getStartTime().getTime());
			funcEntity.setCreateTime(new Date());
			funcService.save(funcEntity);
		}

		return result;
	}
}
