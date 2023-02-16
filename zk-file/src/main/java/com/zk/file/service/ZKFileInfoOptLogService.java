/**
 * 
 */
package com.zk.file.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.file.entity.ZKFileInfoOptLog;
import com.zk.file.dao.ZKFileInfoOptLogDao;

/**
 * ZKFileInfoOptLogService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKFileInfoOptLogService extends ZKBaseService<String, ZKFileInfoOptLog, ZKFileInfoOptLogDao> {

	
	
}