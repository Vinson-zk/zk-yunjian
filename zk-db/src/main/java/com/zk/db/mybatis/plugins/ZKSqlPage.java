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
 * @Title: ZKSqlPage.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.plugins 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:34:13 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis.plugins;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.commons.ZKDBConstants;
import com.zk.db.mybatis.ZKMybatisSqlHelper;
import com.zk.db.mybatis.ZKMybatisSqlHelper.envKey;
import com.zk.db.mybatis.commons.ZKSqlSource;
import com.zk.db.parser.ZKCountSqlParser.ZKModel;

/**
 * 参考：https://github.com/pagehelper/Mybatis-PageHelper
 * 
 * 支持自动生成查询总数方法，或开发提供总数查询方法；
 * 
 * @ClassName: ZKSqlPage
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }), })
public class ZKSqlPage implements Interceptor {

    private Logger logger = LogManager.getLogger(getClass());

    private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<ResultMapping>(0);

    private static ZKPage<?> convertParameter(Object parameterObject) {
        if (parameterObject != null) {
            if (parameterObject instanceof ZKPage) {
                return (ZKPage<?>) parameterObject;
            }
            else {
                MetaObject metaObject = MetaObject.forObject(parameterObject, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
                        ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY,
                        ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
                if (metaObject.hasGetter(ZKDBConstants.PARAM_NAME.Page)) {
                    return (ZKPage<?>) metaObject.getValue(ZKDBConstants.PARAM_NAME.Page);
                }
            }
        }
        return null;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Executor executor = (Executor) invocation.getTarget();
        Object[] invocationArgs = invocation.getArgs();
        MappedStatement sourceMappedStatement = (MappedStatement) invocationArgs[0];
        Object sourceParameter = invocationArgs[1];
//        RowBounds sourceRowBounds = (RowBounds) invocationArgs[2];
        ResultHandler<?> sourceResultHandler = (ResultHandler<?>) invocationArgs[3];

        BoundSql sourceBoundSql = null;
//        CacheKey cacheKey = null;
        if (invocationArgs.length == 4) {
            // 4 个参数时
            sourceBoundSql = sourceMappedStatement.getBoundSql(sourceParameter);
//            cacheKey = executor.createCacheKey(sourceMappedStatement, sourceParameter, sourceRowBounds, sourceBoundSql);
        }
        else {
            // 6 个参数时
//            cacheKey = (CacheKey) invocationArgs[4];
            sourceBoundSql = (BoundSql) invocationArgs[5];
        }

        Object sourceParameterObject = sourceBoundSql.getParameterObject();

        // 从参数中 取 分页参数
        ZKPage<?> page = convertParameter(sourceParameterObject);

        // 分页参数存在，进行分页
        if (page != null && !ZKStringUtils.isEmpty(sourceBoundSql.getSql())) {
            String sourceSql = sourceBoundSql.getSql().trim(); // 含占位符的 sql
            sourceSql = ZKMybatisSqlHelper.formatSql(sourceSql);
            if (!ZKMybatisSqlHelper.getDialect().isPage(sourceSql)) {
                List<ParameterMapping> sourceParameterMappings = sourceBoundSql.getParameterMappings();
                /*
                 * 设置查询结果总数；支持自动生成查询总数方法，或开发提供总数查询方法；
                 * 
                 */
                page.setTotalCount(this.count(executor, sourceSql, sourceBoundSql, sourceMappedStatement,
                        sourceParameter, sourceParameterMappings, sourceResultHandler));
                /*
                 * 分页查询 本地化对象 更换数据库注意修改实现
                 */
                String pageSql = ZKMybatisSqlHelper.getDialect().getPageSql(sourceSql, page);
                logger.info("[^_^:2180109-0149-001] pageSql->{}", pageSql);
//                invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
                invocation.getArgs()[2] = RowBounds.DEFAULT;
                
                // 解决 MyBatis 动态参数【如：foreach 参数】 失效问题； begin
                SqlSource sqlSource = new ZKSqlSource(sourceMappedStatement.getConfiguration(), pageSql,
                        sourceBoundSql);
                MappedStatement newMs = ZKMybatisSqlHelper.newMappedStatement(sourceMappedStatement.getId(),
                        sourceMappedStatement, sqlSource, sourceMappedStatement.getResultMaps());
                // 解决 MyBatis 动态参数【如：foreach 参数】 失效问题； end

                if (invocationArgs.length > 4) {
                    invocationArgs[5] = newMs.getBoundSql(sourceParameter);
                }
                invocation.getArgs()[0] = newMs;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    /**
     * 分页查询，总数查询处理；支持自动生成查询总数方法，或开发提供总数查询方法；
     * 
     * 开发提供总数查询方法：约定总数查询方法名是在 page 分页记录查询方法名后添加 ZKMybatisSqlHelper.envKey.countSuffix 做为方法名；
     *
     *
     * @Title: count
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jan 8, 2024 2:49:07 PM
     * @param executor
     * @param sourceSql
     * @param sourceBoundSql
     * @param sourceMappedStatement
     * @param sourceParameterObject
     * @param sourceParameterMapping
     * @param sourceResultHandler
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @return Long
     */
    private Long count(Executor executor, String sourceSql, BoundSql sourceBoundSql,
            MappedStatement sourceMappedStatement, Object sourceParameterObject,
            List<ParameterMapping> sourceParameterMapping, ResultHandler<?> sourceResultHandler)
            throws SQLException, IllegalAccessException {

//        return ZKMybatisSqlHelper.getCount(sourceSql, null, sourceMappedStatement,
//                sourceParameterObject, sourceBoundSql);

        // 取自动实现的
        String countMsId = sourceMappedStatement.getId() + envKey.countSuffix;
        MappedStatement countMs = ZKMybatisSqlHelper.getExistedMappedStatement(sourceMappedStatement.getConfiguration(),
                countMsId);
        if (countMs == null) {
            String countSql = ZKMybatisSqlHelper.getDialect().getCountSql(sourceSql, "count(0)", ZKModel.Perfect, true);

            // count 查询返回值 int 结果映射
            List<ResultMap> resultMaps = new ArrayList<ResultMap>();
            ResultMap resultMap = new ResultMap.Builder(sourceMappedStatement.getConfiguration(), countMsId, Long.class,
                    EMPTY_RESULTMAPPING).build();
            resultMaps.add(resultMap);

            countMs = ZKMybatisSqlHelper.newMappedStatement(countMsId, sourceMappedStatement,
                    new ZKSqlSource(sourceMappedStatement.getConfiguration(), countSql, sourceParameterMapping,
                            sourceBoundSql),
                    resultMaps);
        }

        return ZKMybatisSqlHelper.executeCount(executor, countMs, sourceParameterObject, null);
    }

}
