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
* @Title: ZKWXUserAuthAccessToken.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.msgBean 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 5, 2021 8:22:59 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.msgBean;

import com.zk.wechat.wx.common.ZKWXAbstractAccessToken;
import com.zk.wechat.wx.common.ZKWXConstants;

/**
 * 目标授权用户的 AccessToken
 * 
 * @ClassName: ZKWXUserAuthAccessToken
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKWXUserAuthAccessToken extends ZKWXAbstractAccessToken {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;
    
    public ZKWXUserAuthAccessToken(String thirdPartyAppid, String accountAppid, String openId) {
        this.identification = makeIdentification(thirdPartyAppid, accountAppid, openId);
        this.openid = openId;
        this.accountAppid = accountAppid;
    }

    /**
     * token
     * 标识；[第三方平台标识][分隔符ZKWXConstants.cacheKeySeparator][授权方APPID][分隔符ZKWXConstants.cacheKeySeparator][用户微信openid]
     */
    private String identification;

    /**
     * 公众号或小程序的账号 appId
     */
    private String accountAppid;

    /**
     * 用户刷新 access_token
     */
    private String refreshToken;

    /**
     * 授权用户唯一标识
     */
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;

    /**
     * token 标识；[授权方APPID][KEY 组合时 分隔符 ZKWXConstants.cacheKeySeparator][用户微信openid]
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
     * @return refreshToken sa
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * @return openid sa
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @return scope sa
     */
    public String getScope() {
        return scope;
    }

    /**
     * @return accountAppid sa
     */
    public String getAccountAppid() {
        return accountAppid;
    }

    /**
     * @param refreshToken
     *            the refreshToken to set
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public static String makeIdentification(String thirdPartyAppid, String accountAppid, String openId) {
        StringBuilder sb = new StringBuilder();
        sb.append(thirdPartyAppid);
        sb.append(ZKWXConstants.cacheKeySeparator);
        sb.append(accountAppid);
        sb.append(ZKWXConstants.cacheKeySeparator);
        sb.append(openId);
        return sb.toString();
    }

}
