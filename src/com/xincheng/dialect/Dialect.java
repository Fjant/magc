package com.xincheng.dialect;

public class Dialect {
	 public boolean supportsLimit(){
	    return false;
	  }

	  public boolean supportsLimitOffset() {
	    return supportsLimit();
	  }

	  public String getLimitString(String sql, int offset, int limit) {
	    return getLimitString(sql, offset, Integer.toString(offset - 1), limit, Integer.toString(limit));
	  }

	  public String getLimitString(String sql, int offset, String offsetstr, int limit, String limitstr) {
	    throw new UnsupportedOperationException("paged queries not supported");
	  }
}

