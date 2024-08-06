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
* @Title: ZKPayGroupDao.java 
* @author Vinson 
* @Package com.zk.wechat.pay.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 4:46:43 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.pay.entity.ZKPayGroup;

/** 
* @ClassName: ZKPayGroupDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKPayGroupDao extends ZKBaseDao<String, ZKPayGroup> {
    
    @Select(value = { "SELECT ${sCols} FROM ${tn} WHERE c_code = #{code}" })
    ZKPayGroup getByCode(@Param("tn") String tn, @Param("sCols") String sCols,
            @Param("code") String code);



}
