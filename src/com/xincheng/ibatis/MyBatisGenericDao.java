package com.xincheng.ibatis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.xincheng.msg.model.EntMsg;
import com.xincheng.page.Page;

public class MyBatisGenericDao extends SqlMapClientDaoSupport {
	  public static final String POSTFIX_INSERT = ".insert";
	  public static final String POSTFIX_UPDATE = ".update";
	  public static final String POSTFIX_DELETE = ".delete";
	  public static final String POSTFIX_DELETE_PRIAMARYKEY = ".deleteByPrimaryKey";
	  public static final String POSTFIX_SELECT = ".select";
	  public static final String POSTFIX_SELECT_PRIAMARYKEY = ".getById";
	  public static final String POSTFIX_COUNT = ".count";
	  public static final String POSTFIX_SELECTMAP = ".selectByMap";
	  public static final String POSTFIX_SELECTSQL = ".selectBySql";
	  
	  @SuppressWarnings("rawtypes")
	private String getClassWithoutPackageName(Class paramClass){
		  String str = paramClass.getName();
		    int i = str.lastIndexOf('.');
		    return i >= 0 ? str.substring(i + 1) : str;
	  }
	  
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> paramClass, Serializable paramSerializable) throws Exception {
	    Object localObject = getSqlSessionTemplate().selectOne(getClassWithoutPackageName(paramClass) + ".getById", paramSerializable);
	    return (T) localObject;
	  }

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> paramClass) throws Exception{
	    return getSqlSessionTemplate().selectList(getClassWithoutPackageName(paramClass) + ".select", null);
	  }

	  public void insert(Object paramObject) throws Exception {
	    getSqlSessionTemplate().insert(getClassWithoutPackageName(paramObject.getClass()) + ".insert", paramObject);
	  }
	  
	  public void update(Object paramObject) throws Exception {
	    getSqlSessionTemplate().update(getClassWithoutPackageName(paramObject.getClass()) + ".update", paramObject);
	  }

	  public void remove(Object paramObject) throws Exception {
	    getSqlSessionTemplate().delete(getClassWithoutPackageName(paramObject.getClass()) + ".delete", paramObject);
	  }

	  public <T> void removeById(Class<T> paramClass, Serializable paramSerializable) throws Exception{
	    getSqlSessionTemplate().delete(getClassWithoutPackageName(paramClass) + ".deleteByPrimaryKey", paramSerializable);
	  }

	  @SuppressWarnings("unchecked")
	public <T> List<T> find(Class<T> paramClass, Object paramObject) throws Exception {
	    return getSqlSessionTemplate().selectList(getClassWithoutPackageName(paramClass) + ".select", paramObject);
	  }

	  @SuppressWarnings("unchecked")
	public <T> List<T> find(Class<T> paramClass, Map<String, Object> paramMap) throws Exception{
	    if (paramMap == null)
	      return getSqlSessionTemplate().selectList(getClassWithoutPackageName(paramClass) + ".select", null);
	    paramMap.put("findBy", "True");
	    return getSqlSessionTemplate().selectList(getClassWithoutPackageName(paramClass) + ".selectByMap", paramMap);
	  }

	  @SuppressWarnings("unchecked")
	public <T> List<T> find(Class<T> paramClass, String paramString) throws Exception {
	    Assert.hasText(paramString);
	    if (StringUtils.isEmpty(paramString))
	      return getSqlSessionTemplate().selectList(getClassWithoutPackageName(paramClass) + ".select", null);
	    return getSqlSessionTemplate().selectList(getClassWithoutPackageName(paramClass) + ".selectBySql", paramString);
	  }

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> findBy(Class<T> paramClass, String paramString, Object paramObject) throws Exception{
	    Assert.hasText(paramString);
	    HashMap localHashMap = new HashMap();
	    localHashMap.put(paramString, paramObject);
	    return find(paramClass, localHashMap);
	  }

	  @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T findUniqueBy(Class<T> paramClass, String paramString, Object paramObject){
	    Assert.hasText(paramString);
	    Map localHashMap = new HashMap();
	    try {
	      localHashMap.put(paramString, paramObject);
	      return (T) getSqlSessionTemplate().selectOne(getClassWithoutPackageName(paramClass) + ".selectByMap", localHashMap);
	    }catch (Exception localException){
	      this.logger.error("Error when propertie on entity," + localException.getMessage(), localException.getCause());
	    }
	    return null;
	  }
	  @SuppressWarnings("rawtypes")
	public Page<?> pagedQuery(Class paramClass, Object paramObject, Page<?> paramPage) throws Exception{
	    return pagedQuery(getClassWithoutPackageName(paramClass) + ".select", getClassWithoutPackageName(paramClass) + ".count", paramObject, paramPage);
	  }

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<?> pagedQuery(String paramString1, String paramString2, Object paramObject, Page<?> paramPage) throws Exception{
	    Long localLong = (Long)getSqlSessionTemplate().selectOne(paramString2, paramObject);
	    if (localLong != null)
	      paramPage.setTotalCount(localLong.longValue());
	    if (paramObject != null) {
	       Map localObject = (Map)paramObject;
	      localObject.put("orderBy", paramPage.getOrderBy());
	      localObject.put("order", paramPage.getOrder());
	    }
	    Object localObject = getSqlSessionTemplate().selectList(paramString1, paramObject, paramPage.getFirst(), paramPage.getPageSize());
	    paramPage.setResult((List)localObject);
	    return paramPage;
	  }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> findByLike(Class<T> paramClass, String paramString1, String paramString2) throws Exception{
	    Assert.hasText(paramString1);
	    Map localHashMap = new HashMap();
	    localHashMap.put(paramString1, paramString2);
	    localHashMap.put("findLikeBy", "True");
	    return getSqlSessionTemplate().selectList(getClassWithoutPackageName(paramClass) + ".selectByMap", localHashMap);
	  }

}
