package com.xincheng.webservice.restful.msg.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xincheng.encrypt.AesUtils;
import com.xincheng.encrypt.MD5Utils;
import com.xincheng.msg.enums.MsgSendType;
import com.xincheng.msg.enums.MsgStatus;
import com.xincheng.msg.service.EntMsgService;
import com.xincheng.msg.service.MPMsgService;
import com.xincheng.msg.service.TemplateService;
import com.xincheng.webservice.enums.ServiceCode;
import com.xincheng.webservice.model.BaseModel;
import com.xincheng.webservice.restful.msg.model.EnterpriseMsg;
import com.xincheng.webservice.restful.msg.model.MPMsg;
import com.xincheng.webservice.restful.msg.service.WXMsgService;

@Component
@Path("/msg/data")
public class WXMsgServiceImpl implements WXMsgService {
	private static Logger logger = LoggerFactory.getLogger(WXMsgServiceImpl.class);

	private final String Secret = ResourceBundle.getBundle("encrypt").getString("DATA_SYNC_SECRET");
	private static String AesKey = ResourceBundle.getBundle("encrypt").getString("DATA_SYNC_AESKEY");

	@Autowired
	private MPMsgService serviceMsgService;

	@Autowired
	private EntMsgService entMsgService;

	@Autowired
	private TemplateService templateService;

	@GET
	@Path("test")
	public String test() {
		return "test";
	}

