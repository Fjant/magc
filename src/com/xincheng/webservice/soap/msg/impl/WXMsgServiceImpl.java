package com.xincheng.webservice.soap.msg.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xincheng.encrypt.MD5Utils;
import com.xincheng.msg.enums.MsgSendType;
import com.xincheng.msg.enums.MsgStatus;
import com.xincheng.msg.service.EntMsgService;
import com.xincheng.msg.service.MPMsgService;
import com.xincheng.msg.service.TemplateService;
import com.xincheng.webservice.enums.ServiceCode;
import com.xincheng.webservice.model.BaseModel;
import com.xincheng.webservice.soap.msg.WXMsgService;
import com.xincheng.wx.msgcsender.EntMsgSender;
import com.xincheng.wx.msgcsender.MPMsgSender;

@Component
@Path("/msg/data")
public class WXMsgServiceImpl implements WXMsgService {
	private static Logger logger = LoggerFactory.getLogger(WXMsgServiceImpl.class);

	private final String Secret = ResourceBundle.getBundle("encrypt").getString("DATA_SYNC_SECRET");

	@Autowired
	private MPMsgService mpMsgService;

	@Autowired
	private EntMsgService entMsgService;

	@Autowired
	private TemplateService templateService;

	@Resource
	private WebServiceContext webServiceContext;

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
	@Override
	public BaseModel submitEntMsg(String userId, String userName, String content, long templateId, int typeId, boolean sendImmediately, String relSys, String sign) {

		logger.info("接收到请求信息：" + userId + "#?#" + userName + "#?#" + content + "#?#" + templateId + "#?#" + typeId + "#?#" + sendImmediately + "#?#" + relSys);

		BaseModel model = new BaseModel();

		if (StringUtils.isEmpty(userId)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("userId参数不能为空");
			return model;
		}

		if (StringUtils.isEmpty(userName)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("userName参数不能为空");
			return model;
		}

		if (StringUtils.isEmpty(content)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("content参数不能为空");
			return model;
		}

		if (typeId == 0) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("typeId参数不能为0或空");
			return model;
		}

		if (StringUtils.isEmpty(relSys)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("relSys参数不能为空");
			return model;
		}

		// if (StringUtils.isEmpty(sign)) {
		// model.setCode(ServiceCode.ParameterNull.getCode());
		// model.setMessage("sign参数不能为空");
		// return model;
		// }

		// // 加密参数
		// String checkCode = null;
		// try {
		// String sourceCode = userId + "#?#" + userName + "#?#" + content +
		// "#?#" + templateId + "#?#" + typeId + "#?#" + sendImmediately + "#?#"
		// + relSys;
		// logger.error("sourceCode=" + sourceCode);
		//
		// checkCode = MD5Utils.encryptMD5(sourceCode.trim());
		// logger.error("checkCode=" + checkCode);
		//
		// String checkSign = MD5Utils.encryptMD5(Secret.trim() + checkCode);
		//
		// logger.error("checkSign=" + checkSign + ", parameterSign=" + sign);
		//
		// if (!checkSign.equalsIgnoreCase(sign)) {
		// model.setCode(ServiceCode.InvokeIllegal.getCode());
		// model.setMessage(ServiceCode.InvokeIllegal.getComment());
		// return model;
		// }
		// } catch (Exception e) {
		// logger.equals(e);
		// model.setCode(ServiceCode.UnknowedError.getCode());
		// model.setMessage(ServiceCode.UnknowedError.getComment());
		// return model;
		// }

		try {
			// 序列化数据
			// 批量保存到数据库中
			//logger.info("batch start:" + new Date());

			HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
			com.xincheng.msg.model.EntMsg entMsg = new com.xincheng.msg.model.EntMsg();
			entMsg.setUserId(userId);
			entMsg.setUserName(userName);
			entMsg.setContent(content);
			entMsg.setTemplateId(templateId);
			entMsg.setTypeId(typeId);
			entMsg.setFromServer(request.getLocalAddr());
			entMsg.setStatusId(MsgStatus.Init.getCode());
			entMsg.setSendType(sendImmediately ? MsgSendType.Immediately.getCode() : MsgSendType.Queue.getCode());
			entMsg.setRelSys(relSys);
			entMsg.setCreateBy(relSys);
			entMsg.setSendTime(new Date());

			if (sendImmediately) {
				JSONObject resultObj = EntMsgSender.sendMsg(entMsg);
				entMsg.setResendTimes(1);
				if (resultObj != null && resultObj.containsKey("errcode")) {
					entMsg.setStatusId(resultObj.getInt("errcode") == 0 ? 0 : 1);
					entMsg.setSendResult(resultObj.getString("errcode"));
					entMsg.setSendTime(new Date());
					entMsg.setRemark(resultObj.getString("errmsg"));
				}
			}

			entMsgService.insert(entMsg);

			//logger.info("batch end:" + new Date());
		} catch (Exception ex) {
			logger.error("企业号消息接收处理失败", ex);
			model.setCode(ServiceCode.UnknowedError.getCode());
			model.setMessage(ServiceCode.UnknowedError.getComment());
			return model;
		}

