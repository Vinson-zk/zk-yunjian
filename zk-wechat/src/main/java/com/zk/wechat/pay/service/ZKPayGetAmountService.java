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
* @Title: ZKPayGetAmountService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 11:55:41 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.pay.dao.ZKPayGetAmountDao;
import com.zk.wechat.pay.entity.ZKPayGetAmount;

/** 
* @ClassName: ZKPayGetAmountService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKPayGetAmountService extends ZKBaseService<String, ZKPayGetAmount, ZKPayGetAmountDao> {

    public ZKPayGetAmount getByPayOrderPkId(String payOrderPkId) {
        return this.dao.getByPayOrderPkId(ZKPayGetAmount.sqlHelper().getTableName(),
                ZKPayGetAmount.sqlHelper().getBlockSqlCols(""), payOrderPkId);
    }

}
