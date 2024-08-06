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
* @Title: ZKDBMybatisSqlProvider.java 
* @author Vinson 
* @Package com.zk.db.mybatis.provider 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 16, 2020 10:43:25 AM 
* @version V1.0 
*/
package com.zk.db.mybatis.provider;

import com.zk.core.utils.ZKStringUtils;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.entity.ZKDBEntity;
import com.zk.db.mybatis.commons.ZKDBScriptKey;

/** 
* @ClassName: ZKDBMybatisSqlProvider 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBMybatisSqlProvider {

    public String insert(ZKDBEntity<?> entity) {
        return entity.getSqlHelper().getSqlInsert();
    }

    public String update(ZKDBEntity<?> entity) {
        return entity.getSqlHelper().getSqlUpdate();
    }

    public String del(ZKDBEntity<?> entity) {
        return entity.getSqlHelper().getSqlDel();
    }

    public String diskDel(ZKDBEntity<?> entity) {
        return entity.getSqlHelper().getSqlDiskDel();
    }

    public String get(ZKDBEntity<?> entity) {
        return entity.getSqlHelper().getSqlGet();
    }

    public String selectList(ZKDBEntity<?> entity) {

//        System.out.println("======== " + ZKJsonUtils.toJsonStr(entity));
//        return "<script>SELECT t0.c_json AS \"json\", t0.c_id AS \"id\", t0.c_type AS \"type\", t0.c_value AS \"value\", t0.c_remarks AS \"remarks\" FROM t_test t0 <where><if test=' type != null '>AND t0.c_type = #{type}</if></where> </script>";
//        return "SELECT t0.c_json AS \"json\", t0.c_id AS \"id\", t0.c_type AS \"type\", t0.c_value AS \"value\", t0.c_remarks AS \"remarks\" FROM t_test t0 WHERE t0.c_type = #{type}";

        StringBuffer sb = new StringBuffer();
        sb.append(ZKDBScriptKey.Script[0]);
        sb.append(entity.getSqlHelper().getBlockSqlSelelctList());
        ZKDBMybatisSqlProvider.appendOrderBySql(entity, sb);
        sb.append(ZKDBScriptKey.Script[1]);
        return sb.toString();
    }

    public static void appendOrderBySql(ZKDBEntity<?> entity, StringBuffer sb) {
        if (entity.getPage() == null || entity.getPage().getSorters() == null) {
            if(!ZKStringUtils.isEmpty(entity.getSqlHelper().getBlockSqlOrderBy())){
                sb.append(ZKSqlConvert.SqlKeyword.orderBy);
                sb.append(entity.getSqlHelper().getBlockSqlOrderBy());
            }
        }
        else {
            sb.append(ZKSqlConvert.SqlKeyword.orderBy);
            sb.append(entity.getSqlHelper().getBlockSqlOrderBy(entity.getPage().getSorters()));
        }
    }

}