		return model;
	}

	@Override
	public BaseModel submitMPMsg(String openId, String cifId, String customerName, String content, String templateId, int typeId, boolean sendImmediately, String relSys, String url, String sign) {

		logger.info("接收到请求信息：" + openId + "#?#" + cifId + "#?#" + customerName + "#?#" + content + "#?#" + templateId + "#?#" + typeId + "#?#" + sendImmediately + "#?#" + relSys + "#?#" + (StringUtils.isEmpty(url) ? "" : url));

		BaseModel model = new BaseModel();

		if (StringUtils.isEmpty(openId)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("openId参数不能为空");
			return model;
		}

		if (StringUtils.isEmpty(cifId)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("cifId参数不能为空");
			return model;
		}

		if (StringUtils.isEmpty(customerName)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("customerName参数不能为空");
			return model;
		}
		
		if (StringUtils.isEmpty(content)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("content参数不能为空");
			return model;
		}

		try {
			JSONObject.fromObject(content);
		} catch (Exception ex) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("content参数值JSON格式不正确");
			return model;
		}

		if (StringUtils.isEmpty(templateId)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("templateId参数不能为空");
			return model;
		}

		if (typeId == 0) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("typeId参数不能为0或空");
			return model;
		}

		if (StringUtils.isEmpty(relSys)) {
			model.setCode(ServiceCode.ParameterNull.getCode());
			model.setMessage("relSys参数不能为空");
			return model;
		}

		// if (StringUtils.isEmpty(sign)) {
		// model.setCode(ServiceCode.ParameterNull.getCode());
		// model.setMessage("sign参数不能为空");
		// return model;
		// }
		//
		// // 加密参数
		// String checkCode = null;
		// try {
		// String sourceCode = openId + "#?#" + cifId + "#?#" + customerName +
		// "#?#" + content + "#?#" + templateId + "#?#" + typeId + "#?#" +
		// sendImmediately + "#?#" + relSys + "#?#" + (StringUtils.isEmpty(url)
		// ? "" : url);
		// logger.error("sourceCode=" + sourceCode);
		//
		// checkCode = MD5Utils.encryptMD5(sourceCode.trim());
		// logger.error("checkCode=" + checkCode);
		//
		// String checkSign = MD5Utils.encryptMD5(Secret.trim() +
		// checkCode.trim());
		//
		// logger.error("checkSign=" + checkSign + ", parameterSign=" + sign);
		//
		// if (!checkSign.equalsIgnoreCase(sign)) {
		// model.setCode(ServiceCode.InvokeIllegal.getCode());
		// model.setMessage(ServiceCode.InvokeIllegal.getComment());
		// return model;
		// }
		// } catch (Exception e) {
		// logger.equals(e);
		// model.setCode(ServiceCode.UnknowedError.getCode());
		// model.setMessage(ServiceCode.UnknowedError.getComment());
		// return model;
		// }

		try {
			// 序列化数据
			// 批量保存到数据库中
			//logger.info("batch start:" + new Date());
			HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
			com.xincheng.msg.model.MPMsg mpMsg = new com.xincheng.msg.model.MPMsg();
			mpMsg.setOpenId(openId);
			mpMsg.setCifId(cifId);
			mpMsg.setCustomerName(customerName);
			mpMsg.setContent(content);
			mpMsg.setTemplateId(templateId);
			mpMsg.setTypeId(typeId);
			mpMsg.setFromServer(request.getLocalAddr());
			mpMsg.setStatusId(MsgStatus.Init.getCode());
			mpMsg.setSendType(sendImmediately ? MsgSendType.Immediately.getCode() : MsgSendType.Queue.getCode());
			mpMsg.setUrl(url);
			mpMsg.setRelSys(relSys);
			mpMsg.setCreateBy(relSys);
			
			
			if (sendImmediately) {
				JSONObject resultObj = MPMsgSender.sendMsg(mpMsg);
				mpMsg.setResendTimes(1);
				if (resultObj != null && resultObj.containsKey("errcode")) {
					mpMsg.setStatusId(resultObj.getInt("errcode") == 0 ? 1 : 0);
					mpMsg.setSendResult(resultObj.getString("errcode"));
					mpMsg.setSendTime(new Date());
					mpMsg.setRemark(resultObj.getString("errmsg"));
				}
			}

			mpMsgService.insert(mpMsg);

			//logger.info("batch end:" + new Date());
		} catch (Exception ex) {
			logger.error("服务号消息接收处理失败", ex);
			model.setCode(ServiceCode.UnknowedError.getCode());
			model.setMessage(ServiceCode.UnknowedError.getComment());
			return model;
		}

		return model;
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
