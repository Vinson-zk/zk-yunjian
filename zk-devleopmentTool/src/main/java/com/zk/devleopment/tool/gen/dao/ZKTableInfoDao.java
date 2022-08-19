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
* @Title: ZKTableInfoDao.java 
* @author Vinson 
* @Package com.zk.code.generate.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 31, 2021 8:35:09 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/**
 * @ClassName: ZKTableInfoDao
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKMyBatisDao
public interface ZKTableInfoDao extends ZKBaseDao<String, ZKTableInfo> {

    @Select(value = { "SELECT ${sCols} FROM ${tn} WHERE c_module_id = #{moduleId} and c_table_name = #{tableName}" })
    ZKTableInfo getByTableName(@Param("tn") String tn, @Param("sCols") String sCols, @Param("moduleId") String moduleId,
            @Param("tableName") String tableName);

}
