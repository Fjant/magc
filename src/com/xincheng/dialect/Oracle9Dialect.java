package com.xincheng.dialect;

public class Oracle9Dialect extends Dialect {
	
	 public boolean supportsLimit() {
	    return true;
	  }

	  public boolean supportsLimitOffset() {
	    return true;
	  }

	  public String getLimitString(String sqlstr, int offset, String offsetstr, int limit, String limitstr){
	    int i = 0;
	    if (sqlstr.trim().toLowerCase().endsWith(" for update")){
	    	sqlstr = sqlstr.substring(0, sqlstr.length() - 11);
	        i = 1;
	    }
	    StringBuffer localStringBuffer = new StringBuffer(sqlstr.length() + 100);
	    if (offset > 1){
	      localStringBuffer.append("select * from ( select row_.*, rownum rownum_ from ( ");
	    }else{
	      localStringBuffer.append("select * from ( ");
	    }
	    localStringBuffer.append(sqlstr);
	    if (offset > 1) {
	      String str = offsetstr + "+" + limitstr;
	      localStringBuffer.append(" ) row_  where rownum <=  " + str + ") where rownum_ > " + offsetstr);
	    }else {
	      localStringBuffer.append(" ) where rownum <= " + limitstr);
	    }
	    if (i != 0)
	      localStringBuffer.append(" for update");
	    return localStringBuffer.toString();
	  }
	}