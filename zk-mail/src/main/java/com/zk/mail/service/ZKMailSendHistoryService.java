/**
 * 
 */
package com.zk.mail.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.mail.entity.ZKMailSendHistory;
import com.zk.mail.dao.ZKMailSendHistoryDao;

/**
 * ZKMailSendHistoryService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKMailSendHistoryService extends ZKBaseService<String, ZKMailSendHistory, ZKMailSendHistoryDao> {

	
	
}