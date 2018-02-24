package com.xincheng.webservice;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class CxfClientFactory {
	/**
     * 获得调用的proxy factory
     * @param clazz - 服务端对应的服务类（接口）
     * @param endPointAddress - 调用的服务地址
     * @return
     */
	
    public static Object create(Class<?> clazz, String endPointAddress){
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(clazz);
        factory.setAddress( endPointAddress);
        return factory.create();
    }
}
