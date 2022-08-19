/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.thirdParty.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.thirdParty.entity.ZKThirdPartyAuthAccount;

/**
 * ZKThirdPartyAuthAccountDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKThirdPartyAuthAccountDao extends ZKBaseDao<String, ZKThirdPartyAuthAccount> {

    @Select(value = {
            "SELECT ${sCols} FROM ${tn} WHERE c_wx_third_party_appid = #{thirdPartyAppid} and c_wx_authorizer_appid = #{authorizerAppid}" })
    ZKThirdPartyAuthAccount getByThirdPartyAppidAndAuthorizerAppid(@Param("tn") String tn, @Param("sCols") String sCols,
            @Param("thirdPartyAppid") String thirdPartyAppid, @Param("authorizerAppid") String authorizerAppid);
	
}