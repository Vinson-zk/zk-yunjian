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
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 简单包装参数
     *
     * @param parameter
     * @return
     */
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

            logger.info("----- " + ms.getSqlSource());

            ms = ms.getConfiguration().getMappedStatement(ms.getId());

            Object params = wrapCollection(parameter);
            Configuration configuration = ms.getConfiguration();// session.getConfiguration();
            MappedStatement mappedStatement = configuration.getMappedStatement(ms.getId());// configuration.getMappedStatement(namespace);
            TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
            BoundSql boundSql2 = mappedStatement.getBoundSql(params);
            List<ParameterMapping> parameterMappings = boundSql2.getParameterMappings();
            String sql = boundSql2.getSql();
            logger.info("==== " + sql);
            if (parameterMappings != null) {
                for (i = 0; i < parameterMappings.size(); i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    if (parameterMapping.getMode() != ParameterMode.OUT) {
                        Object value;
                        String propertyName = parameterMapping.getProperty();
                        if (boundSql2.hasAdditionalParameter(propertyName)) {
                            value = boundSql2.getAdditionalParameter(propertyName);
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
            }

            BoundSql boundSql = ms.getBoundSql(parameter);
            MetaObject metaObject = MetaObject.forObject(ms, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
                    ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY, ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
            DynamicContext context = new DynamicContext(ms.getConfiguration(), boundSql.getParameterObject());
            ZKMybatisSqlHelper.getMapperSql(metaObject, context);
            if (!ZKStringUtils.isEmpty(context.getSql())) {
                logger.info("context.getSql() -> " + context.getSql());
            }
//            SqlSource sqlSource = ms.getSqlSource();
            logger.info("boundSql.getSql -> " + boundSql.getSql());
            logger.info("getSqlCommandType -> " + ms.getSqlCommandType());
            logger.info("getParameterMappings -> " + ZKJsonUtils.writeObjectJson(boundSql.getParameterMappings()));
            logger.info("getParameterObject -> " + ZKJsonUtils.writeObjectJson(boundSql.getParameterObject()));
        }
        if (target instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) target;
            BoundSql boundSql = statementHandler.getBoundSql();
            logger.info("boundSql.getSql -> " + boundSql.getSql());
            Statement stmt = (Statement) objs[0];
            logger.info("getParameterObject -> {}", stmt.toString());
        }
        if (target instanceof ParameterHandler) {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            MetaObject metaObject = MetaObject.forObject(parameterHandler, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
                    ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY, ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
//            Object parameterObj = metaObject.getValue("parameterObject");
            BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
            logger.info("boundSql.getSql -> " + boundSql.getSql());

            PreparedStatement ps = (PreparedStatement) objs[0];
            MetaObject metaObject2 = MetaObject.forObject(ps, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
                    ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY, ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
            if (metaObject2.hasGetter(ZKMybatisSqlHelper.WRAPPER.originalSql)) {
                String originalSql = (String) metaObject2.getValue(ZKMybatisSqlHelper.WRAPPER.originalSql);
                logger.info("originalSql -> " + originalSql);
            }
        }
        logger.info("===============================================================");
        logger.info("===");
        logger.info("===");
        logger.info("===");
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
