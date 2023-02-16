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

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.commons.ZKDBConstants;
import com.zk.db.mybatis.ZKMybatisSqlHelper;

/** 
* @ClassName: ZKSqlPage 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }), })
public class ZKSqlPage implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

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

//      Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = ms.getBoundSql(parameter);
        Object parameterObject = boundSql.getParameterObject();

        // 取 分页参数
        ZKPage<?> page = convertParameter(parameterObject);

        // 分页参数存在，进行分页
        if (page != null && !ZKStringUtils.isEmpty(boundSql.getSql())) {
            String sql = boundSql.getSql().trim();
            sql = ZKMybatisSqlHelper.formatSql(sql);
            if (!ZKMybatisSqlHelper.getDialect().isPage(sql)) {
                // 设置总记录数
                page.setTotalCount(ZKMybatisSqlHelper.getCount(sql, null, ms, parameterObject, boundSql, logger));
                // 分页查询 本地化对象 更换数据库注意修改实现
                String pageSql = ZKMybatisSqlHelper.generatePageSql(sql, page, ZKMybatisSqlHelper.getDialect());
                logger.info("[^_^:2180109-0149-001] pageSql->{}", pageSql);
                invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
                BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(),
                        boundSql.getParameterObject());
                // 解决MyBatis 分页foreach 参数失效
                ZKMybatisSqlHelper.validForeachParams(boundSql, newBoundSql);
                MappedStatement newMs = ZKMybatisSqlHelper.copyFromMappedStatement(ms,
                        new ZKMybatisSqlHelper.BoundSqlSqlSource(newBoundSql));
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

    }

}
