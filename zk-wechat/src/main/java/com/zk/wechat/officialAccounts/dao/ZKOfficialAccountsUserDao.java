/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.officialAccounts.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUser;

/**
 * ZKOfficialAccountsUserDAO接口
 * 
 * @author
 * @version 1.0
 */
@ZKMyBatisDao
public interface ZKOfficialAccountsUserDao extends ZKBaseDao<String, ZKOfficialAccountsUser> {

    // 根据 第三方平台APPID和授权方APPID、openId，取唯一授权用户实体
    @Select("SELECT ${sCols} FROM ${tn} ${ta} WHERE ${ta}.c_wx_third_party_appid = #{thirdPartyAppId} "
            + "and ${ta}.c_wx_official_account_appid = #{accountAppId} and ${ta}.c_wx_openid = #{openid}")
    ZKOfficialAccountsUser getByOpenId(@Param("tn") String tn, @Param("ta") String ta,
            @Param("sCols") String sCols, @Param("thirdPartyAppId") String thirdPartyAppId,
            @Param("accountAppId") String accountAppId, @Param("openid") String openid);
	
}