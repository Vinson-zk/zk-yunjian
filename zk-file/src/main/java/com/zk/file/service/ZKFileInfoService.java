/**
 * 
 */
package com.zk.file.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.file.entity.ZKFileInfo;
import com.zk.file.dao.ZKFileInfoDao;

/**
 * ZKFileInfoService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKFileInfoService extends ZKBaseService<String, ZKFileInfo, ZKFileInfoDao> {

	
	
}