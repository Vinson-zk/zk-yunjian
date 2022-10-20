/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.mail.dao;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.mail.entity.ZKMailSendHistory;

/**
 * ZKMailSendHistoryDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKMailSendHistoryDao extends ZKBaseDao<String, ZKMailSendHistory> {
	
}