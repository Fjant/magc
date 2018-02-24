package com.xincheng.wx.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xincheng.wx.Env.WxEnv;
import com.xincheng.wx.model.WxAccessToken;
import com.xincheng.wx.model.WxMsgBase;

/**
 * 公众平台通用接口工具类
 * 
 */
public class WeixinUtil {

	private static String CORP_ID = WxEnv.propertyUtil.get("CORP_ID");
	private static String GROUP = WxEnv.propertyUtil.get("default_group_" + CORP_ID);
	private static String CORP_SECRET = WxEnv.propertyUtil.get("CORP_SECRET_" + CORP_ID + "_" + GROUP);

	private static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);
	private static String encodeStr = "UTF-8";
	private static int count;

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpsURLConnection httpUrlConn = null;

		try {

			URL url = new URL(requestUrl);

			if ("1".equals(WxEnv.propertyUtil.get("SSLCONTEXT_IS_Proxy"))) {
				// 代理服务器的ip地址
				String httpProxHost = WxEnv.propertyUtil.get("httpProxHost");
				// 代理服务器的端口
				String httpProxPort = WxEnv.propertyUtil.get("httpProxPort");
				SocketAddress sa = new InetSocketAddress(httpProxHost, Integer.parseInt(httpProxPort));
				// 定义代理，此处的Proxy是源自java.net
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, sa);
				httpUrlConn = (HttpsURLConnection) url.openConnection(proxy);
			} else {
				httpUrlConn = (HttpsURLConnection) url.openConnection();
			}
			// httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setDefaultUseCaches(false);
			httpUrlConn.setConnectTimeout(Integer.parseInt(WxEnv.propertyUtil.get("httpConnectTimeout")));
			httpUrlConn.setReadTimeout(30000);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			// httpUrlConn.setRequestProperty("content-type",
			// "application/x-www-form-urlencoded");
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes(encodeStr));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, encodeStr);
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.");
		} catch (SocketException se) {
			count++;
			if (count == 1) {
				count = 0;
				return httpRequest(requestUrl, requestMethod, outputStr);
			} else {
				logger.error("SocketException ", se);
				logger.error(requestUrl);
			}
		} catch (Exception e) {
			logger.error("https request error:{}", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(inputStreamReader);
			IOUtils.closeQuietly(bufferedReader);
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
			}
		}
		return jsonObject;
	}

	/**
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpUrlConn = null;

		try {

			URL url = new URL(requestUrl);

			if ("1".equals(WxEnv.propertyUtil.get("SSLCONTEXT_IS_Proxy"))) {
				// 代理服务器的ip地址
				String httpProxHost = WxEnv.propertyUtil.get("httpProxHost");
				// 代理服务器的端口
				String httpProxPort = WxEnv.propertyUtil.get("httpProxPort");
				SocketAddress sa = new InetSocketAddress(httpProxHost, Integer.parseInt(httpProxPort));
				// 定义代理，此处的Proxy是源自java.net
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, sa);
				httpUrlConn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				httpUrlConn = (HttpURLConnection) url.openConnection();
			}
			// httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setDefaultUseCaches(false);
			httpUrlConn.setConnectTimeout(Integer.parseInt(WxEnv.propertyUtil.get("httpConnectTimeout")));
			httpUrlConn.setReadTimeout(30000);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			// httpUrlConn.setRequestProperty("content-type",
			// "application/x-www-form-urlencoded");
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes(encodeStr));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, encodeStr);
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.");
		} catch (SocketException se) {
			count++;
			if (count == 1) {
				count = 0;
				return httpRequest(requestUrl, requestMethod, outputStr);
			} else {
				logger.error("SocketException ", se);
				logger.error(requestUrl);
			}
		} catch (Exception e) {
			logger.error("https request error:{}", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(inputStreamReader);
			IOUtils.closeQuietly(bufferedReader);
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
			}
		}
		return jsonObject;
	}
	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static WxAccessToken getAccessTokenFromWx() {
		WxAccessToken accessToken = null;
		try {

			Object accessTokenObj = WxEnv.wxInfoMap.get(CORP_ID + "-" + CORP_SECRET);
			if (accessTokenObj == null || System.currentTimeMillis() - (accessToken = (WxAccessToken) accessTokenObj).getBeginTimeMillis() > accessToken.getRePickMillis()) {
				String requestUrl = WxEnv.propertyUtil.get("ACCESS_TOKEN_URL").replace(WxEnv.propertyUtil.get("CORP_ID_NAME"), CORP_ID).replace(WxEnv.propertyUtil.get("CORP_SECRET_NAME"), CORP_SECRET);
				JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
				// 如果请求成功
				if (null != jsonObject) {
					try {
						accessToken = new WxAccessToken();
						accessToken.setAccessToken(jsonObject.getString(WxEnv.propertyUtil.get("REP_ACCESS_TOKEN_NAME")));
						accessToken.setExpiresIn(jsonObject.getInt(WxEnv.propertyUtil.get("REP_EXPIRES_IN_NAME")));
						accessToken.setBeginTimeMillis(System.currentTimeMillis());
						accessToken.setRePickMillis(Long.parseLong(WxEnv.propertyUtil.get("REP_ACCESS_TOKEN_rePickMillis")));
						WxEnv.wxInfoMap.put(CORP_ID + "-" + CORP_SECRET, accessToken);
					} catch (JSONException e) {
						accessToken = null;
						// 获取token失败
						logger.error("获取token失败 errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{" + jsonObject.getString("errmsg") + "}");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("WxAccessToken", e);
		}
		return accessToken;
		// try {
		// if (weixinClient == null) {
		// weixinClient = (EntService) CxfClientFactory.create(EntService.class,
		// GET_ACCESS_TOKEN_API);
		// }
		//
		// // 如果map里面的accessToken为null或者获取时间超过时间限制，从微信端获取accessToken
		// if (accessToken == null || System.currentTimeMillis() - (accessToken
		// = (WxAccessToken) accessToken).getBeginTimeMillis() >
		// accessToken.getRePickMillis()) {
		// accessToken = weixinClient.getToken("msgc");
		// }
		// } catch (Exception e) {
		// logger.error(e);
		// }
		// return accessToken;
	}

	/**
	 * 通过methodName参数，调用微信企业号对应的接口方法
	 * 
	 * @param map
	 *            request参数map
	 * @param wxMap
	 *            微信接口参数map
	 * @param accessToken
	 * @return
	 */
	public static JSONObject callWxInterface(Map<String, String[]> map, Map<String, Object> wxMap, String accessToken) {
		// String resultStr="";
		// int result = 0;
		// 该参数决定调用哪个接口方法
		String methodName = map.get("methodName")[0];
		// 接口调用的url
		String url = WxEnv.propertyUtil.get(methodName + "_URL").replace(WxEnv.propertyUtil.get("ACCESS_TOKEN_NAME"), accessToken);
		// 接口url的请求类型（POST或者GET）
		String reqType = "POST";
		String jsonWxMsg = null;
		if (("," + WxEnv.propertyUtil.get("WX_URL_HTTP_GET") + ",").indexOf(methodName + "_URL") >= 0) {
			// get请求
			reqType = "GET";
			String allParamStr = WxEnv.propertyUtil.get(methodName + "_PARAM");
			String[] allParamStrArr = allParamStr.split(",");
			// 替换get请求url里面的占位参数值
			for (String paramStr : allParamStrArr) {
				String[] paramStrArr = paramStr.split("\\|");
				url = url.replace("#xincheng_wxParam_" + paramStrArr[0] + "#", wxMap.get(paramStrArr[0]) == null ? "" : wxMap.get(paramStrArr[0]).toString());
			}
		} else {
			// 将消息对象转换成json字符串
			jsonWxMsg = JSONObject.fromObject(wxMap).toString();
			logger.info("jsonWxMsg=" + jsonWxMsg);
		}
		// 调用接口发送消息
		JSONObject jsonObject = httpRequest(url, reqType, jsonWxMsg);

		if (jsonObject != null && logger.isDebugEnabled()) {
			logger.info(methodName + ":::" + jsonObject.toString());
		}

		return jsonObject;
	}

	/**
	 * 
	 * 发送企业号消息
	 * 
	 * @param
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static JSONObject sendWxQyhMsg(WxMsgBase wxMsg, String accessToken) {
		// 拼发送消息的url
		String url = WxEnv.propertyUtil.get("SEND_QYH_MSG_URL").replace(WxEnv.propertyUtil.get("ACCESS_TOKEN_NAME"), accessToken);
		// 将消息对象转换成json字符串
		String jsonWxMsg = JSONObject.fromObject(wxMsg).toString();

		if (logger.isDebugEnabled()) {
			logger.debug("jsonWxMsg=" + jsonWxMsg);
		}

		// 调用接口发送消息
		JSONObject jsonObject = httpRequest(url, "POST", jsonWxMsg);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode") && 82001 != jsonObject.getInt("errcode")) {
				logger.error("发送消息失败 。errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{" + jsonObject.getString("errmsg") + "}");
			}
		}

		return jsonObject;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menuJsonStr
	 *            创建菜单的请求参数,json字符串
	 * @param accessToken
	 * @param agentid
	 *            企业应用id
	 * @return
	 */
	public static JSONObject createMenu(String menuJsonStr, String accessToken, String agentid) {
		// 创建菜单的请求url
		String url = WxEnv.propertyUtil.get("create_menu_URL").replace(WxEnv.propertyUtil.get("ACCESS_TOKEN_NAME"), accessToken);
		url = url.replace("#xincheng_wxParam_agentid#", agentid);
		JSONObject jsonObject = httpRequest(url, "POST", menuJsonStr);
		if (logger.isDebugEnabled()) {
			logger.debug("menuJsonStr======" + menuJsonStr);
			logger.debug("创建菜单的返回结果=" + jsonObject.toString());
		}
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				logger.error("创建菜单失败 errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{" + jsonObject.getString("errmsg") + "}");
			}
		}
		return jsonObject;
	}
}
