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
* @Title: ZKSysSendMailMsgService.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2024 11:38:09 PM 
* @version V1.0 
*/
package com.zk.sys.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.mail.api.ZKMailSendApi;
import com.zk.sys.utils.ZKSysUtils;

/**
 * 发送邮件信息
 * 
 * @ClassName: ZKSysSendMailMsgService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
//@Transactional(readOnly = true)
public class ZKSysSendMailMsgService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKMailSendApi mailSendApi;

    /**
     * mail.verifiy.code 发送邮件验证码
     * 
     * @Title: sendMailCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 6, 2024 3:11:41 PM
     * @param codeType
     * @param userName
     * @param verifiyCode
     * @param recipientMailAddr
     * @return
     * @return ZKMsgRes
     */
    public ZKMsgRes sendMailCode(String codeType, String userName, String verifiyCode, String recipientMailAddr) {
        // userName\verifiyCode\timeStr\codeType

        // 记录和处理发送的频率

        String timeStr = ZKDateUtils.formatDate(ZKDateUtils.getToday(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss);
        Map<String, Object> mailParams = new HashMap<>();
        mailParams.put("timeStr", timeStr);
        mailParams.put("userName", ZKStringUtils.isEmpty(userName) ? "" : userName);
        mailParams.put("verifiyCode", verifiyCode);
        mailParams.put("codeType", ZKMsgUtils.getMessage("zk.sys.msg.code.type.mail." + codeType));

        return mailSendApi.mailSendByTypeCode(ZKSysUtils.getMailVerifyCode(), null, null, recipientMailAddr, null, null,
                mailParams);
    }

}
