package com.xincheng.cache.interceptors;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.xincheng.memcached.MemcachedUtils;

@Component
@Aspect
public class TemplateInterceptor {

	private static final Logger logger = Logger.getLogger(TemplateInterceptor.class);
	private final static String NOTICES_KEY = "YXY_BROADCAST_INFO";

	public void saveNotice() {
		try {
			if (MemcachedUtils.containsKey(NOTICES_KEY)) {
				MemcachedUtils.delete(NOTICES_KEY);
				if (logger.isDebugEnabled()) {
					logger.debug("清除公告缓存[saveNotice]");
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	public void updateNotice() {
		try {
			if (MemcachedUtils.containsKey(NOTICES_KEY)) {
				MemcachedUtils.delete(NOTICES_KEY);
				if (logger.isDebugEnabled()) {
					logger.debug("清除公告缓存[updateNotice]");
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}
	
	/**
	 * 此方法只处理营销员自己删除数据时的操作，管理员删除时应另外处理。
	 * */
	public void removeById() {
		try {
			if (MemcachedUtils.containsKey(NOTICES_KEY)) {
				MemcachedUtils.delete(NOTICES_KEY);
				if (logger.isDebugEnabled()) {
					logger.debug("清除公告缓存[removeById]");
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}
}
