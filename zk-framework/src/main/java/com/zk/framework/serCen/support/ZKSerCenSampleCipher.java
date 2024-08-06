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
* @Title: ZKSerCenSampleCipher.java 
* @author Vinson 
* @Package com.zk.framework.serCen.support 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 12, 2020 9:01:49 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.serCen.ZKSerCenDecode;
import com.zk.framework.serCen.ZKSerCenEncrypt;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKSerCenSampleCipher 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenSampleCipher implements ZKSerCenDecode, ZKSerCenEncrypt {

    protected Logger log = LogManager.getLogger(getClass());

    public static interface Key {

        public static final String serverName = "serverName";
//
//        public static final String ip = "ip";
//
        public static final String date = "dateTime";

        public static final String signature = "_Cipher_Signature_";

    }

    @Override
    public String getSignature() {
        return Key.signature;
    }

    @Value("${spring.application.name:\"\"}")
    private String serverName;

    public String getServerName() {
//        System.out.println("[^_^:20200820-1558-001] spring.application.name: " + serverName);
        return serverName == null ? "" : serverName.toUpperCase();
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 加密
     */
    public Map<String, String> encrypt() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(Key.serverName, getServerName());
//        jsonObj.put(Key.ip, ip);
        jsonObj.put(Key.date, new Date());

        String str = jsonObj.toJSONString();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Key.serverName, getServerName());
        headerMap.put(getSignature(), ZKEncodingUtils.encodeBase64ToStr(str));

        return headerMap;
    }

    /**
     * 解密
     * <p>
     * Title: decode
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param encryptStr
     * @return
     * @see com.zk.framework.serCen.ZKSerCenCipher#decode(java.lang.String)
     */
    public Map<String, Object> decode(String encryptStr) {
        String str = ZKEncodingUtils.decodeBase64ToStr(encryptStr);
        JSONObject jsonObj = JSON.parseObject(str);

        Map<String, Object> decMap = new HashMap<String, Object>();
        decMap.put(Key.serverName, jsonObj.getString(Key.serverName));
//        decMap.put(Key.ip, jsonObj.getString(Key.ip));
        decMap.put(Key.date, jsonObj.getDate(Key.date));

        return decMap;
    }

    public String getEncryptStr(HttpServletRequest hReq) {
        return hReq.getHeader(this.getSignature());
    }

    @Override
    public boolean assertServerClient(HttpServletRequest hReq) {

//        System.out.println("[^_^:20200312-2110-001] ZKSerCenRegisterFilter httpReq -> uri: " + hReq.getRequestURI());

        String encStr = this.getEncryptStr(hReq);
        log.info("[^_^:20200701-1536-001] encStr: {}", encStr);
        
        Map<String, Object> decMap = this.decode(encStr);
        log.info("[^_^:20200701-1536-002] decMap: {}", ZKJsonUtils.toJsonStr(decMap));

        String serverName = hReq.getHeader(Key.serverName);
        log.info("[^_^:20200701-1536-003] serverName: {}", serverName);

        String decServerName = (String) decMap.get(Key.serverName);
        return ZKStringUtils.equals(serverName, decServerName);
//        return false;
    }

}
