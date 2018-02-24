package com.xincheng.ibatis;

import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

public class SqlMapClientDaoSupport extends DaoSupport {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private SqlSessionFactory sqlSessionFactory;
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	protected void checkDaoConfig() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Assert.notNull(this.sqlSessionFactory, "sqlSessionFactory must be not null");
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return this.sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return this.sqlSessionTemplate;
	}

	public static interface SqlSessionCallback {
		public Object doInSession(SqlSession paramSqlSession);
	}

	public static class SqlSessionTemplate {
		private int batchSize = 500;
		SqlSessionFactory sqlSessionFactory;

		public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
			this.sqlSessionFactory = sqlSessionFactory;
		}

		public Object execute(SqlMapClientDaoSupport.SqlSessionCallback sqlSessionCallback) throws Exception {
			SqlSession sqlSession = null;
			try {
				sqlSession = this.sqlSessionFactory.openSession();
				Object localObject = sqlSessionCallback.doInSession(sqlSession);
				return localObject;
			} catch (Exception e) {
				e.printStackTrace();				
				throw e;
			} finally {
				if (sqlSession != null)
					sqlSession.close();
			}
		}

		public Object executeBatch(SqlMapClientDaoSupport.SqlSessionCallback sqlSessionCallback) throws Exception {
			SqlSession sqlSession = null;
			try {
				sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH);
				Object localObject = sqlSessionCallback.doInSession(sqlSession);
				sqlSession.commit();
				return localObject;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (sqlSession != null)
					sqlSession.close();
			}
		}

		public Object selectOne(final String paramString, final Object paramObject) throws Exception {
			return execute(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession sqlSession) {
					return sqlSession.selectOne(paramString, paramObject);
				}
			});
		}

		public List selectList(final String paramString, final Object paramObject, final int paramInt1, final int paramInt2) throws Exception {
			return (List) execute(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession paramAnonymousSqlSession) {
					return paramAnonymousSqlSession.selectList(paramString, paramObject, new RowBounds(paramInt1, paramInt2));
				}
			});
		}

		public List selectList(final String paramString, final Object paramObject) throws Exception {
			return (List) execute(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession paramAnonymousSqlSession) {
					return paramAnonymousSqlSession.selectList(paramString, paramObject);
				}
			});
		}

		public int delete(final String paramString, final Object paramObject) throws Exception {
			return ((Integer) execute(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession paramAnonymousSqlSession) {
					return Integer.valueOf(paramAnonymousSqlSession.delete(paramString, paramObject));
				}
			})).intValue();
		}

		public int update(final String paramString, final Object paramObject) throws Exception {
			return ((Integer) execute(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession paramAnonymousSqlSession) {
					return Integer.valueOf(paramAnonymousSqlSession.update(paramString, paramObject));
				}
			})).intValue();
		}

		public int insert(final String paramString, final Object paramObject) throws Exception {
			return ((Integer) execute(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession paramAnonymousSqlSession) {
					return Integer.valueOf(paramAnonymousSqlSession.insert(paramString, paramObject));
				}
			})).intValue();
		}

		public int insertBatch(final String paramString, final List paramList) throws Exception {
			return ((Integer) executeBatch(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession paramAnonymousSqlSession) {
					Integer localInteger = null;
					int i = 0;
					Iterator localIterator = paramList.iterator();
					while (localIterator.hasNext()) {
						Object localObject = localIterator.next();
						localInteger = Integer.valueOf(paramAnonymousSqlSession.insert(paramString, localObject));
						i++;
						if (i == SqlMapClientDaoSupport.SqlSessionTemplate.this.batchSize) {
							paramAnonymousSqlSession.commit();
							i = 0;
						}
					}
					return localInteger;
				}
			})).intValue();
		}

		public int updateBatch(final String paramString, final List paramList) throws Exception {
			return ((Integer) executeBatch(new SqlMapClientDaoSupport.SqlSessionCallback() {
				public Object doInSession(SqlSession paramAnonymousSqlSession) {
					Integer localInteger = null;
					int i = 0;
					Iterator localIterator = paramList.iterator();
					while (localIterator.hasNext()) {
						Object localObject = localIterator.next();
						localInteger = Integer.valueOf(paramAnonymousSqlSession.update(paramString, localObject));
						i++;
						if (i == SqlMapClientDaoSupport.SqlSessionTemplate.this.batchSize) {
							paramAnonymousSqlSession.commit();
							i = 0;
						}
					}
					return localInteger;
				}
			})).intValue();
		}
	}
}
