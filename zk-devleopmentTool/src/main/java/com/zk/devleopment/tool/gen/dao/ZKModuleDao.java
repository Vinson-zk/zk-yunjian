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
* @Title: ZKModuleDao.java 
* @author Vinson 
* @Package com.zk.code.generate.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 30, 2021 10:46:06 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.devleopment.tool.gen.entity.ZKModule;

/** 
* @ClassName: ZKModuleDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKModuleDao extends ZKBaseDao<String, ZKModule> {

    @Select({ "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE c_module_name = #{moduleName} " })
    ZKModule getByModuleName(@Param("moduleName") String moduleName);

}
