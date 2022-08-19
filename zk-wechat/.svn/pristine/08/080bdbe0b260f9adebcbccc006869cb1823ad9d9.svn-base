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
* @Title: ZKThirdPartyService.java 
* @author Vinson 
* @Package com.zk.wechat.thirdParty.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 10:40:17 AM 
* @version V1.0 
*/
package com.zk.wechat.thirdParty.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.thirdParty.dao.ZKThirdPartyDao;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;

/** 
* @ClassName: ZKThirdPartyService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKThirdPartyService extends ZKBaseService<String, ZKThirdParty, ZKThirdPartyDao> {

    public ZKThirdParty get(String thirdPartyAppId) {
        return this.dao.get(new ZKThirdParty(thirdPartyAppId));
    }

    @Transactional(readOnly = false)
    public int updateTicket(String appId, String wxTicket) {
        return this.dao.updateTicket(appId, wxTicket, new Date());
    }

    @Transactional(readOnly = false)
    public int updateAccessToken(String appId, String wxAccessToken, Integer expiresIn) {
        return this.dao.updateAccessToken(appId, wxAccessToken, expiresIn, new Date());
    }

}
