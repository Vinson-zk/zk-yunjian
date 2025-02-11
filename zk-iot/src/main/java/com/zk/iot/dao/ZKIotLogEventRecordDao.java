/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.dao;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.iot.entity.ZKIotLogEventRecord;

/**
 * ZKIotLogEventRecordDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKIotLogEventRecordDao extends ZKBaseDao<String, ZKIotLogEventRecord> {
	
}