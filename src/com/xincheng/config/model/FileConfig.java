package com.xincheng.config.model;

public class FileConfig {
	/**
	 * 临时文件路径
	 * */
	private String fileTempPath;
	/**
	 * 上传文件保存路径根目录
	 * */
	private String fileUploadPath;
	/**
	 * 下载文件保存路径根目录
	 * */
	private String fileDownloadPath;
	/**
	 * 文件访问虚拟路径
	 * */
	private String fileVitualBaseUrl;
	/**
	 * 文件访问虚拟路径
	 * */
	private String fileVitualBaseDownloadUrl;
	
	/**
	 * 文件访问虚拟路径
	 * */
	private String fileVitualBaseUploadUrl;
	
	public String getFileTempPath() {
		return fileTempPath;
	}

	public void setFileTempPath(String fileTempPath) {
		this.fileTempPath = fileTempPath;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = fileDownloadPath;
	}

	public String getFileVitualBaseUrl() {
		return fileVitualBaseUrl;
	}

	public void setFileVitualBaseUrl(String fileVitualBaseUrl) {
		this.fileVitualBaseUrl = fileVitualBaseUrl;
	}

	public String getFileVitualBaseDownloadUrl() {
		return fileVitualBaseDownloadUrl;
	}

	public void setFileVitualBaseDownloadUrl(String fileVitualBaseDownloadUrl) {
		this.fileVitualBaseDownloadUrl = fileVitualBaseDownloadUrl;
	}

	public String getFileVitualBaseUploadUrl() {
		return fileVitualBaseUploadUrl;
	}

	public void setFileVitualBaseUploadUrl(String fileVitualBaseUploadUrl) {
		this.fileVitualBaseUploadUrl = fileVitualBaseUploadUrl;
	}


}
