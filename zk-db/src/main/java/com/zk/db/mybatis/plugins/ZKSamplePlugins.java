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
 * @Title: ZKSamplePlugins.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.plugins 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:33:23 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis.plugins;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.mybatis.ZKMybatisSqlHelper;

/** 
* @ClassName: ZKSamplePlugins 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
        @Signature(type = ParameterHandler.class, method = "setParameters", args = { PreparedStatement.class }),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class ZKSamplePlugins implements Interceptor {

    protected Logger logger = LogManager.getLogger(getClass());

    /**
     * 简单包装参数
     *
     * @param parameter
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object wrapCollection(final Object parameter) {
        if (parameter instanceof List) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", parameter);
            return map;
        }
        else if (parameter != null && parameter.getClass().isArray()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("zkray", parameter);
            return map;
        }else if(parameter instanceof Map){
            ((Map)parameter).put("_zk_str", "#{v}");
        }
        return parameter;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        logger.info("===============================================================");
        logger.info("=== TestPlugins ===============================================");
        logger.info("===============================================================");
        logger.info("[^_^:20180305-0847-001-1] invocation -> target class: " + target.getClass().getName());
        logger.info("[^_^:20180305-0847-001-2] invocation -> method name: " + invocation.getMethod().getName());
        Object[] objs = invocation.getArgs();
        logger.info("[^_^:20180305-0847-001-3] invocation -> method args length:" + objs.length);
        int i = 0;
        for (Object obj : objs) {
            ++i;
            logger.info("[^_^:20180305-0847-001-3] invocation -> method args " + i + ": " + obj);
        }
        logger.info("---------------------------------------------------------------");
        if (target instanceof Executor) {
            MappedStatement ms = (MappedStatement) objs[0];
            Object parameter = objs[1];

            logger.info("01.SqlSource -> " + ms.getSqlSource());

            ms = ms.getConfiguration().getMappedStatement(ms.getId());

            Object params = wrapCollection(parameter);
            Configuration configuration = ms.getConfiguration();// session.getConfiguration();
            MappedStatement mappedStatement = configuration.getMappedStatement(ms.getId());// configuration.getMappedStatement(namespace);
            TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
            BoundSql boundSql = mappedStatement.getBoundSql(params);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            String sql = boundSql.getSql();
            logger.info("02.boundSql.sql -> " + sql);
            if (parameterMappings != null) {
                for (i = 0; i < parameterMappings.size(); i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    if (parameterMapping.getMode() != ParameterMode.OUT) {
                        Object value;
                        String propertyName = parameterMapping.getProperty();
                        if (boundSql.hasAdditionalParameter(propertyName)) {
                            value = boundSql.getAdditionalParameter(propertyName);
                        }
                        else if (params == null) {
                            value = null;
                        }
                        else if (typeHandlerRegistry.hasTypeHandler(params.getClass())) {
                            value = params;
                        }
                        else {
                            MetaObject metaObject = configuration.newMetaObject(params);
                            value = metaObject.getValue(propertyName);
                        }
                        JdbcType jdbcType = parameterMapping.getJdbcType();
                        if (value == null && jdbcType == null)
                            jdbcType = configuration.getJdbcTypeForNull();
                    }
                }

//                SqlSource sqlSource = ms.getSqlSource();

            }

            BoundSql boundSql2 = ms.getBoundSql(parameter);
            MetaObject metaObject = MetaObject.forObject(ms, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
                    ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY, ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
            DynamicContext context = new DynamicContext(ms.getConfiguration(), boundSql2.getParameterObject());
            ZKMybatisSqlHelper.getMapperSql(metaObject, context);
            if (!ZKStringUtils.isEmpty(context.getSql())) {
                logger.info("03.context.getSql() -> " + context.getSql());
            }
//            SqlSource sqlSource = ms.getSqlSource();
            logger.info("04.boundSql.getSql -> " + boundSql2.getSql());
            logger.info("05.getSqlCommandType -> " + ms.getSqlCommandType());
            logger.info("06.getParameterMappings -> " + ZKJsonUtils.toJsonStr(boundSql2.getParameterMappings()));
            logger.info("07.getParameterObject -> " + ZKJsonUtils.toJsonStr(boundSql2.getParameterObject()));
        }
        if (target instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) target;
            BoundSql boundSql = statementHandler.getBoundSql();
            logger.info("08.boundSql.getSql -> " + boundSql.getSql());
            Statement stmt = (Statement) objs[0];
            logger.info("09.getParameterObject -> {}", stmt.toString());
        }
        if (target instanceof ParameterHandler) {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            MetaObject metaObject = MetaObject.forObject(parameterHandler, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
                    ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY, ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
//            Object parameterObj = metaObject.getValue("parameterObject");
            BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
            logger.info("10.boundSql.getSql -> " + boundSql.getSql());

//            PreparedStatement ps = (PreparedStatement) objs[0];
//            MetaObject metaObject2 = MetaObject.forObject(ps, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
//                    ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY, ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
//            if (metaObject2.hasGetter(ZKMybatisSqlHelper.WRAPPER.originalSql)) {
//                String originalSql = (String) metaObject2.getValue(ZKMybatisSqlHelper.WRAPPER.originalSql);
//                logger.info("11.originalSql -> " + originalSql);
//            }
        }

        logger.info("===============================================================");
        logger.info("-----------------");
        logger.info("-----------------");
        logger.info("-----------------");
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    // Vinson ===================================
//    // 取 mybatis 的原生 sql 未实现，因为通 SqlSource 取 原生 sql 不靠普 
//    public void getOriginalSql(SqlSource sqlSource) throws IllegalArgumentException, IllegalAccessException {
//        logger.info("getOriginalSql.SqlSource -> " + sqlSource.getClass().getName());
//        if (sqlSource instanceof DynamicSqlSource) {
//            DynamicSqlSource dynamicSqlSource = (DynamicSqlSource) sqlSource;
//            Field rootSqlNodeField = ZKClassUtils.getField(DynamicSqlSource.class, "rootSqlNode");
//            rootSqlNodeField.setAccessible(true);
//            // MixedSqlNode TextSqlNode
//            MixedSqlNode msqlNode = (MixedSqlNode) rootSqlNodeField.get(dynamicSqlSource);
////            msqlNode.
//        }
//    }

}
