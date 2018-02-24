package com.xincheng.page;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Page<T> {
  public static final String ASC = "asc";
  public static final String DESC = "desc";
  public static final int MAX_PAGESIZE = 200;
  public static final int MIN_PAGESIZE = 1;
  public static final int DEFAULT_PAGESIZE = 10;
  protected int maxPageSize = 200;
  protected int pageNo = 1;
  protected int pageSize = 10;
  protected String orderBy = null;
  protected String order = null;
  protected boolean autoCount = false;
  protected String search;
  protected List<T> result = Collections.emptyList();
  protected long totalCount = -1L;
  public Page() {
  }

  public Page(int paramInt){
    this.pageSize = paramInt;
  }

  public Page(int paramInt, boolean paramBoolean){
    this.pageSize = paramInt;
    this.autoCount = paramBoolean;
  }

  public int getPageNo(){
    return this.pageNo;
  }

  public void setPageNo(int paramInt){
    this.pageNo = paramInt;
    if (paramInt < 1)
      this.pageNo = 1;
  }

  public Page<T> pageNo(int paramInt){
    setPageNo(paramInt);
    return this;
  }

  public int getPageSize(){
    return this.pageSize;
  }

  public void setPageSize(int paramInt){
    this.pageSize = paramInt;
    if (paramInt < 1)
      this.pageSize = 1;
    else if (paramInt > this.maxPageSize)
      this.pageSize = this.maxPageSize;
  }

  public int getMaxPageSize() {
    return this.maxPageSize;
  }

  public void setMaxPageSize(int paramInt){
    this.maxPageSize = paramInt;
  }

  public Page<T> pageSize(int paramInt){
    setPageSize(paramInt);
    return this;
  }

  public int getFirst(){
    return (this.pageNo - 1) * this.pageSize + 1;
  }

  public String getOrderBy() {
    return this.orderBy;
  }

  public void setOrderBy(String paramString){
    this.orderBy = paramString;
  }

  public Page<T> orderBy(String paramString){
    setOrderBy(paramString);
    return this;
  }

  public String getOrder(){
    return this.order;
  }

  public void setOrder(String paramString){
    String str1 = StringUtils.lowerCase(paramString);
    String[] arrayOfString1 = StringUtils.split(str1, ',');
    for (String str2 : arrayOfString1)
      if ((!StringUtils.equals("desc", str2)) && (!StringUtils.equals("asc", str2)))
        throw new IllegalArgumentException("排序方向" + str2 + "不是合法值");
    this.order = str1;
  }

  public Page<T> order(String paramString){
    setOrder(paramString);
    return this;
  }

  public boolean isOrderBySetted(){
    return (StringUtils.isNotBlank(this.orderBy)) && (StringUtils.isNotBlank(this.order));
  }

  public boolean isAutoCount(){
    return this.autoCount;
  }

  public void setAutoCount(boolean paramBoolean){
    this.autoCount = paramBoolean;
  }

  public Page<T> autoCount(boolean paramBoolean){
    setAutoCount(paramBoolean);
    return this;
  }

  public List<T> getResult(){
    return this.result;
  }

  public void setResult(List<T> paramList){
    this.result = paramList;
  }

  public long getTotalCount(){
    return this.totalCount;
  }

  public void setTotalCount(long paramLong){
    this.totalCount = paramLong;
  }

  public long getTotalPages() {
    if (this.totalCount < 0L)
      return -1L;
    long l = this.totalCount / this.pageSize;
    if (this.totalCount % this.pageSize > 0L)
      l += 1L;
    return l;
  }

  public boolean isHasNext(){
    return this.pageNo + 1 <= getTotalPages();
  }

  public int getNextPage(){
    if (isHasNext())
      return this.pageNo + 1;
    return this.pageNo;
  }

  public boolean isHasPre(){
    return this.pageNo - 1 >= 1;
  }

  public int getPrePage(){
    if (isHasPre())
      return this.pageNo - 1;
    return this.pageNo;
  }

  public boolean isSearch(){
    Boolean localBoolean = Boolean.valueOf(this.search);
    return localBoolean.booleanValue();
  }

  public void setSearch(String paramString){
    this.search = paramString;
  }
}
