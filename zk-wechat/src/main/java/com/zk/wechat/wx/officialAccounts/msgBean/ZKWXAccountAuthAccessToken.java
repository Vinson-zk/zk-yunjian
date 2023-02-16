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
* @Title: ZKWXAccountAuthAccessToken.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.msgBean 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 5, 2021 8:24:35 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.msgBean;

import com.zk.wechat.wx.common.ZKWXAbstractAccessToken;
import com.zk.wechat.wx.common.ZKWXConstants;

/**
 * 目标授权账号的 AccessToken
 * 
 * @ClassName: ZKWXAccountAuthAccessToken
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKWXAccountAuthAccessToken extends ZKWXAbstractAccessToken {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;
    
    public ZKWXAccountAuthAccessToken(String thirdPartyAppid, String accountAppid) {
        this.identification = ZKWXAccountAuthAccessToken.makeIdentification(thirdPartyAppid, accountAppid);
        this.thirdPartyAppid = thirdPartyAppid;
        this.accountAppid = accountAppid;
    }

    /**
     * token 标识；[第三方平台 APPID][分隔符ZKWXConstants.cacheKeySeparator][授权方APPID]
     */
    private String identification;

    /**
     * 第三方平台 appid
     */
    String thirdPartyAppid;

    /**
     * 账号 appid
     */
    private String accountAppid;

    /**
     * 刷新令牌（在授权的公众号具备API权限时，才有此返回值），刷新令牌主要用于第三方平台获取和刷新已授权用户的
     * authorizer_access_token。一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌。用户重新授权后，之前的刷新令牌会失效
     */
    private String authorizerRefreshToken;

    /**
     * @return thirdPartyAppid sa
     */
    public String getThirdPartyAppid() {
        return thirdPartyAppid;
    }

    /**
     * @return accountAppid sa
     */
    public String getAccountAppid() {
        return accountAppid;
    }

    /**
     * @return authorizerRefreshToken sa
     */
    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    /**
     * @param authorizerRefreshToken
     *            the authorizerRefreshToken to set
     */
    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }

    /**
     * 
     * token 标识；[第三方平台 APPID][分隔符ZKWXConstants.cacheKeySeparator][授权方APPID]
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * 过期判断时，偏移量，单位秒；正数为提前多少秒；负数为延迟多少秒;
     * 
     * @return
     * @see com.zk.wechat.wx.common.ZKWXAbstractAccessToken#getOffsetExpiresTime()
     */
    @Override
    protected int getOffsetExpiresTime() {
        // TODO Auto-generated method stub
        return 60;
    }

    /**
     * 制作 预授权码 的缓存ID
     * 
     * [第三方平台 APPID][KEY 组合时 分隔符 ZKWXConstants.cacheKeySeparator][预授权码]
     *
     * @Title: makeIdentification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 5, 2021 11:27:58 AM
     * @param thirdPartyAppid
     * @param preAuthCode
     * @return
     * @return String
     */
    public static String makeIdentification(String thirdPartyAppid, String authorizerAppid) {
        StringBuilder sb = new StringBuilder();
        sb.append(thirdPartyAppid);
        sb.append(ZKWXConstants.cacheKeySeparator);
        sb.append(authorizerAppid);
        return sb.toString();
    }

}