	/**
	 * 考勤需求数据上传
	 * 
	 * @param sign
	 *            检验参数
	 * @param code
	 *            请求参数，needs(规则编号列表)#?batchNo(操作批次号)#?isFirstUpload(是否首次上传)#?
	 *            isLastUpload(是否最后一次上传)
	 * 
	 * */
	@POST
	@Path("saveEntMsg")
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public BaseModel submitEntMsg(@FormParam("sign") String sign, @FormParam("code") String code, @FormParam("msgs") String msgs, String sendImmediately, String relSys) {
		// saveAsFileWriter(needs);
		try {
			if (!MD5Utils.encryptMD5(Secret.trim() + code.trim() + MD5Utils.encryptMD5(msgs.trim())).equals(sign.trim())) {
				return new BaseModel(ServiceCode.ParameterValidationFailed.getCode(), "非法请求");
			}
		} catch (Exception e) {
			return new BaseModel(ServiceCode.ParameterValidationFailed.getCode(), "非法请求");
		}

		Map<String, String> args = handleParameters(code);

		if (args == null) {
			return new BaseModel(ServiceCode.ParameterValidationFailed.getCode(), "参数解析失败");
		}

		if (!StringUtils.isNotEmpty(msgs)) {
			return new BaseModel(ServiceCode.ParameterNotFound.getCode(), "[needs]参数未提供");
		}

		if (!args.containsKey("ruleId")) {
			return new BaseModel(ServiceCode.ParameterNotFound.getCode(), "[batchNo]参数未提供");
		}

		if (!args.containsKey("amDate")) {
			return new BaseModel(ServiceCode.ParameterNotFound.getCode(), "[amDate]参数未提供");
		}

		if (!args.containsKey("incrementId")) {
			return new BaseModel(ServiceCode.ParameterNotFound.getCode(), "[incrementId]参数未提供");
		}

		long ruleId = Long.parseLong(args.get("ruleId").toString().trim());
		long incrementId = Long.parseLong(args.get("incrementId").toString().trim());
		Date amDate = null;
		try {
			amDate = new SimpleDateFormat("yyyy-MM-dd").parse(args.get("amDate").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("ruleId", ruleId);
		filter.put("incrementId", incrementId);
		filter.put("amDate", amDate);

		try {
			// 序列化数据
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(EnterpriseMsg.class);

			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" }));

			JSONArray json = JSONArray.fromObject(msgs, jsonConfig);
			@SuppressWarnings("unchecked")
			List<EnterpriseMsg> tempMsgs = (List<EnterpriseMsg>) JSONArray.toCollection(json, jsonConfig);

			// 批量保存到数据库中
			if (logger.isDebugEnabled()) {
				logger.debug("batch start:" + new Date());
			}

			if (tempMsgs != null && tempMsgs.size() > 0) {
				List<com.xincheng.msg.model.EntMsg> entMsgs = new ArrayList<com.xincheng.msg.model.EntMsg>();

				for (EnterpriseMsg tempItem : tempMsgs) {
					com.xincheng.msg.model.EntMsg entMsg = new com.xincheng.msg.model.EntMsg();
					entMsg.setUserId(tempItem.getUserId());
					entMsg.setUserName(tempItem.getUserName());
					entMsg.setContent(tempItem.getContent());
					entMsg.setTypeId(tempItem.getTypeId());
					entMsg.setFromServer(ServletActionContext.getRequest().getLocalAddr());
					entMsg.setStatusId(MsgStatus.Init.getCode());
					entMsg.setSendType(MsgSendType.Queue.getCode());
					entMsg.setRelSys(relSys);
					entMsg.setCreateBy(tempItem.getCreateBy());

					entMsgs.add(entMsg);
				}

				entMsgService.insertBatch(entMsgs);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("batch end:" + new Date());
			}
		} catch (Exception ex) {
			logger.error("接收考勤需求数据并保存到临时表时出错", ex);
			return new BaseModel(ServiceCode.UnknowedError.getCode(), "接收考勤需求数据并保存到临时表时出错");
		}

		return new BaseModel();
	}

	@POST
	@Path("saveServiceMsg")
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public BaseModel submitMPMsg(String sign, String code, String msgs, String sendImmediately, String relSys) {
		try {
			if (!MD5Utils.encryptMD5(Secret.trim() + code.trim() + MD5Utils.encryptMD5(msgs.trim())).equals(sign.trim())) {
				return new BaseModel(ServiceCode.ParameterValidationFailed.getCode(), "非法请求");
			}
		} catch (Exception e) {
			return new BaseModel(ServiceCode.ParameterValidationFailed.getCode(), "非法请求");
		}

		Map<String, String> args = handleParameters(code);

		if (args == null) {
			return new BaseModel(ServiceCode.ParameterValidationFailed.getCode(), "参数解析失败");
		}

		if (!StringUtils.isNotEmpty(msgs)) {
			return new BaseModel(ServiceCode.ParameterNotFound.getCode(), "[msgs]参数未提供");
		}

		if (!StringUtils.isNotEmpty(msgs)) {
			return new BaseModel(ServiceCode.ParameterNotFound.getCode(), "[msgs]参数未提供");
		}

		try {
			// 序列化数据
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(EnterpriseMsg.class);

			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" }));

			JSONArray json = JSONArray.fromObject(msgs, jsonConfig);
			@SuppressWarnings("unchecked")
			List<MPMsg> tempMsgs = (List<MPMsg>) JSONArray.toCollection(json, jsonConfig);

			// 批量保存到数据库中
			if (logger.isDebugEnabled()) {
				logger.debug("batch start:" + new Date());
			}

			if (tempMsgs != null && tempMsgs.size() > 0) {
				List<com.xincheng.msg.model.MPMsg> serviceMsgs = new ArrayList<com.xincheng.msg.model.MPMsg>();

				for (MPMsg tempItem : tempMsgs) {
					com.xincheng.msg.model.MPMsg serviceMsg = new com.xincheng.msg.model.MPMsg();
					serviceMsg.setOpenId(tempItem.getOpenId());
					serviceMsg.setCifId(tempItem.getCifId());
					serviceMsg.setCustomerName(tempItem.getCustomerName());
					serviceMsg.setContent(tempItem.getContent());
					serviceMsg.setTemplateId(tempItem.getTemplateId());
					serviceMsg.setTypeId(tempItem.getTypeId());
					serviceMsg.setFromServer(ServletActionContext.getRequest().getLocalAddr());
					serviceMsg.setStatusId(MsgStatus.Init.getCode());
					serviceMsg.setSendType(MsgSendType.Queue.getCode());
					serviceMsg.setRelSys(relSys);
					serviceMsg.setCreateBy(tempItem.getCreateBy());

					serviceMsgs.add(serviceMsg);
				}

				serviceMsgService.insertBatch(serviceMsgs);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("batch end:" + new Date());
			}
		} catch (Exception ex) {
			logger.error("接收服务器消息并保存到数据库时失败", ex);
			return new BaseModel(ServiceCode.UnknowedError.getCode(), "接收服务器消息并保存到数据库时失败");
		}

		return new BaseModel();
	}

	private Map<String, String> handleParameters(String code) {

		try {
			String aesArgs = AesUtils.aesDecrypt(code, AesKey);

			if (!StringUtils.isNotEmpty(aesArgs))
				return null;

			String[] codeArgs = StringUtils.split(aesArgs.trim(), "_#?");

			Map<String, String> args = new HashMap<String, String>();
			for (int i = 0; i < codeArgs.length; i++) {
				String paraItem = codeArgs[i];
				if (!StringUtils.isNotEmpty(paraItem))
					continue;

				int equalIndex = paraItem.indexOf('=');
				if (equalIndex == -1)
					throw new Exception("[" + paraItem + "]参数格式不合法，未找到参数值。");

				String paraName = paraItem.substring(0, equalIndex).trim();
				String paraValue = paraItem.length() > equalIndex + 1 ? paraItem.substring(equalIndex + 1).trim() : "";
				args.put(paraName, paraValue);
			}

			return args;
		} catch (Exception e) {
			logger.error("参数解析出错", e);
		}

		return null;
	}

	private String file = "d:\\mdoor.txt";

	// 保存字符串到文件中
	private void saveAsFileWriter(String content) {

		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(file);
			fwriter.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
