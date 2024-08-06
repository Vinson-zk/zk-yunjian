/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKBoundSqlSqlSource.java 
* @author Vinson 
* @Package com.zk.db.mybatis.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 5, 2024 12:19:52 AM 
* @version V1.0 
*/
package com.zk.db.mybatis.commons;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.db.mybatis.ZKMybatisSqlHelper;

/** 
* @ClassName: ZKBoundSqlSqlSource 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSqlSource implements SqlSource {

    private final String sql;

    private final List<ParameterMapping> parameterMappings;

    private final Configuration configuration;

    private BoundSql sourceBountSql, newBountSql = null;

    /**
     * 解决 MyBatis 动态参数【如：foreach 参数】 失效问题；
     * 
     * 需要配合 ZKMybatisSqlHelper.newMappedStatement 一起使用才能解决 动态参数【如：foreach 参数】 失效
     * 
     * @param configuration
     * @param sql
     * @param sourceBountSql
     */
    public ZKSqlSource(Configuration configuration, String sql, BoundSql sourceBountSql) {
        this(configuration, sql, sourceBountSql.getParameterMappings(), sourceBountSql);
    }

    public ZKSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings,
            BoundSql sourceBountSql) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
        this.sourceBountSql = sourceBountSql;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        if (this.newBountSql != null) {
            return this.newBountSql;
        }
        this.newBountSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
        if (this.sourceBountSql != null) {
            try {
                /*
                 * 解决 MyBatis 动态参数【如：foreach 参数】 失效问题；
                 * 
                 * 需要配合 ZKMybatisSqlHelper.newMappedStatement 一起使用才有效果
                 */
                // 这里从原 sourceBountSql 中复制 additionalParameters、metaParameters 两个属性到新 newBoundSql 中；
                ZKMybatisSqlHelper.validForeachParams(this.sourceBountSql, this.newBountSql);
            }
            catch(IllegalAccessException e) {
                throw ZKExceptionsUtils.unchecked(e);
            }
        }
        return this.newBountSql;
    }

}
