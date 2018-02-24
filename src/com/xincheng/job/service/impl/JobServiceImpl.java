package com.xincheng.job.service.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.exceptions.JobIllegalArgumentException;
import com.xincheng.job.dao.JobDao;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.service.JobService;
import com.xincheng.utils.SpringUtils;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	private JobDao taskDao;
	
	@Override
	public void save(JobEntity entity) throws Exception {
		checkJobParams(entity);

		taskDao.insert(entity);
	}

	private void checkJobParams(JobEntity entity) throws ClassNotFoundException, NoSuchMethodException {
		Class clazz = null;
		if (StringUtils.isEmpty(entity.getClassPath())) {
			throw new JobIllegalArgumentException("类名不能为空");
		} else {
			
			try {
				clazz = Class.forName(entity.getClassPath().trim());
				Object bean = SpringUtils.getBean(clazz);
				if (bean == null) {
					throw new JobIllegalArgumentException("类文件不存在");
				}
			} catch (ClassNotFoundException e) {
				throw new JobIllegalArgumentException("类文件不存在");
			}
		}

		if (StringUtils.isEmpty(entity.getMethod())) {
			throw new JobIllegalArgumentException("方法名不能为空");
		} else {
			try {		
				Method method = clazz.getDeclaredMethod(entity.getMethod().trim(),entity.getClass());
				if (method == null) {
					throw new JobIllegalArgumentException("方法名不存在");	
				}
			} catch (NoSuchMethodException e) {				
					throw new JobIllegalArgumentException("方法名不存在");	
			}
		}

		if (StringUtils.isEmpty(entity.getCronExpression())) {
			throw new JobIllegalArgumentException("运行时间表达式不能为空");
		} else {
			if (!CronExpression.isValidExpression(entity.getCronExpression().trim())) {
				throw new JobIllegalArgumentException("运行时间表达式不正确");
			}
		}
	}

	@Override
	public JobEntity getById(long id) throws Exception {
		return taskDao.get(id);
	}

	@Override
	public void update(JobEntity entity) throws Exception {
		checkJobParams(entity);
		
		taskDao.update(entity);
	}

	@Override
	public List<JobEntity> getAll() throws Exception {
		return taskDao.getAll();
	}

	@Override
	public List<JobEntity> getByPage(Map<String, Object> filters) throws Exception {
		return taskDao.getByPage(filters);
	}

	@Override
	public int count(Map<String, Object> filters) throws Exception {
		return taskDao.count(filters);
	}

	@Override
	public void startJob(long id) throws Exception{
		taskDao.startJob(id);

	}

	@Override
	public void stopJob(long id) throws Exception{
		taskDao.stopJob(id);

	}

	@Override
	public void removeById(long id) throws Exception {
		taskDao.removeById(id);
		
	}

	@Override
	public int selectCount(Map<String, Object> filters) throws Exception {
		// TODO Auto-generated method stub
		return taskDao.selectCount(filters);
	}

}
