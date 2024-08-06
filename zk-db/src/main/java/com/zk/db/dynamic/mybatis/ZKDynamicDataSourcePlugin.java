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
 * @Title: ZKDynamicDataSourcePlugin.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic.mybatis 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:30:51 AM 
 * @version V1.0   
*/
package com.zk.db.dynamic.mybatis;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.zk.db.dynamic.ZKDynamicDataSourceHelper;
import com.zk.db.dynamic.ZKPatternType;

/** 
* @ClassName: ZKDynamicDataSourcePlugin 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class ZKDynamicDataSourcePlugin implements Interceptor {

    protected static final Logger logger = LogManager.getLogger(ZKDynamicDataSourcePlugin.class);

    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    private static final Map<String, ZKPatternType> cacheMap = new ConcurrentHashMap<String, ZKPatternType>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();

        logger.debug("[^_^:20170625-0903-001] 执行 sql 事务启动情况 {synchronizationActive:{}}", synchronizationActive);

        if (!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];

            ZKPatternType dataSourcePattern = null;

            if ((dataSourcePattern = cacheMap.get(ms.getId())) == null) {
                // 读方法
                if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    // !selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                    if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        dataSourcePattern = ZKPatternType.WRITE;
                    }
                    else {
                        // select into
                        BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                        if (sql.matches(REGEX)) {
                            dataSourcePattern = ZKPatternType.WRITE;
                        }
                        else {
                            dataSourcePattern = ZKPatternType.READ;
                        }
                    }
                }
                else {
                    dataSourcePattern = ZKPatternType.WRITE;
                }
                cacheMap.put(ms.getId(), dataSourcePattern);
            }
            logger.debug("[^_^:20170625-0903-002] 执行 sql 方法[{}]，dataSourcePattern[{}] Strategy, SqlCommandType[{}]",
                    ms.getId(), dataSourcePattern.name(), ms.getSqlCommandType().name());
            ZKDynamicDataSourceHelper.putDataSource(dataSourcePattern);
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
