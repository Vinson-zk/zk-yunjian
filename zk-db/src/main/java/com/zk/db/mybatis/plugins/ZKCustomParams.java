package com.zk.db.mybatis.plugins;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.db.commons.ZKDBConstants;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;
import com.zk.db.mybatis.session.ZKDBMybatisConfiguration;

/**
 * Copyright (c) 2017-2022 Vinson. address: All rights reserved This software is the confidential and proprietary information of Vinson. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered
 * into with Vinson;
 *
 * @Description: 自定义参数插件；需要配置在其他有处理 sql 的插件前执行；
 * @ClassName ZKCustomParams
 * @Package com.zk.db.mybatis.plugins
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-29 23:00:09
 **/
@Intercepts({
//        @Signature(type = ParameterHandler.class, method = "setParameters", args = { PreparedStatement.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class }),
        @Signature(type = Executor.class, method = "queryCursor", args = { MappedStatement.class, Object.class, RowBounds.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class })
})
public class ZKCustomParams implements Interceptor {

    protected Logger log = LogManager.getLogger(getClass());

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object wrapCollection(ZKDBMybatisConfiguration zkConfiguration, String mappedStatementId,
            final Object parameter) {

        log.info("[^_^:20221007-1026-001] parameter.class:{}", parameter.getClass());
        log.info("[^_^:20221007-1026-001] mappedStatementId:{}", mappedStatementId);

        ZKDBSqlHelper sqlHelper = zkConfiguration.getSqlHelper(mappedStatementId);
        if(parameter instanceof Map){
            if(sqlHelper != null){
                ((Map) parameter).put(ZKDBConstants.Mybatis_Param_Name.sqlHelper,
                        ZKDBSqlParams.as(sqlHelper));
            }else{
                log.info("[^_^:20221007-1029-001] mappedStatementId:{}, sqlHelper is null", mappedStatementId);
            }
        }
        return parameter;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] objs = invocation.getArgs();
        MappedStatement ms = (MappedStatement)objs[0];
        Configuration configuration = ms.getConfiguration();
        log.info("[^_^:20221007-1027-001] Configuration:{}", configuration.getClass());
        if (configuration instanceof ZKDBMybatisConfiguration) {
            Object parameter = objs[1];
            this.wrapCollection((ZKDBMybatisConfiguration) configuration, ms.getId(), parameter);
        }
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
}

/**
 * 自定义注入的 mysql mybatis 参数
 * @MethodName
 * @return
 * @throws
 * @Author bs
 * @DATE 2022-10-07 15:25:35
 */
class ZKDBSqlParams implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;
    final String tn;
    final String ta;
    final String cols;
    final String where;

    public static ZKDBSqlParams as(ZKDBSqlHelper sqlHelper){
        return new ZKDBSqlParams(sqlHelper);
    }

    public ZKDBSqlParams(ZKDBSqlHelper sqlHelper){
        this.tn = sqlHelper.getTableName();
        this.ta = sqlHelper.getTableAlias();
        this.cols = sqlHelper.getBlockSqlCols();
        this.where = sqlHelper.getBlockSqlWhere();
    }

    public String getTn() {
        return tn;
    }

    public String getTa() {
        return ta;
    }

    public String getCols() {
        return cols;
    }

    public String getWhere() {
        return where;
    }
}