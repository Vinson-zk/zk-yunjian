/**
 * 
 */
package com.zk.file.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.file.entity.ZKFileDirectoryOptLog;
import com.zk.file.dao.ZKFileDirectoryOptLogDao;

/**
 * ZKFileDirectoryOptLogService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKFileDirectoryOptLogService extends ZKBaseService<String, ZKFileDirectoryOptLog, ZKFileDirectoryOptLogDao> {

	
	
}