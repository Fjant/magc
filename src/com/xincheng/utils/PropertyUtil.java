package com.xincheng.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	private Properties properties = null;

	public PropertyUtil(String path) {
		InputStream inStream = PropertyUtil.class.getResourceAsStream(path);
		properties = new Properties();
		try {
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String get(String key) {
		return properties.getProperty(key);
	}
}
