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
 * @Title: ZKSqlRunCost.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.plugins 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:34:44 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis.plugins;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @ClassName: ZKSqlRunCost
 * @Description: sql 运行时效
 * @author Vinson
 * @version 1.0
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
        @Signature(type = StatementHandler.class, method = "batch", args = { Statement.class }) })
public class ZKSqlRunCost implements Interceptor {

    private Logger logger = LogManager.getLogger(getClass());

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();
        logger.info("[^_^:20200805-1859-001] begin... ...");
        try {
            return invocation.proceed();
        } finally {
//            /** 打印 sql；一般注释掉，不打印 */
//            Object target = invocation.getTarget();
//            BoundSql boundSql = null;
////            Method method = invocation.getMethod();
////            if ("query".equals(method.getName())) {
//            if (target instanceof StatementHandler) {
//                StatementHandler statementHandler = (StatementHandler) target;
//                boundSql = statementHandler.getBoundSql();
//            }
//            else {
//                Object[] args = invocation.getArgs();
//                MappedStatement ms = (MappedStatement) args[0];
//                Object parameter = args[1];
//                boundSql = ms.getBoundSql(parameter);
//            }
//
//            String sql = boundSql.getSql();
////            if (sql.length() > 1000) {
////                sql = sql.substring(0, 1000) + "......";
////            }
//            logger.info("[^_^:20200805-1859-002] sql：{} ", ZKMybatisSqlHelper.formatSql(sql));

            /** sql 执行结束 */
            long endTime = System.currentTimeMillis();
            logger.info("[^_^:20200805-1859-002] end; run 耗时(/毫秒)：{} 毫秒; 起始/结束时间(毫秒)：{}/{}；", endTime - startTime,
                    startTime, endTime);
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof Executor) {
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
