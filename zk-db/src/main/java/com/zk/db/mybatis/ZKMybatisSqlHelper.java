/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKMybatisSqlHelper.java 
 * @author Vinson 
 * @Package com.zk.db.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:38:12 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.commons.ZKDBConstants;
import com.zk.db.dialect.ZKDialect;
import com.zk.db.dialect.support.ZKMySqlDialect;
import com.zk.db.dynamic.spring.dataSource.ZKDynamicDataSource;

/** 
* @ClassName: ZKMybatisSqlHelper 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMybatisSqlHelper {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 环境变量配置的 KEY
     */
    public static interface envKey {

        public static final String jdbcType = "jdbc.type";
    }

    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    public static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    /**
     * 一些参数名适配
     * 
     * @author zk
     *
     */
    public static interface WRAPPER {
        public static final String originalSql = "h.statement.originalSql";

        public static final String rootSqlNode = "sqlSource.rootSqlNode";

        public static final String metaParameters = "metaParameters";

        public static final String boundSqlAdditionalParameters = "additionalParameters";
    }

    private static ZKDialect dialect = null;

    /**
     * 取数据方言实体
     * 
     * @return
     */
    public static ZKDialect getDialect() {
        if (dialect == null) {
            dialect = initDialect(null);
        }
        return dialect;
    }

    /**
     * 手动新建一个 BoundSql 时，解决MyBatis 分页foreach 参数失效 start
     * 
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static void validForeachParams(BoundSql oldBoundSql, BoundSql newBoundSql) throws IllegalAccessException {
        // 解决MyBatis 分页foreach 参数失效 start
        Object obj = ZKClassUtils.getFieldValue(oldBoundSql, WRAPPER.metaParameters);
        if (obj != null) {
            ZKClassUtils.setFieldValue(newBoundSql, WRAPPER.metaParameters, (MetaObject) obj);
        }
        obj = ZKClassUtils.getFieldValue(oldBoundSql, WRAPPER.boundSqlAdditionalParameters);
        if (obj != null) {
            ZKClassUtils.setFieldValue(newBoundSql, WRAPPER.boundSqlAdditionalParameters, (Map<String, Object>) obj);
        }
    }

    /**
     * 从 MappedStatement 中取 rootSqlNode; 用 MetaObject 包装 MappedStatement
     */
    public static SqlNode getRootSqlNode(MetaObject metaObject) {
        if (metaObject.hasGetter(ZKMybatisSqlHelper.WRAPPER.rootSqlNode)) {
            return (SqlNode) metaObject.getValue(ZKMybatisSqlHelper.WRAPPER.rootSqlNode);
        }
        return null;
    }

    /**
     * 取 mapper sql
     */
    public static String getMapperSql(MetaObject metaObject, DynamicContext context) {
        SqlNode sqlNode = getRootSqlNode(metaObject);
        if (sqlNode != null) {
            sqlNode.apply(context);
            if (!ZKStringUtils.isEmpty(context.getSql())) {
                return formatSql(context.getSql());
            }
        }
        return null;
    }

    /**
     * 根据 mapper sql 生成 BoundSql
     */
    public static BoundSql getBoundSql(String sql, Configuration configuration, DynamicContext context,
            Object parameterObject) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }

    /**
     * 格式化 sql
     */
    public static String formatSql(String sql) {
        if (sql != null) {
            return sql.trim().replaceAll("([\\t\\n\\r ]+)", " ");
        }
        return null;
    }

    /**
     * 设置属性，支持自定义方言类和制定数据库的方式 <code>dialectClass</code>,自定义方言类。可以不配置这项
     * <ode>dbms</ode> 数据库类型，插件支持的数据库 <code>sqlPattern</code> 需要拦截的SQL ID
     * 
     * @param p
     *            属性
     */
    protected static ZKDialect initDialect(String dbType) {
        ZKDialect dialect = null;
        if (ZKStringUtils.isEmpty(dbType)) {
            dbType = ZKEnvironmentUtils.getString(envKey.jdbcType, ZKDBConstants.defaultJdbcType);
        }
        if ("mysql".equals(dbType)) {
            dialect = new ZKMySqlDialect();
        }
//      else if ("db2".equals(dbType)){
//          dialect = new DB2Dialect();
//        }else if("derby".equals(dbType)){
//          dialect = new DerbyDialect();
//        }else if("h2".equals(dbType)){
//          dialect = new H2Dialect();
//        }else if("hsql".equals(dbType)){
//          dialect = new HSQLDialect();
//        }else if("oracle".equals(dbType)){
//          dialect = new OracleDialect();
//        }else if("postgre".equals(dbType)){
//          dialect = new PostgreSQLDialect();
//        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
//          dialect = new SQLServer2005Dialect();
//        }else if("sybase".equals(dbType)){
//          dialect = new SybaseDialect();
//        }
        if (dialect == null) {
            throw new RuntimeException("mybatis dialect error.");
        }
        return dialect;
    }

    /**
     * 查询总纪录数
     * 
     * @param sql
     *            SQL语句
     * @param connection
     *            数据库连接
     * @param mappedStatement
     *            mapped
     * @param parameterObject
     *            参数
     * @param boundSql
     *            boundSql
     * @return 总记录数
     * @throws SQLException
     *             sql查询错误
     * @throws IllegalAccessException
     */
    public static int getCount(final String sql, final Connection connection, final MappedStatement mappedStatement,
            final Object parameterObject, final BoundSql boundSql, Logger log)
            throws SQLException, IllegalAccessException {
        String dbType = ZKEnvironmentUtils.getString(envKey.jdbcType, ZKDBConstants.defaultJdbcType);
        final String countSql;
        if ("oracle".equals(dbType)) {
            countSql = "select count(1) from (" + sql + ") tmp_count";
        }
        else {
            countSql = "select count(1) from (" + removeOrders(sql) + ") tmp_count";
//          countSql = "select count(1) " + removeSelect(removeOrders(sql));
        }
        log.info("[^_^:20220418-2349-001] countSql: {}", countSql);
        Connection conn = connection;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("COUNT SQL: "
                        + ZKStringUtils.replaceEach(countSql, new String[] { "\n", "\t" }, new String[] { " ", " " }));
            }
            if (conn == null) {
                DataSource ds = mappedStatement.getConfiguration().getEnvironment().getDataSource();
                if (ds instanceof ZKDynamicDataSource) {
                    conn = ((ZKDynamicDataSource) ds).getReadDataSource().getConnection();
                }
                else {
                    conn = ds.getConnection();
                }
            }
            ps = conn.prepareStatement(countSql);
            BoundSql countZK = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), parameterObject);
            // 解决MyBatis 分页foreach 参数失效
            validForeachParams(boundSql, countZK);
