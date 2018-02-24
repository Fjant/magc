package com.xincheng.msg.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import com.xincheng.msg.model.SumMsg;
import com.xincheng.msg.service.SumMsgService;
import com.xincheng.utils.JsonUtils;
import com.xincheng.utils.Struts2Utils;
import com.xincheng.web.BaseAction;

@Namespace("/msg")
@Action(value ="sumMsg")
@Results( { @Result(name=BaseAction.SUCCESS, type = "json") })
public class SumMsgAction extends BaseAction<SumMsg>{

	private static final long serialVersionUID = -7635201428502035211L;
	
	@Autowired
	private SumMsgService sumMsgService;

	@Override
	public SumMsg getModel() {
		// TODO Auto-generated method stub
		return entity;
	}

	public String list(){
		Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
		try {
			Map<String,Object> list = new HashMap<String,Object>();
			SumMsg ent = sumMsgService.selectEntMsgRate(filter);
			SumMsg mp = sumMsgService.selectMPMsgRate(filter);
			list.put("ent", ent);
			list.put("mp", mp);
			retData.setRetCode("1");
			retData.setReturnData(list);
		} catch (Exception e) {
			logger.error("查询出错", e);
		}finally{
			JsonUtils.renderJson(retData);
		}
		
		return SUCCESS;
	}
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
