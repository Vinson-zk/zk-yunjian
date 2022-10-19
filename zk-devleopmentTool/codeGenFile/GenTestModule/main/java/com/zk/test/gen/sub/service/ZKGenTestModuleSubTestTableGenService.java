/**
 * 
 */
package com.zk.test.gen.sub.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.test.gen.sub.entity.ZKGenTestModuleSubTestTableGen;
import com.zk.test.gen.sub.dao.ZKGenTestModuleSubTestTableGenDao;

/**
 * ZKGenTestModuleSubTestTableGenService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKGenTestModuleSubTestTableGenService extends ZKBaseService<Date, ZKGenTestModuleSubTestTableGen, ZKGenTestModuleSubTestTableGenDao> {

	
	
}