package com.xincheng.errorObserver.model;

import com.xincheng.ibatis.BaseEntity;

/**
 * 功能模块表
 * @author Administrator 
 *  SYS_FUNCTION_MODULAR
 */

public class FunctionModular extends BaseEntity{

	private static final long serialVersionUID = -5027904376565142649L;

	private long id; 
 
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
