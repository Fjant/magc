package com.xincheng.dialect.plugin;

import java.util.Iterator;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.xincheng.dialect.Dialect;


@Intercepts({@Signature(  
        type= Executor.class,  
        method = "query",  
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})  
public class OffsetLimitInterceptor implements Interceptor{
	static int MAPPED_STATEMENT_INDEX = 0;  
    static int PARAMETER_INDEX = 1;  
    static int ROWBOUNDS_INDEX = 2;  
    static int RESULT_HANDLER_INDEX = 3;  
      
    Dialect dialect;  
    
    public Object intercept(Invocation invocation) throws Throwable {  
        processIntercept(invocation.getArgs());  
        return invocation.proceed();  
    }  
    void processIntercept(final Object[] queryArgs) {  
        //queryArgs = query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler)  
        MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];  
        Object parameter = queryArgs[PARAMETER_INDEX];  
        final RowBounds rowBounds = (RowBounds)queryArgs[ROWBOUNDS_INDEX];  
        int offset = rowBounds.getOffset();  
        int limit = rowBounds.getLimit();  
          
        if(dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {  
            BoundSql boundSql = ms.getBoundSql(parameter);  
            String sql = boundSql.getSql().trim();  
            if (dialect.supportsLimitOffset()) {  
                sql = dialect.getLimitString(sql, offset, limit);  
                offset = RowBounds.NO_ROW_OFFSET;  
            } else {  
                sql = dialect.getLimitString(sql, 0, limit);  
            }  
            limit = RowBounds.NO_ROW_LIMIT;  
              
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset,limit);  
            BoundSql localBoundSql2 = copyFromBoundSql(ms, boundSql, sql);
            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(localBoundSql2));  
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;  
        }  
    }  
      
    private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {  
        Builder builder = new MappedStatement.Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());  
        builder.resource(ms.getResource());  
        builder.fetchSize(ms.getFetchSize());  
        builder.statementType(ms.getStatementType());  
        builder.keyGenerator(ms.getKeyGenerator());  
        builder.keyProperty(ms.getKeyProperty());  
        builder.timeout(ms.getTimeout());  
        builder.parameterMap(ms.getParameterMap());  
        builder.resultMaps(ms.getResultMaps());  
        builder.cache(ms.getCache());  
        MappedStatement newMs = builder.build();  
        return newMs;  
    }  
  
    public Object plugin(Object target) {  
        return Plugin.wrap(target, this);  
    }  
  
    public void setProperties(Properties properties) {  
    	String dialectClass = properties.getProperty("dialectClass");
        try {  
            dialect = (Dialect)Class.forName(dialectClass).newInstance();  
        } catch (Exception e) {  
            throw new RuntimeException("cannot create dialect instance by dialectClass:"+dialectClass,e);  
        }   
        System.out.println(OffsetLimitInterceptor.class.getSimpleName()+".dialect="+dialectClass);  
    }  
      
    public static class BoundSqlSqlSource implements SqlSource {  
        BoundSql boundSql;  
        public BoundSqlSqlSource(BoundSql boundSql) {  
            this.boundSql = boundSql;  
        }  
        public BoundSql getBoundSql(Object parameterObject) {  
            return boundSql;  
        }  
    }  
      
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql){
      BoundSql localBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
      Iterator<ParameterMapping> localIterator = boundSql.getParameterMappings().iterator();
      while (localIterator.hasNext())
      {
        ParameterMapping localParameterMapping = (ParameterMapping)localIterator.next();
        String str = localParameterMapping.getProperty();
        if (boundSql.hasAdditionalParameter(str))
          localBoundSql.setAdditionalParameter(str, boundSql.getAdditionalParameter(str));
      }
      return localBoundSql;
    }  
}