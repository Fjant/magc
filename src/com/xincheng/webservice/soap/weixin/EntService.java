package com.xincheng.webservice.soap.weixin;

import javax.jws.WebService;
import com.xincheng.wx.model.WxAccessToken;

@WebService
public interface EntService {
	/**
	 * @param relSys
	 *            调用的业务系统
	 * 
	 * @return AccessToken，Token 值，包含Token及失效时间
	 */
	WxAccessToken getToken(String relSys);
}
