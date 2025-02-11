/**
 * 
 */
package com.zk.iot.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.iot.dao.ZKIotLogFuncRecordDao;
import com.zk.iot.entity.ZKIotLogFuncRecord;

/**
 * ZKIotLogFuncRecordService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotLogFuncRecordService extends ZKBaseService<String, ZKIotLogFuncRecord, ZKIotLogFuncRecordDao> {

	
	
}