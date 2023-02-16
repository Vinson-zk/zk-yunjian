/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.dao;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.file.entity.ZKFileInfo;

/**
 * ZKFileInfoDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKFileInfoDao extends ZKBaseDao<String, ZKFileInfo> {
	
}