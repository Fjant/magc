package com.xincheng.config;

import org.springframework.stereotype.Component;

@Component
public class Communication {

	private String senderEmailTitle;

	private String senderSmtpHost;

	private String senderEmailName;
	
	private String senderEmailPassWord;
	
	private String notifierEmail;

	public String getSenderEmailTitle() {
		return senderEmailTitle;
	}

	public void setSenderEmailTitle(String senderEmailTitle) {
		this.senderEmailTitle = senderEmailTitle;
	}

	public String getSenderSmtpHost() {
		return senderSmtpHost;
	}

	public void setSenderSmtpHost(String senderSmtpHost) {
		this.senderSmtpHost = senderSmtpHost;
	}

	public String getSenderEmailName() {
		return senderEmailName;
	}

	public void setSenderEmailName(String senderEmailName) {
		this.senderEmailName = senderEmailName;
	}

	public String getSenderEmailPassWord() {
		return senderEmailPassWord;
	}

	public void setSenderEmailPassWord(String senderEmailPassWord) {
		this.senderEmailPassWord = senderEmailPassWord;
	}

	public String getNotifierEmail() {
		return notifierEmail;
	}

	public void setNotifierEmail(String notifierEmail) {
		this.notifierEmail = notifierEmail;
	}
}
