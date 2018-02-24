package com.xincheng.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.sql.DataSource;
import jdbchelper.SimpleDataSource;

public class DataSourceUtil {
	static Hashtable<String, DataSource> dataSources = new Hashtable<String, DataSource>();

	public static DataSource getDataSource(String dbName) throws Exception {

		DataSource dataSource = null;
		if (dataSources == null || dataSources.size() == 0 || !dataSources.containsKey(dbName) || dataSources.get(dbName) == null) {
			dataSource = createDataSource(dbName);
			dataSources.put(dbName, dataSource);

			return dataSource;
		}

		return dataSources.get(dbName);
	}

	private static DataSource createDataSource(String dbName) throws IOException {
		Properties properties = new Properties();
		InputStream inStream = DataSourceUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
		properties.load(inStream);

		String user = properties.getProperty(dbName + ".user");
		String password = properties.getProperty(dbName + ".password");
		String jdbcUrl = properties.getProperty(dbName + ".jdbcUrl");
		String driverClass = properties.getProperty(dbName + ".driverClass");

		return new SimpleDataSource(driverClass, jdbcUrl, user, password);
	}
}
