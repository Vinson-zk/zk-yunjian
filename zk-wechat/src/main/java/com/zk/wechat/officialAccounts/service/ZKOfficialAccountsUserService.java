/**
 * 
 */
package com.zk.wechat.officialAccounts.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.officialAccounts.dao.ZKOfficialAccountsUserDao;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUser;

/**
 * ZKOfficialAccountsUserService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKOfficialAccountsUserService extends ZKBaseService<String, ZKOfficialAccountsUser, ZKOfficialAccountsUserDao> {

	
    /**
     * 根据 第三方平台APPID和授权方APPID、openId，取唯一授权用户实体
     *
     * @Title: getByOpenId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 20, 2022 12:10:27 AM
     * @param thirdPartyAppId
     * @param accountAppId
     * @param openid
     * @return
     * @return ZKOfficialAccountsUser
     */
    public ZKOfficialAccountsUser getByOpenId(String thirdPartyAppId, String accountAppId, String openid) {
        return this.dao.getByOpenId(ZKOfficialAccountsUser.sqlHelper().getTableName(),
                ZKOfficialAccountsUser.sqlHelper().getTableAlias(),
                ZKOfficialAccountsUser.sqlHelper().getBlockSqlCols(), thirdPartyAppId, accountAppId, openid);
    }
	
}