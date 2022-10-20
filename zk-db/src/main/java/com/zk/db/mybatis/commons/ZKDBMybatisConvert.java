/** 
 e* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKDBMybatisConvert.java 
* @author Vinson 
* @Package com.zk.db.mybatis.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 15, 2021 10:45:58 AM 
* @version V1.0 
*/
package com.zk.db.mybatis.commons;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zk.db.annotation.ZKColumn;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;

/** 
* @ClassName: ZKDBMybatisConvert 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKDBMybatisConvert implements ZKSqlConvert {

    /********************************************************/
    /*** 这个抽象类中，只解析类上的注解；实际根据注解生成 sql 在不同的数据库对应的方言中实现 **/
    /********************************************************/

    // 根据注解 解析查询的条件; 会添加 <trim prefixOverrides="and|or"> 包裹
    @Override
    public ZKDBQueryWhere resolveQueryCondition(ZKDBMapInfo mapInfo) {
        return resolveQueryCondition(mapInfo, null);
    }

    @Override
    public ZKDBQueryWhere resolveQueryCondition(ZKDBMapInfo mapInfo, List<String>filterAttrNames) {
        ZKDBQueryWhere where = ZKDBQueryWhere.asAnd();
        where.setPrefix(ZKDBScriptKey.trimAndOr[0]);
        where.setSuffix(ZKDBScriptKey.trimAndOr[1]);
        if(mapInfo.isEmpty()){
            return where;
        }
        Iterator<Map.Entry<PropertyDescriptor, ZKColumn>> iterator = mapInfo.getColumnsIterator();
        Map.Entry<PropertyDescriptor, ZKColumn> item = null;
        while (iterator.hasNext()){
            item = iterator.next();
            if(item.getValue().query().value()){
                if(filterAttrNames == null || !filterAttrNames.contains(item.getKey().getName())){
                    if(item.getValue().query().isForce()){
                        where.put(asQueryCol(item.getKey().getName(), item.getValue()));
                    }else{
                        where.put(asQueryScript(item.getKey().getName(), item.getValue()));
                    }
                }

            }
        }
        return where;
    }

    // 将 @ZKColumn 注解转换为 强制查询条件 ZKDBQueryCol
    protected ZKDBQueryCol asQueryCol(String attrName, ZKColumn col){
        return ZKDBQueryCol.as(col.query().queryType(), col.name(), attrName, col.javaType(), col.formats(),
            col.query().isCaseSensitive());
    }

    // 将 @ZKColumn 注解转换为 包裹 <if> 的查询条件 ZKDBQueryColScript
    protected ZKDBQueryScript asQueryScript(String attrName, ZKColumn col){
        return ZKDBQueryScript.asIf(asQueryCol(attrName, col), col.query().testRule(), attrName, col.javaType());
    }

}
