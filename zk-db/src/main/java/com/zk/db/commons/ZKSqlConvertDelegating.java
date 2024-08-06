/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSqlConvertDelegating.java 
* @author Vinson 
* @Package com.zk.db.annotation 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 11, 2020 9:58:16 AM 
* @version V1.0 
*/
package com.zk.db.commons;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.commons.data.ZKOrder;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.mybatis.ZKMybatisSqlHelper.envKey;
import com.zk.db.mybatis.mysql.ZKDBMysqlSqlConvert;

/** 
* @ClassName: ZKSqlConvertDelegating 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSqlConvertDelegating implements ZKSqlConvert {

    protected Logger log = LogManager.getLogger(getClass());

    private ZKSqlConvert sqlConvert;

    public ZKSqlConvertDelegating() {
        this(null);
    }

    public ZKSqlConvertDelegating(String dbType) {
        
        if(ZKEnvironmentUtils.isInit()) {
            if(ZKEnvironmentUtils.getApplicationContext().containsBean("sqlConvert")) {
                sqlConvert = ZKEnvironmentUtils.getApplicationContext().getBean("sqlConvert", ZKSqlConvert.class);
            }
            else {
                if (ZKStringUtils.isEmpty(dbType)) {

                }
            }
        }
        if (sqlConvert == null) {
            if (ZKStringUtils.isEmpty(dbType)) {
                dbType = ZKEnvironmentUtils.getString(envKey.jdbcType, ZKDBConstants.defaultJdbcType);
            }

            if ("mysql".equals(dbType)) {
                sqlConvert = new ZKDBMysqlSqlConvert();
            }
        }

        if (sqlConvert == null) {
            log.error("[>_<:20200911-0915-001] sqlConvert 初始化失败，不支持的数据库：{};", dbType);
        }
    }

    protected static Logger logger = LogManager.getLogger(ZKSqlConvertDelegating.class);

    /********************************************************/
    /*** 通过注解直接转换成 sql **/
    /********************************************************/
    @Override
    public String convertSqlInsert(ZKDBMapInfo mapInfo){
        return this.sqlConvert.convertSqlInsert(mapInfo);
    }

    @Override
    public String convertSqlUpdate(ZKDBMapInfo mapInfo){
        return this.sqlConvert.convertSqlUpdate(mapInfo);
    }

    @Override
    public String convertSqlDel(ZKDBMapInfo mapInfo, String delSetSql){
        return this.sqlConvert.convertSqlDel(mapInfo, delSetSql);
    }

    @Override
    public String convertSqlDiskDel(ZKDBMapInfo mapInfo){
        return this.sqlConvert.convertSqlDiskDel(mapInfo);
    }

    @Override
    public String convertSqlSelCols(ZKDBMapInfo mapInfo, String tableAlias, String columnPrefix) {
        return this.sqlConvert.convertSqlSelCols(mapInfo, tableAlias, columnPrefix);
    }

    @Override
    public String convertPkCondition(ZKDBMapInfo mapInfo, String tableAlias){
        return this.sqlConvert.convertPkCondition(mapInfo, tableAlias);
    }


    /********************************************************/
    /*** 动态 转换 **/
    /********************************************************/

    @Override
    public void convertQueryCondition(StringBuffer sb, ZKDBQueryCol queryCol, String tableAlias){
        this.sqlConvert.convertQueryCondition(sb, queryCol, tableAlias);
    }

    @Override
    public void convertQueryCondition(ZKDBOptLogic queryLogic, StringBuffer sb, ZKDBQueryCol queryCol,
                                      String tableAlias){
        this.sqlConvert.convertQueryCondition(queryLogic, sb, queryCol, tableAlias);
    }

    @Override
    public String convertSqlOrderBy(ZKDBMapInfo mapInfo, Collection<ZKOrder> sorts, String tableAlias,
        boolean isDefault) {
        return this.sqlConvert.convertSqlOrderBy(mapInfo, sorts, tableAlias, isDefault);
    }

    /********************************************************/
    /*** 解析类上的注解 **/
    /********************************************************/
    @Override
    public ZKDBQueryWhere resolveQueryCondition(ZKDBMapInfo mapInfo) {
        return this.sqlConvert.resolveQueryCondition(mapInfo);
    }

    @Override
    public ZKDBQueryWhere resolveQueryCondition(ZKDBMapInfo mapInfo, List<String> filterAttrNames){
        return this.sqlConvert.resolveQueryCondition(mapInfo, filterAttrNames);
    }
    

}
