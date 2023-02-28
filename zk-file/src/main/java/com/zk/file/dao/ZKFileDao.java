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
* @Title: ZKFileDao.java 
* @author Vinson 
* @Package com.zk.file.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 5:04:35 PM 
* @version V1.0 
*/
package com.zk.file.dao;

import com.zk.base.dao.ZKBaseDao;
import com.zk.file.dto.ZKFileDto;
import com.zk.file.entity.ZKFile;

/** 
* @ClassName: ZKFileDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKFileDao extends ZKBaseDao<String, ZKFileDto<ID, ZKFile>> {

}
