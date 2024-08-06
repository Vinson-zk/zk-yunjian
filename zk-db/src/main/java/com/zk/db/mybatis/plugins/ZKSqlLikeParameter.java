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
 * @Title: ZKSqlLikeParameter.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.plugins 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:33:49 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis.plugins;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKStringUtils;
import com.zk.db.mybatis.ZKMybatisSqlHelper;
import com.zk.db.mybatis.commons.ZKSqlSource;

/**
 * 不建议使用，自行在 sql 中处理。
 * 
 * @ClassName: ZKSqlLikeParameter
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@Deprecated
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }), })
public class ZKSqlLikeParameter implements Interceptor {

    private Logger logger = LogManager.getLogger(getClass());

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql = ms.getBoundSql(parameter);

//        MetaObject metaObject = MetaObject.forObject(ms, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
//        DynamicContext context = new DynamicContext(ms.getConfiguration(), boundSql.getParameterObject());
//        String mapperSql = SqlHelper.getMapperSql(metaObject, context);

        String sql = boundSql.getSql();

        if (!ZKStringUtils.isEmpty(sql) && !ZKMybatisSqlHelper.getDialect().isReplace(sql)) {
//          logger.info("[^_^:2180109-0028-001] sql->{}", mapperSql);
//          mapperSql = SqlHelper.getDialect().replaceLikeParam(mapperSql);
//            logger.info("[^_^:2180109-0028-001] sql->{}", mapperSql);
//            // mapper sql 不为空
//          BoundSql newBoundSql = SqlHelper.getBoundSql(mapperSql, ms.getConfiguration(), context, boundSql.getParameterObject());
            sql = ZKMybatisSqlHelper.formatSql(sql);
            logger.info("[^_^:2180109-0028-001] sql->{}", sql);
            sql = ZKMybatisSqlHelper.getDialect().replaceLikeParam(sql);
            logger.info("[^_^:2180109-0028-002] sql->{}", sql);

            // 解决MyBatis 分页foreach 参数失效 begin
            SqlSource sqlSource = new ZKSqlSource(ms.getConfiguration(), sql, boundSql);
            MappedStatement newMs = ZKMybatisSqlHelper.newMappedStatement(ms.getId(), ms, sqlSource,
                    ms.getResultMaps());
            // 解决MyBatis 分页foreach 参数失效 end
            invocation.getArgs()[0] = newMs;
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
