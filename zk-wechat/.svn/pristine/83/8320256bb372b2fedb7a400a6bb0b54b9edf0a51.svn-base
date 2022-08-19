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
* @Title: ZKWechatCacheUtils.java 
* @author Vinson 
* @Package com.zk.wechat.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 7, 2021 3:28:10 PM 
* @version V1.0 
*/
package com.zk.wechat.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPAuthAccountAccessToken;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPComponentAccessToken;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPPreAuthCode;

/** 
* @ClassName: ZKWechatCacheUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWechatCacheUtils {
    
    private static Logger logger = LoggerFactory.getLogger(ZKWechatCacheUtils.class);

    static ZKCacheManager<String> zkCacheManager = null;

    @SuppressWarnings("unchecked")
    private static ZKCacheManager<String> getCacheManager() {
        if (zkCacheManager == null) {
            zkCacheManager = (ZKCacheManager<String>) ZKEnvironmentUtils.getApplicationContext()
                    .getBean("zkCacheManager");
        }
        return zkCacheManager;
    }

    /**
     * 缓存名称
     * 
     * @ClassName: KEY_PREFIX
     * @Description: TODO(simple description this class what to do.)
     * @author bs
     * @version 1.0
     */
    private static interface Cache_Name {

        /**
         * 微信开放平台，第三方平台 ComponentAccessToken 存放的缓存名称
         */
        public static final String ZKWXTPComponentAccessToken = "_wx_third_party_componentAccessToken_";

        /**
         * 预授权码存放的缓存名称
         */
        public static final String ZKWXTPPreAuthCode = "_wx_third_party_PreAuthCode_";

        /**
         * 目标授权账号 AccessToken 缓存名称
         */
        public static final String ZKWXTPAuthAccountAccessToken = "_wx_third_party_auth_account_AccessToken_";

//        /**
//         * 用户授权 Token 缓存名称
//         */
//        public static final String UserAuthAccessToken = "_wx_user_auth_AccessToken_";

//        /**
//         * 授权方 JS 令牌
//         */
//        public static final String JsTicket = "_wx_js_ticket_";

    }

    /**
     * 取微信开放平台，第三方平台 ComponentAccessToken 存放的缓存
     */
    private static ZKCache<String, ZKWXTPComponentAccessToken> getCacheWXTPComponentAccessToken() {
        return getCacheManager().getCache(Cache_Name.ZKWXTPComponentAccessToken, 7200);
    }

    /**
     * 取预授权码存放的缓存
     */
    private static ZKCache<String, ZKWXTPPreAuthCode> getCacheWXTPPreAuthCode() {
        return getCacheManager().getCache(Cache_Name.ZKWXTPPreAuthCode, 1800 * 2);
    }

    /**
     * 取授权方 Token 缓存
     */
    private static ZKCache<String, ZKWXTPAuthAccountAccessToken> getCacheWXTPAuthAccountAccessToken() {
        return getCacheManager().getCache(Cache_Name.ZKWXTPAuthAccountAccessToken, 7200);
    }

    /*******************************************************/
    /*** 第三方平台 ComponentAccessToken 存放的缓存 */
    /**
     * 存 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: putWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:52 PM
     * @param componentAccessToken
     * @return void
     */
    public static void putWXTPComponentAccessToken(ZKWXTPComponentAccessToken componentAccessToken) {
        try {
            getCacheWXTPComponentAccessToken().put(componentAccessToken.getThirdPartyAppid(), componentAccessToken);
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-001] 存 第三方平台 ComponentAccessToken 缓存失败。 componentAccessToken：{}",
                    ZKJsonUtils.writeObjectJson(componentAccessToken));
            e.printStackTrace();
        }
    }

    /**
     * 取 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: getWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:59 PM
     * @param thirdPartyAppid
     * @return
     * @return ZKWXTPComponentAccessToken
     */
    public static ZKWXTPComponentAccessToken getWXTPComponentAccessToken(String thirdPartyAppid) {
        try {
            return getCacheWXTPComponentAccessToken().get(thirdPartyAppid);
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-002] 取 第三方平台 ComponentAccessToken 缓存失败。thirdPartyAppid:{}",
                    thirdPartyAppid);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: removeWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:05 PM
     * @param thirdPartyAppid
     * @return void
     */
    public static void removeWXTPComponentAccessToken(String thirdPartyAppid) {
        try {
            getCacheWXTPComponentAccessToken().remove(thirdPartyAppid);
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-003] 删 第三方平台 ComponentAccessToken 缓存失败。thirdPartyAppid:{}",
                    thirdPartyAppid);
            e.printStackTrace();
        }
    }

    /**
     * 清空 第三方平台 ComponentAccessToken 缓存
     *
     * @Title: clearWXTPComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:11 PM
     * @return void
     */
    public static void clearWXTPComponentAccessToken() {
        try {
            getCacheWXTPComponentAccessToken().clear();
        }
        catch(Exception e) {
            logger.error("[>_<:20180912-1432-004] 清空 第三方平台 ComponentAccessToken 缓存失败。");
            e.printStackTrace();
        }
    }

    /*******************************************************/
    /*** 预授权码存放 缓存 */
    /**
     * 存 预授权码 缓存
     *
     * @Title: putWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:52 PM
     * @param preAuthCode
     * @return void
     */
    public static void putWXTPPreAuthCode(ZKWXTPPreAuthCode preAuthCode) {
        try {
            getCacheWXTPPreAuthCode().put(preAuthCode.getIdentification(), preAuthCode);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-001] 存 预授权码 缓存失败。 preAuthCode：{}",
                    ZKJsonUtils.writeObjectJson(preAuthCode));
            e.printStackTrace();
        }
    }

    /**
     * 取 预授权码 缓存
     *
     * @Title: getWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:59 PM
     * @param identification
     * @return ZKWXTPPreAuthCode
     */
    public static ZKWXTPPreAuthCode getWXTPPreAuthCode(String identification) {
        try {
            return getCacheWXTPPreAuthCode().get(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-002] 取 预授权码 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删 预授权码 缓存
     *
     * @Title: removeWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:05 PM
     * @param identification
     * @return void
     */
    public static void removeWXTPPreAuthCode(String identification) {
        try {
            getCacheWXTPPreAuthCode().remove(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-003] 删 预授权码 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
    }

    /**
     * 清空 预授权码 缓存
     *
     * @Title: clearWXTPPreAuthCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:11 PM
     * @return void
     */
    public static void clearWXTPPreAuthCode() {
        try {
            getCacheWXTPPreAuthCode().clear();
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-004] 清空 预授权码 缓存失败。");
            e.printStackTrace();
        }
    }

    /*******************************************************/
    /*** 目标授权账号 缓存 */
    /**
     * 存 目标授权账号 AccessToken 缓存
     *
     * @Title: putWXTPAuthAccountAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:52 PM
     * @param authAccountAccessToken
     * @return void
     */
    public static void putWXTPAuthAccountAccessToken(ZKWXTPAuthAccountAccessToken authAccountAccessToken) {
        try {
            getCacheWXTPAuthAccountAccessToken().put(authAccountAccessToken.getIdentification(),
                    authAccountAccessToken);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-001] 存 存目标授权账号 AccessToken 缓存失败。 authAccountAccessToken：{}",
                    ZKJsonUtils.writeObjectJson(authAccountAccessToken));
            e.printStackTrace();
        }
    }

    /**
     * 取 目标授权账号 AccessToken 缓存
     *
     * @Title: getWXTPAuthAccountAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:47:59 PM
     * @param identification
     * @return ZKWXTPPreAuthCode
     */
    public static ZKWXTPAuthAccountAccessToken getWXTPAuthAccountAccessToken(String identification) {
        try {
            return getCacheWXTPAuthAccountAccessToken().get(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211107-1432-002] 取 目标授权账号 AccessToken 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删 目标授权账号 AccessToken 缓存
     *
     * @Title: removeWXTPAuthAccountAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:05 PM
     * @param identification
     * @return void
     */
    public static void removeWXTPAuthAccountAccessToken(String identification) {
        try {
            getCacheWXTPAuthAccountAccessToken().remove(identification);
        }
        catch(Exception e) {
            logger.error("[>_<:20211108-1432-003] 删 目标授权账号 AccessToken 缓存失败。identification:{}", identification);
            e.printStackTrace();
        }
    }

    /**
     * 清空 目标授权账号 AccessToken 缓存
     *
     * @Title: clearWXTPAuthAccountAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:48:11 PM
     * @return void
     */
    public static void clearWXTPAuthAccountAccessToken() {
        try {
            getCacheWXTPAuthAccountAccessToken().clear();
        }
        catch(Exception e) {
            logger.error("[>_<:20211108-1432-004] 清空 目标授权账号 AccessToken 缓存失败。");
            e.printStackTrace();
        }
    }


}
