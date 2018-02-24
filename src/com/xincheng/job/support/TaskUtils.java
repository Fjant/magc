package com.xincheng.job.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.xincheng.job.model.JobEntity;
import com.xincheng.utils.SpringUtils;

public class TaskUtils {
	public final static Logger log = Logger.getLogger(TaskUtils.class);

	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param jobEntity
	 */
	public static void invokMethod(JobEntity jobEntity) {
		Object object = null;
		Class clazz = null;
		if (StringUtils.isNotBlank(jobEntity.getClassPath())) {
			try {
				clazz = Class.forName(jobEntity.getClassPath());
				object = SpringUtils.getBean(clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (object == null) {
			log.error("任务名称 = [" + jobEntity.getName() + "]---------------未启动成功，请检查是否配置正确！！！");
			return;
		}

		Method method = null;
		try {
			method = clazz.getDeclaredMethod(jobEntity.getMethod(), JobEntity.class);
		} catch (NoSuchMethodException e) {
			log.error("任务名称 = [" + jobEntity.getName() + "]---------------未启动成功，方法名设置错误！！！");
		} catch (SecurityException e) {
			log.error("任务名称 = [" + jobEntity.getName() + "]---------------未启动成功，方法名设置错误！！！", e);
		}

		if (method != null) {
			try {
				method.invoke(object, jobEntity);
				System.out.println("任务名称 = [" + jobEntity.getName() + "]----------启动成功");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				log.error("任务名称 = [" + jobEntity.getName() + "]---------------运行中出错", e);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				log.error("任务名称 = [" + jobEntity.getName() + "]---------------运行中出错", e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				log.error("任务名称 = [" + jobEntity.getName() + "]---------------运行中出错", e);
			}
		}
	}
}
