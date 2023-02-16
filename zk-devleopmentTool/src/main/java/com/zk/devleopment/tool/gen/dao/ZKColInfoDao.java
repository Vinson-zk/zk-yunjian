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
* @Title: ZKColInfoDao.java 
* @author Vinson 
* @Package com.zk.code.generate.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 31, 2021 11:58:15 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.dao;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;

/** 
* @ClassName: ZKColInfoDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKColInfoDao extends ZKBaseDao<String, ZKColInfo> {

}
