/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKThirdPartyDao.java 
* @author Vinson 
* @Package com.zk.wechat.thirdParty.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 10:40:25 AM 
* @version V1.0 
*/
package com.zk.wechat.thirdParty.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;

/** 
* @ClassName: ZKThirdPartyDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKThirdPartyDao extends ZKBaseDao<String, ZKThirdParty> {

    /**
     * 更新第三方平台令牌
     *
     * @Title: updateTicket
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 18, 2021 10:13:00 AM
     * @param appId
     * @param wxTicket
     * @param updateDate
     * @return
     * @return int
     */
    @Update("UPDATE t_wx_third_party SET c_wx_ticket = #{wxTicket}, c_wx_ticket_update_date = #{updateDate} where c_pk_id = #{appId} ")
    int updateTicket(@Param("appId")String appId, @Param("wxTicket")String wxTicket, @Param("updateDate")Date updateDate);

    /**
     * 更新第三方平台权限 token
     *
     * @Title: updateAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 18, 2021 10:13:14 AM
     * @param appId
     * @param wxAccessToken
     * @param updateDate
     * @return
     * @return int
     */
    @Update("UPDATE t_wx_third_party SET c_wx_access_token = #{wxAccessToken}, c_wx_access_token_expires_in = #{expiresIn}, c_wx_access_token_update_date = #{updateDate} where c_pk_id = #{appId} ")
    int updateAccessToken(@Param("appId")String appId, @Param("wxAccessToken") String wxAccessToken, 
            @Param("expiresIn")
            Integer expiresIn, @Param("updateDate")
            Date updateDate);

}