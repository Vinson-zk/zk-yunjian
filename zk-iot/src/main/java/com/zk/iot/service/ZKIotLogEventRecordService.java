/**
 * 
 */
package com.zk.iot.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.iot.dao.ZKIotLogEventRecordDao;
import com.zk.iot.entity.ZKIotLogEventRecord;

/**
 * ZKIotLogEventRecordService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotLogEventRecordService extends ZKBaseService<String, ZKIotLogEventRecord, ZKIotLogEventRecordDao> {

	
	
}