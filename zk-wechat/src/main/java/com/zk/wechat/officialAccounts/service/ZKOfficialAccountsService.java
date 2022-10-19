/**
 * 
 */
package com.zk.wechat.officialAccounts.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.officialAccounts.dao.ZKOfficialAccountsDao;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccounts;

/**
 * ZKOfficialAccountsService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKOfficialAccountsService extends ZKBaseService<String, ZKOfficialAccounts, ZKOfficialAccountsDao> {

    /**
     * 根据 第三方平台APPID和授权方APPID，取唯一授权方实体
     *
     * @Title: getByAccountAppid
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 19, 2022 6:50:51 PM
     * @param thirdPartyAppId
     *            第三方平台ID
     * @param accountAppid
     *            授权方APPID
     * @return
     * @return ZKOfficialAccounts
     */
    public ZKOfficialAccounts getByAccountAppid(String thirdPartyAppId, String accountAppid) {
        return this.dao.getByAccountAppid(ZKOfficialAccounts.sqlHelper().getTableName(),
                ZKOfficialAccounts.sqlHelper().getTableAlias(),
                ZKOfficialAccounts.sqlHelper().getBlockSqlCols(), thirdPartyAppId, accountAppid);
    }
	
}