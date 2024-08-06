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
* @Title: ZKPayGetOrderHistoryService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 9:51:36 AM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.pay.dao.ZKPayGetOrderHistoryDao;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.entity.ZKPayGetOrderHistory;

/** 
* @ClassName: ZKPayGetOrderHistoryService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKPayGetOrderHistoryService extends ZKBaseService<String, ZKPayGetOrderHistory, ZKPayGetOrderHistoryDao> {

    /**
     *
     * @Title: save
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 22, 2021 9:59:51 AM
     * @param entity
     * @return void
     */
    @Transactional(readOnly = false)
    public void save(ZKPayGetOrder entity) {
        this.save(ZKPayGetOrderHistory.as(entity));
    }

}
