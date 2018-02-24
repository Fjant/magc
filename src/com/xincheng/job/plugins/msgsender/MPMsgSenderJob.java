package com.xincheng.job.plugins.msgsender;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.xincheng.config.SystemConfig;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.service.JobLogService;
import com.xincheng.msg.model.MPMsg;
import com.xincheng.msg.service.EntMsgService;
import com.xincheng.msg.service.MPMsgService;
import com.xincheng.utils.DateUtils;
import com.xincheng.wx.msgcsender.MPMsgSender;

@Service
public class MPMsgSenderJob implements BaseJob {
	private static Logger logger = LogManager.getLogger(MPMsgSenderJob.class);
	private static ReentrantReadWriteLock rtw = new ReentrantReadWriteLock();
	private static List<String> MSG_ERROR_CODES = Arrays.asList(SystemConfig.getPara("MPMsgErrorCode").split(","));

	@Autowired
	private EntMsgService entMsgService;

	@Autowired
	private MPMsgService mpMsgService;

	@Autowired
	private JobLogService joblogService;

	public void main(String[] args) {

	}

	/**
	 * 在指定时间段内发送消息
	 * */
	private boolean sendMsgAt(int startHour, int startMin, int endHour, int endMin) {

		if ((startHour == 0 && startMin == 0 && endHour == 0 && endMin == 0)
				|| (DateUtils.addMinutes(DateUtils.addHours(DateUtils.startOfDate(new Date()), startHour), startMin).getTime() <= DateUtils.addHours(new Date(), 0).getTime() && DateUtils.addMinutes(DateUtils.addHours(DateUtils.startOfDate(new Date()), endHour), endMin).getTime() >= DateUtils.addHours(new Date(), 0)
						.getTime())) {
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("不再发送消息的指定时间段内，消息发送推迟……");
			}
			return false;
		}
	}

	@Override
	public void execute(JobEntity jobEntity) {
		// 判断是否需要对发送时间进行控制
		if (StringUtils.isNotEmpty(jobEntity.getArgument())) {
			String[] times = jobEntity.getArgument().split(",");
			boolean isContinue = sendMsgAt(Integer.valueOf(times[0]), Integer.valueOf(times[1]), Integer.valueOf(times[2]), Integer.valueOf(times[3]));
			if (!isContinue) {
				return;
			}
		}

		Date syncStartTime = new Date();
		boolean isHasError = false;
		int msgCount = 0;
		int successSendCount = 0;

		try {
			rtw.writeLock().lock();
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("statusId", 0);
			filter.put("pageFrom", 0);
			filter.put("pageTo", 2000);

			List<MPMsg> list = mpMsgService.getByPage(filter);
			msgCount = list.size();
			if (list != null && list.size() > 0) {

				Iterator<MPMsg> it = list.iterator();
				while (it.hasNext()) {
					try {
						MPMsg mpMsg = it.next();
						JSONObject jsonobj = MPMsgSender.sendMsg(mpMsg);
						mpMsg.setResendTimes(mpMsg.getResendTimes()+1);
						mpMsg.setSendTime(new Date());
						mpMsg.setUpdateTime(new Date());
						if (jsonobj != null) {
							int sendResult = jsonobj.getInt("errcode");
							mpMsg.setSendResult(String.valueOf(sendResult));
							
							if (0 == sendResult) {
								mpMsg.setStatusId(1);
								mpMsg.setSendResult(String.valueOf(sendResult));
								mpMsg.setRemark(jsonobj.getString("errmsg"));
								successSendCount++;
							}
							// 发送失败,只有在错误列表中的编码的消息才会重发
							else if (MSG_ERROR_CODES.contains(String.valueOf(sendResult))) {
								mpMsg.setSendResult(String.valueOf(sendResult));
								mpMsg.setRemark(jsonobj.getString("errmsg"));
							} else {
								//其它错误一律不重发消息
								mpMsg.setStatusId(-2);
								mpMsg.setSendResult(String.valueOf(sendResult));
								mpMsg.setRemark(jsonobj.getString("errmsg"));
							}
						} else {
							mpMsg.setSendResult("-1");
							mpMsg.setRemark("发送失败，未接收到响应信息。");
						}
						mpMsgService.update(mpMsg);
					} catch (Exception e) {
						logger.error(e);
						isHasError = true;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			isHasError = true;
		} finally {
			rtw.writeLock().unlock();
		}

		// 5、记录日志
		try {
			JobLog logData = new JobLog();
			logData.setJobName(jobEntity.getName());
			logData.setIncTypeId(jobEntity.getIncTypeId());
			logData.setTotalNumber(msgCount);
			logData.setSuccessNumber(successSendCount);
			logData.setResult(isHasError ? 0 : 1);
			logData.setStartTime(syncStartTime);
			logData.setEndTime(new Date());
			logData.setCreateTime(new Date());
			logData.setUpdateTime(new Date());
			logData.setRemark(jobEntity.getName());
			joblogService.save(logData);
		} catch (Exception ex) {
			logger.error("保存微信服务号消息发送日志出错。", ex);
			ex.printStackTrace();
		}
	}
}
