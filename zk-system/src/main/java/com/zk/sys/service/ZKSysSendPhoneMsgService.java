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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKBusinessException;

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

    @Value("${zk.sys.auth.captcha.phone.interval.time:60000}")
    protected long mailIntervalTime;

    @Autowired
    ZKCacheManager<String> zkCacheManager;

    protected static final String captchaIntervalTimeCache = "_captcha.phone.interval.time";

    // 取发送手机验证码 所需要的间隔时间还剩多少 秒
    protected int getIntervalTime(String phoneNum) {
        ZKCache<String, Long> cache = zkCacheManager.getCache(captchaIntervalTimeCache);
        Long time = cache.get(phoneNum);
        if (time == null) {
            return -1;
        }
        return (int) ((this.mailIntervalTime - System.currentTimeMillis() + time) / 1000);

    }

    protected void setIntervalTime(String phoneNum) {
        ZKCache<String, Long> cache = zkCacheManager.getCache(captchaIntervalTimeCache);
        cache.put(phoneNum, System.currentTimeMillis());
    }

    /**
     * 是否发送过于频繁
     *
     * @Title: isToFrequent
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 11, 2024 5:53:03 PM
     * @param recipientMailAddr
     * @return
     * @return boolean
     */
    public boolean isToFrequent(String phoneNum) {
        int intervalTime = this.getIntervalTime(phoneNum);
        if (intervalTime > 0) {
            log.error("[>_<:20240911-0103-001] zk.sys.010032=手机验证码发送过于频繁，请稍后再试: {}", phoneNum);
            throw ZKBusinessException.as("zk.sys.010032", intervalTime);
        }
        return true;
    }

    public ZKMsgRes sendPhoneCode(String codeType, String phoneNum, String verifyCode) {

        this.setIntervalTime(phoneNum);

        log.error("-------- 还未实现");
        return null;
    }

}
