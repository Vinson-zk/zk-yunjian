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
* @Title: ZKWXOfficialAccountsUserService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 2:32:31 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.exception.ZKBusinessException;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccounts;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUser;
import com.zk.wechat.officialAccounts.service.ZKOfficialAccountsService;
import com.zk.wechat.officialAccounts.service.ZKOfficialAccountsUserService;
import com.zk.wechat.wx.common.ZKWXConstants;

/**
 * @ClassName: ZKWXOfficialAccountsUserService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
public class ZKWXOfficialAccountsUserService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKOfficialAccountsService officialAccountsService;

    @Autowired
    ZKOfficialAccountsUserService officialAccountsUserService;

    @Autowired
    ZKWXApiOfficialAccountsService wxApiOfficialAccountsService;

//    // 取用户基本信息
//    public ZKOfficialAccountsUser getUserBaseInfo(String thirdPartyAppId, String appId, String openId,
//            String accessToken) {
//        ZKOfficialAccountsUser outAccountUser = this.officialAccountsUserService.getByOpenId(thirdPartyAppId, appId,
//                openId);
//        if (outAccountUser == null) {
//            outAccountUser = new ZKOfficialAccountsUser();
//        }
//        return this.getUserBaseInfo(outAccountUser, thirdPartyAppId, appId, openId, accessToken);
//    }

    // 取用户基本信息
    public ZKOfficialAccountsUser getUserBaseInfo(ZKOfficialAccountsUser outAccountUser, String thirdPartyAppId,
            String appId, String openId, String accessToken) {
        if (outAccountUser == null) {
            outAccountUser = new ZKOfficialAccountsUser();
        }

        wxApiOfficialAccountsService.api_user_auth_userinfo(outAccountUser, accessToken, openId,
                ZKWXConstants.KeyLocale.zh_CN);
        this.putUserInfo(thirdPartyAppId, appId, outAccountUser);
        return outAccountUser;
    }

//    // 取用户基本信息 UnionID 机制
//    public ZKOfficialAccountsUser getUserUnionID(String thirdPartyAppId, String appId, String openId,
//            String accessToken) {
//        ZKOfficialAccountsUser outAccountUser = this.officialAccountsUserService.getByOpenId(thirdPartyAppId, appId,
//                openId);
//        if (outAccountUser == null) {
//            outAccountUser = new ZKOfficialAccountsUser();
//        }
//        else {
//
//        }
//        return this.getUserUnionID(outAccountUser, thirdPartyAppId, appId, openId, accessToken);
//    }

    // 取用户基本信息 UnionID 机制
    public ZKOfficialAccountsUser getUserUnionID(ZKOfficialAccountsUser outAccountUser, String thirdPartyAppId,
            String appId, String openId, String accessToken) {
        if (outAccountUser == null) {
            outAccountUser = new ZKOfficialAccountsUser();
        }
        wxApiOfficialAccountsService.api_user_auth_info(outAccountUser, accessToken, openId,
                ZKWXConstants.KeyLocale.zh_CN);
        this.putUserInfo(thirdPartyAppId, appId, outAccountUser);
        return outAccountUser;
    }

    /**
     * 设置用户其他信息; 公司信息，第三方平台，授权公众号等信息
     *
     * @Title: putUserInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 23, 2022 10:21:46 AM
     * @param thirdPartyAppId
     * @param appId
     * @param officialAccountUser
     * @return void
     */
    public void putUserInfo(String thirdPartyAppId, String appId, ZKOfficialAccountsUser officialAccountUser) {
        ZKOfficialAccounts officialAccounts = officialAccountsService.getByAccountAppid(thirdPartyAppId, appId);
        if (officialAccounts == null) {
            log.error(
                    "[>_<_^:20220520-0116-001] 目标授权方未向第三方平台授权，无法获取 AuthAccountAccessToken; thirdPartyAppid：{}，accountAppid：{}",
                    thirdPartyAppId, appId);
            throw ZKBusinessException.as("zk.wechat.010011");
        }
        officialAccountUser.setWxThirdPartyAppid(thirdPartyAppId);
        officialAccountUser.setWxOfficialAccountAppid(appId);
        officialAccountUser.setGroupCode(officialAccounts.getGroupCode());
        officialAccountUser.setCompanyId(officialAccounts.getCompanyId());
        officialAccountUser.setCompanyCode(officialAccounts.getCompanyCode());
    }

}

