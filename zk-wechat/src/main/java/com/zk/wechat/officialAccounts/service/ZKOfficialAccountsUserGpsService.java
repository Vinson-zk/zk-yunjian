/**
 * 
 */
package com.zk.wechat.officialAccounts.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.officialAccounts.dao.ZKOfficialAccountsUserGpsDao;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUserGps;

/**
 * ZKOfficialAccountsUserGpsService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKOfficialAccountsUserGpsService extends ZKBaseService<String, ZKOfficialAccountsUserGps, ZKOfficialAccountsUserGpsDao> {

	
	
}