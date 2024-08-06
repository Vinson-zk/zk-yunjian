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
* @Title: ZKSysNavDao.java 
* @author Vinson 
* @Package com.zk.sys.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2020 1:49:46 PM 
* @version V1.0 
*/
package com.zk.sys.res.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.res.entity.ZKSysNav;

/**
 * @ClassName: ZKSysNavDao
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKMyBatisDao
public interface ZKSysNavDao extends ZKBaseDao<String, ZKSysNav> {

    /**
     * 按 navCode 查询, navCode 需要做唯一键
     *
     * @Title: getByNavCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2020 1:56:15 PM
     * @param navCode
     * @return ZKSysNav
     */
//    ZKSysNav getByNavCode(@Param("navCode") String navCode);

    @Select(value = { "SELECT ${sCols} FROM ${tn} WHERE c_code = #{navCode}" })
    ZKSysNav getByNavCode(@Param("tn") String tn, @Param("sCols") String sCols,
            @Param("navCode") String navCode);

}
