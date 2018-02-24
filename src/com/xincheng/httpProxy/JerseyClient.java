package com.xincheng.httpProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.xincheng.httpProxy.ConnectionFactory;
import com.xincheng.wx.Env.WxEnv;

@Component
public class JerseyClient {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private Client client = null;

	public Client getClient(String proxyHost, int proxyPort) {
		if (client == null) {
			URLConnectionClientHandler cc = new URLConnectionClientHandler(new ConnectionFactory(proxyHost, proxyPort));
			client = new Client(cc);
			client.setConnectTimeout(2000000);
		}

		return client;
	}

	public Client getClient() {
		if (client == null) {
			if (WxEnv.propertyUtil.get("SSLCONTEXT_IS_Proxy").trim().equals("1")) {
				String httpProxHost = WxEnv.propertyUtil.get("httpProxHost");
				int httpProxPort = Integer.parseInt(WxEnv.propertyUtil.get("httpProxPort"));
				URLConnectionClientHandler cc = new URLConnectionClientHandler(new ConnectionFactory(httpProxHost, httpProxPort));
				client = new Client(cc);
				client.setConnectTimeout(2000000);
			} else {
				client = Client.create(new DefaultClientConfig());
			}
		}

		return client;
	}

}