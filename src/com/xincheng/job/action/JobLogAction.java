package com.xincheng.job.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import com.xincheng.job.model.JobLog;
import com.xincheng.job.service.JobLogService;
import com.xincheng.utils.JsonUtils;
import com.xincheng.utils.Struts2Utils;
import com.xincheng.web.BaseAction;

@Namespace("/job")
@Action(value = "joblog")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class JobLogAction extends BaseAction<JobLog>{

	private static final long serialVersionUID = 830849477910566761L;

	@Autowired
	private JobLogService jobLogService;
	
	public String list(){
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
		    filter = initPage(filter);
		    List<JobLog> job = jobLogService.getByPage(filter);
		    int count = jobLogService.count(filter);
		    page.setResult(job);
		    page.setTotalCount(count);
		} catch (Exception e) {
			logger.error("查询任务记录列表异常",e);
			e.printStackTrace();
		}finally{
			JsonUtils.renderJson(page);
		}
		return SUCCESS;
	}
	@Override
	public JobLog getModel() {
		// TODO Auto-generated method stub
		return entity;
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
