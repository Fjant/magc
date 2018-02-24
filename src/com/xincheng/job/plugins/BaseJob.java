package com.xincheng.job.plugins;

import com.xincheng.job.model.JobEntity;

public interface BaseJob {
	public void execute(JobEntity jobEntity);
}
