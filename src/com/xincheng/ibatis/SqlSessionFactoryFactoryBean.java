package com.xincheng.ibatis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;

public class SqlSessionFactoryFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean{
	  Log logger = LogFactory.getLog(SqlSessionFactoryFactoryBean.class);
	  private Resource configLocation;
	  private Resource[] mapperLocations;
	  private DataSource dataSource;
	  private boolean useTransactionAwareDataSource = true;
	  private String charset = "UTF-8";
	  SqlSessionFactory sqlSessionFactory;
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		 Assert.notNull(this.configLocation, "configLocation must be not null");
		 this.sqlSessionFactory = createSqlSessionFactory();
	}
	
	 private SqlSessionFactory createSqlSessionFactory() throws IOException{
		 InputStreamReader inputStreamReader = new InputStreamReader(getConfigLocation().getInputStream());
		 try {
			 SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStreamReader);
		     Configuration localConfiguration = sqlSessionFactory.getConfiguration();
		     //DataSource datasource = this.dataSource;
		     TransactionAwareDataSourceProxy datasource = null;
		     if (this.dataSource != null){
		    	 if ((this.useTransactionAwareDataSource) && (!(this.dataSource instanceof TransactionAwareDataSourceProxy))){
		    		  datasource = new TransactionAwareDataSourceProxy(this.dataSource);
		    	 }
		    	 localConfiguration.setEnvironment(new Environment("development", new ManagedTransactionFactory(), (DataSource)datasource));
		    	 sqlSessionFactory = new SqlSessionFactoryBuilder().build(localConfiguration);	 
		     }
		     if (this.mapperLocations != null){
		    	 Map<String, XNode> sqlFragments = new HashMap<String, XNode>(); 
		    	 for (Resource localResource : this.mapperLocations){
		    		 String path = null;
		    		 if ((localResource instanceof FileSystemResource)){
		    			 path = localResource.getFile().getAbsolutePath();
		    		 }
		    		 if ((localResource instanceof UrlResource)){
		    			 path = localResource.getURL().getPath();
		    		 }
		    		 this.logger.info("Loading iBatis3 mapper xml from file[" + path + "]");
		    		 InputStreamReader localInputStreamReader2 = new InputStreamReader(localResource.getInputStream(), this.charset);
		    		 XMLMapperBuilder localXMLMapperBuilder = null;
		    		 try{
		               localXMLMapperBuilder = new XMLMapperBuilder(localInputStreamReader2, localConfiguration, path, sqlFragments);
		               localXMLMapperBuilder.parse();
		             }catch(Exception e){
		            	 logger.error(e);
		             }finally{
		            	 
		             }
		    	 }
		     }
		     return  sqlSessionFactory;
		 }catch(Exception e){
			 logger.error(e);
		 } finally {
			 inputStreamReader.close();
		  }
		 return null;
	 }

	@Override
	public SqlSessionFactory getObject() throws Exception {
		// TODO Auto-generated method stub
		return this.sqlSessionFactory;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return SqlSessionFactory.class;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return true;
	}

	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public Resource[] getMapperLocations() {
		return mapperLocations;
	}

	public void setMapperLocations(Resource[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}


}
