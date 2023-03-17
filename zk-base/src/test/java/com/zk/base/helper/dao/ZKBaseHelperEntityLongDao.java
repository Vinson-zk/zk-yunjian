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
* @Title: ZKBaseHelperEntityLongDao.java 
* @author Vinson 
* @Package com.zk.base.helper.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 2, 2023 7:30:03 PM 
* @version V1.0 
*/
package com.zk.base.helper.dao;

import com.zk.base.dao.ZKBaseDao;
import com.zk.base.helper.entity.ZKBaseHelperEntityLong;
import com.zk.db.annotation.ZKMyBatisDao;

/** 
* @ClassName: ZKBaseHelperEntityLongDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKBaseHelperEntityLongDao extends ZKBaseDao<Long, ZKBaseHelperEntityLong> {

}
