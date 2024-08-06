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
* @Title: ZKMyBatisTreeSqlProvider.java 
* @author Vinson 
* @Package com.zk.base.myBaits.provider 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 28, 2021 10:29:50 AM 
* @version V1.0 
*/
package com.zk.base.myBaits.provider;

import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.mybatis.commons.ZKDBScriptKey;
import com.zk.db.mybatis.provider.ZKDBMybatisSqlProvider;

/** 
* @ClassName: ZKMyBatisTreeSqlProvider
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMyBatisTreeSqlProvider {

    public String selectTree(ZKBaseTreeEntity<?, ?> entity) {
        StringBuffer sb = new StringBuffer();
        sb.append(ZKDBScriptKey.Script[0]);
        sb.append(ZKSqlConvert.SqlKeyword.select);
        sb.append(entity.getSqlHelper().getBlockSqlCols());
        sb.append(ZKSqlConvert.SqlKeyword.from);
        sb.append(entity.getSqlHelper().getTableName());
        sb.append(ZKSqlConvert.SqlKeyword.space);
        sb.append(entity.getSqlHelper().getTableAlias());
        sb.append(ZKSqlConvert.SqlKeyword.space);
        sb.append(ZKDBScriptKey.where[0]);
        sb.append(entity.getTreeSqlHelper().getBlockSqlWhereTree());
        sb.append(ZKDBScriptKey.where[1]);
        ZKDBMybatisSqlProvider.appendOrderBySql(entity, sb);
        sb.append(ZKDBScriptKey.Script[1]);
        return sb.toString();
    }

//    public String selectTreeFilter(ZKBaseTreeEntity<?, ?> entity) {
//        StringBuffer sb = new StringBuffer();
//        sb.append(ZKDBScriptKey.Script[0]);
//        sb.append(ZKSqlConvert.SqlKeyword.select);
//        sb.append(entity.getSqlHelper().getBlockSqlCols());
//        sb.append(ZKSqlConvert.SqlKeyword.from);
//        sb.append(entity.getSqlHelper().getTableName());
//        sb.append(ZKSqlConvert.SqlKeyword.space);
//        sb.append(entity.getSqlHelper().getTableAlias());
//        sb.append(ZKSqlConvert.SqlKeyword.space);
//        sb.append(ZKDBScriptKey.where[0]);
//        sb.append(entity.getTreeSqlHelper().getBlockSqlWhereTreeFilter());
//        sb.append(ZKDBScriptKey.where[1]);
//        ZKDBMybatisSqlProvider.appendOrderBySql(entity, sb);
//        sb.append(ZKDBScriptKey.Script[1]);
//        return sb.toString();
//    }

    public String selectTreeDetail(ZKBaseTreeEntity<?, ?> entity) {
        StringBuffer sb = new StringBuffer();
        sb.append(ZKDBScriptKey.Script[0]);
        sb.append(ZKSqlConvert.SqlKeyword.select);
        sb.append(entity.getSqlHelper().getBlockSqlCols());
        sb.append(ZKSqlConvert.SqlKeyword.from);
        sb.append(entity.getSqlHelper().getTableName());
        sb.append(ZKSqlConvert.SqlKeyword.space);
        sb.append(entity.getSqlHelper().getTableAlias());
        sb.append(ZKSqlConvert.SqlKeyword.space);
        sb.append(ZKDBScriptKey.where[0]);
        sb.append(entity.getTreeSqlHelper().getBlockSqlPkWhere());
        sb.append(ZKDBScriptKey.where[1]);
        sb.append(ZKDBScriptKey.Script[1]);
        return sb.toString();
    }



}
