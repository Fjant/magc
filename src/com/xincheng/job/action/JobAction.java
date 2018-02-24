package com.xincheng.job.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;
import com.xincheng.exceptions.JobIllegalArgumentException;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.service.JobService;
import com.xincheng.job.support.service.JobTaskService;
import com.xincheng.utils.JsonUtils;
import com.xincheng.utils.Struts2Utils;
import com.xincheng.utils.UserOperateUtils;
import com.xincheng.web.BaseAction;

@Namespace("/job")
@Action(value = "jobinfo")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class JobAction extends BaseAction<JobEntity> {

	private static final long serialVersionUID = 5213563736102628351L;

	@Autowired
	private JobService jobService;

	@Autowired
	private JobTaskService jobTaskService;

	public String list() {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			filter = initPage(filter);
			List<JobEntity> job = jobService.getByPage(filter);
			int count = jobService.count(filter);
			page.setResult(job);
			page.setTotalCount(count);
		} catch (Exception e) {
			logger.error("查询任务信息列表出错!", e);
			e.printStackTrace();
		} finally {
			JsonUtils.renderJson(page);
		}
		return SUCCESS;
	}

	// 运行任务
	public String startJob() {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			String id = (String) filter.get("id");
			if (StringUtils.isEmpty(id)) {
				this.retData.setMessage("没有任务ID，请重新操作");
			} else {
				jobService.startJob(Long.parseLong(id));
				JobEntity job = jobService.getById(Long.parseLong(id));
				jobTaskService.resumeJob(job);
				this.retData.setRetCode("1");
			}
		} catch (Exception e) {
			logger.error("运行任务异常", e);
		} finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}

	// 停止任务
	public String stopJob() {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			String id = (String) filter.get("id");
			if (StringUtils.isEmpty(id)) {
				this.retData.setMessage("没有任务ID，请重新操作");
			} else {
				jobService.stopJob(Long.parseLong(id));
				JobEntity job = jobService.getById(Long.parseLong(id));
				jobTaskService.pauseJob(job);
				this.retData.setRetCode("1");
			}
		} catch (Exception e) {
			logger.error("停止任务异常", e);
		} finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}

	@Override
	public JobEntity getModel() {
		// TODO Auto-generated method stub
		return entity;
	}

	@Override
	public String save() throws Exception {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			int num = jobService.selectCount(filter);
			if(num < 1){				
				JobEntity job = new JobEntity();
				String name = (String) filter.get("name");
				String incTypeId = (String) filter.get("incTypeId");
				String group = (String) filter.get("group");
				String classPath = (String) filter.get("classPath");
				String method = (String) filter.get("method");
				String argument = (String) filter.get("argument");
				String isConcurrent = (String) filter.get("isConcurrent");
				String cronExpression = (String) filter.get("cronExpression");
				String statusId = (String) filter.get("statusId");
				String remark = (String) filter.get("remark");
				job.setName(name);
				job.setIncTypeId(Integer.parseInt(incTypeId));
				job.setGroup(group);
				job.setClassPath(classPath);
				job.setMethod(method);
				job.setArgument(argument);
				job.setIsConcurrent(Integer.parseInt(isConcurrent));
				job.setCronExpression(cronExpression);
				job.setStatusId(Integer.parseInt(statusId));
				job.setRemark(remark);
				job.setCreateBy(UserOperateUtils.getUserId() == null ? "-1" : UserOperateUtils.getUserId());
				jobService.save(job);
				jobTaskService.addJob(job);
				this.retData.setRetCode("1");
			}else{	
				this.retData.setMessage("增量类型已经存在");
				this.retData.setRetCode("2");
			}
		} catch (JobIllegalArgumentException argsEx) {
			this.retData.setMessage(argsEx.getMessage());
			this.retData.setRetCode("-1");
		} catch (Exception e) {
			logger.error("保存任务消息异常", e);
			this.retData.setRetCode("0");
		} finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}

	public String getById() {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			String id = (String) filter.get("id");
			if (StringUtils.isEmpty(id)) {
				this.retData.setMessage("没有任务ID，请重新操作");
			} else {
				JobEntity job = jobService.getById(Long.parseLong(id));
				this.retData.setReturnData(job);
				this.retData.setRetCode("1");
			}
		} catch (Exception e) {
			logger.error("获取任务信息失败", e);
			this.retData.setRetCode("0");
		} finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}

	@Override
	public String delete() throws Exception {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			String id = null;
			String[] ids = null;
			if (filter.get("ids[]") instanceof String) {
				id = (String) filter.get("ids[]");
			} else {
				ids = (String[]) filter.get("ids[]");
			}
			JobEntity job = null;
			if (null != id) {
				// 先查询任务
				job = jobService.getById(Long.parseLong(id));
				// 再执行删除操作
				jobService.removeById(Long.parseLong(id));
				// 删除操作成功再执行任务终止
				jobTaskService.deleteJob(job);
			}
			if (null != ids) {
				for (String jobid : ids) {
					// 先查询任务
					job = jobService.getById(Long.parseLong(jobid));
					// 再执行删除操作
					jobService.removeById(Long.parseLong(jobid));
					// 删除操作成功再执行任务终止
					jobTaskService.deleteJob(job);
				}
			}
			retData.setRetCode("1");
		} catch (Exception e) {
			logger.error("删除消息模板信息失败", e);
			this.retData.setRetCode("0");
		} finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}

	@Override
	public String update() throws Exception {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			int num = jobService.selectCount(filter);
			if(num < 1){				
				JobEntity job = new JobEntity();
				String id = (String) filter.get("id");
				String name = (String) filter.get("name");
				String incTypeId = (String) filter.get("incTypeId");
				String group = (String) filter.get("group");
				String classPath = (String) filter.get("classPath");
				String method = (String) filter.get("method");
				String argument = (String) filter.get("argument");
				String isConcurrent = (String) filter.get("isConcurrent");
				String cronExpression = (String) filter.get("cronExpression");
				String statusId = (String) filter.get("statusId");
				String remark = (String) filter.get("remark");
				job.setId(Long.parseLong(id));
				job.setName(name);
				job.setIncTypeId(Integer.parseInt(incTypeId));
				job.setGroup(group);
				job.setClassPath(classPath);
				job.setMethod(method);
				job.setArgument(argument);
				job.setIsConcurrent(Integer.parseInt(isConcurrent));
				job.setCronExpression(cronExpression);
				job.setStatusId(Integer.parseInt(statusId));
				job.setRemark(remark);
				
				// 先查询任务
				JobEntity oldJob = jobService.getById(job.getId());
				// 更新任务
				jobService.update(job);
				// 先删除旧任务
				jobTaskService.deleteJob(oldJob);
				// 再添加新任务
				jobTaskService.addJob(job);
				
				this.retData.setRetCode("1");
			}else{
				this.retData.setMessage("增量类型已经存在");
				this.retData.setRetCode("2");
			}
		} catch (JobIllegalArgumentException argsEx) {
			this.retData.setMessage(argsEx.getMessage());
			this.retData.setRetCode("-1");
		} catch (Exception e) {
			logger.error("更新任务消息异常", e);
			this.retData.setRetCode("0");
		} finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

}
