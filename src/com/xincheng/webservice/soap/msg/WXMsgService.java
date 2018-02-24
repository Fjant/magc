package com.xincheng.webservice.soap.msg;

import javax.jws.WebService;

import com.xincheng.webservice.model.BaseModel;

/**
 * @author Administrator
 *
 */
@WebService
public interface WXMsgService {

	
	/**
	 * 企业号消息发送接口
	 * 
	 * @param userId					用户编码，接收信息的人员，存在于企业号里面的成员编码。不可空
	 * @param userName				用户姓名。不可空
	 * @param content					消息内容，消息中可通过<a href=""></a>增加义链接。不可空
	 * @param templateId				消息模板，此模板为内部模板。可空
	 * @param typeId						消息类型，自定义的数值，不同类型的消息该值设置为不一样的即可。不可空
	 * @param sendImmediately  是否立即发送，true为立即发送 ，false为插入队列后排队发送
	 * @param relSys						相关业务系统编码，如LAS,STS,GRP等。不可空
	 * @param sign							本接口访问时的验证参数。可为空
	 * @return
	 */
	BaseModel submitEntMsg(String userId, String userName, String content, long templateId, int typeId, boolean sendImmediately, String relSys, String sign);

	
	/**
	 * 公众服务号消息发送接口
	 * 
	 * @param openId					客户的OPENID。不可空
	 * @param cifId						客户的CIF_ID编号。不可空
	 * @param customerName		客户的姓名。不可空
	 * @param content					消息内容，以JSON格式存储的消息内容，如{"first":"尊敬的客户","remark":"详情请查看以下链接"}。不可空
	 * @param templateId				服务号的消息模板。不可空
	 * @param typeId						消息类型，自定义的数值，不同类型的消息该值设置为不一样的即可。不可空
	 * @param sendImmediately	是否立即发送，true为立即发送 ，false为插入队列后排队发送
	 * @param relSys						相关业务系统编码，如LAS,STS,GRP等。不可空
	 * @param url							消息链接，用户点击消息内容中的“详情”链接时跳转到的链接地址。可空
	 * @param sign							本接口访问时的验证参数。可为空
	 * @return
	 */
	BaseModel submitMPMsg(String openId, String cifId, String customerName, String content, String templateId, int typeId, boolean sendImmediately, String relSys, String url, String sign);

}
