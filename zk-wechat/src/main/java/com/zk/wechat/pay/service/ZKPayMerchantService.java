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
* @Title: ZKPayMerchantService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 5:45:07 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.pay.dao.ZKPayMerchantDao;
import com.zk.wechat.pay.entity.ZKPayMerchant;

/**
 * @ClassName: ZKPayMerchantService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ZKPayMerchantService extends ZKBaseService<String, ZKPayMerchant, ZKPayMerchantDao> {

    @Transactional(readOnly = false)
    public int updateTicket(String mchid, String apiv3Aeskey) {
        return this.dao.updateApiv3Aeskey(mchid, apiv3Aeskey, new Date());
    }

    @Transactional(readOnly = false)
    public int updateCertMyPrivatePath(String mchid, String certMyPrivatePath) {
        return this.dao.updateCertMyPrivatePath(mchid, certMyPrivatePath, new Date());
    }

}
