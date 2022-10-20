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
* @Title: ZKSecDefaultAuthcDevToken.java 
* @author Vinson 
* @Package com.zk.security.token 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 9:19:09 AM 
* @version V1.0 
*/
package com.zk.security.token;

/**
 * @ClassName: ZKSecDefaultAuthcDevToken
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecDefaultAuthcDevToken extends ZKSecAbstractAuthenticationToken implements ZKSecAuthcDevToken {

    /**
     * 
     */
    private static final long serialVersionUID = 5513927269968075424L;

    // 应用ID
    private String devId;

    // 应用 secret
    private char[] secretKey;
    
    // 第三方标识
    private String thirdPartyId;

    /**
     * token 生成加密字符 返回应用
     * @param groupCode
     * @param appId
     * @param secretKey
     */
    public ZKSecDefaultAuthcDevToken(String companyCode, String devId, char[] secretKey, long osType, String udid,
            long appType, String appId) {
        super(companyCode, osType, udid, appType, appId);
        this.secretKey = secretKey;
        this.devId = devId;
    }

    /**
     * @return devId sa
     */
    public String getDevId() {
        return devId;
    }

    /**
     * 应用 secret
     */
    @Override
    public char[] getSecretKey() {
        return secretKey;
    }
    
    /**
     * @return thirdPartyId sa
     */
    public String getThirdPartyId() {
        return thirdPartyId;
    }



}
