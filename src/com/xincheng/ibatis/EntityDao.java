package com.xincheng.ibatis;

import java.io.Serializable;
import java.util.List;

import com.xincheng.page.Page;

public interface EntityDao<T, PK extends Serializable> {
	 public  T get(Serializable paramSerializable) throws Exception;

	  public  List<T> getAll() throws Exception;

	  public  void save(Object paramObject) throws Exception;

	  public  void removeById(Serializable paramSerializable) throws Exception;
	  
	  public  Page<?> pagedQuery(Object paramObject, Page<?> paramPage) throws Exception;

	  public  List<T> find(Object paramObject) throws Exception;
}

