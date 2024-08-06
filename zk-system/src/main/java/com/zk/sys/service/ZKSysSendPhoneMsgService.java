/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysSendPhoneMsgService.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 6, 2024 3:12:37 PM 
* @version V1.0 
*/
package com.zk.sys.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zk.core.commons.ZKMsgRes;

/** 
* @ClassName: ZKSysSendPhoneMsgService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKSysSendPhoneMsgService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    /**
     * 
     * 
     */

    public ZKMsgRes sendPhoneCode(String codeType, String phoneNum, String verifiyCode) {

        log.error("-------- 还未实现");
        return null;
    }

}