//          if (ClassUtils.getFieldValue(boundSql, "metaParameters") != null) {
//              MetaObject mo = (MetaObject) ClassUtils.getFieldValue(boundSql, "metaParameters");
//              ClassUtils.setFieldValue(countZK, "metaParameters", mo);
//          }
//          //解决MyBatis 分页foreach 参数失效 end 
            ZKMybatisSqlHelper.setParameters(ps, mappedStatement, countZK, parameterObject);
            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     *
     * @param ps
     *            表示预编译的 SQL 语句的对象。
     * @param mappedStatement
     *            MappedStatement
     * @param boundSql
     *            SQL
     * @param parameterObject
     *            参数对象
     * @throws java.sql.SQLException
     *             数据库异常
     */
    @SuppressWarnings("unchecked")
    public static void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
            Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    }
                    else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    }
                    else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    }
                    else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
                            && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value)
                                    .getValue(propertyName.substring(prop.getName().length()));
                        }
                    }
                    else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    @SuppressWarnings("rawtypes")
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
                                + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    /**
     * 根据数据库方言，生成特定的分页sql
     * 
     * @param sql
     *            Mapper中的Sql语句
     * @param page
     *            分页对象
     * @param dialect
     *            方言类型
     * @return 分页SQL
     */
    public static String generatePageSql(String sql, ZKPage<?> page, ZKDialect dialect) {
        if (dialect.supportsLimit()) {
            return dialect.getLimitString(sql, page.getStartRow(), page.getPageSize());
        }
        else {
            return sql;
        }
    }

    /**
     * 去除qlString的select子句。
     * 
     * @param qlString
     * @return
     */
    @SuppressWarnings("unused")
    private static String removeSelect(String qlString) {
        int beginPos = qlString.toLowerCase().indexOf("from");
        return qlString.substring(beginPos);
    }

    /**
     * 去除hql的orderBy子句。
     * 
     * @param qlString
     * @return
     */
    private static String removeOrders(String qlString) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(qlString);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 自定义 BoundSql 类
     * 
     * @author zk
     *
     */
    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
