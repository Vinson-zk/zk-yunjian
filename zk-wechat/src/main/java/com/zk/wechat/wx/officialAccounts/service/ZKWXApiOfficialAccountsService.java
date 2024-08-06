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
* @Title: ZKWXApiOfficialAccountsService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 2:21:59 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.zk.core.commons.data.ZKJsonArray;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKHttpApiUtils;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUser;
import com.zk.wechat.wx.officialAccounts.ZKWXOfficialAccountsConstants.ConfigKey;
import com.zk.wechat.wx.officialAccounts.ZKWXOfficialAccountsConstants.MsgAttr;
import com.zk.wechat.wx.utils.ZKWXUtils;

/**
 * @ClassName: ZKWXApiOfficialAccountsService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
public class ZKWXApiOfficialAccountsService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    /**
     * 取用户基本信息
     *
     * @Title: api_user_auth_userinfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 20, 2022 1:06:26 AM
     * @param outAccountUser
     * @param accessToken
     * @param openid
     * @param lang
     * @return void
     */
    public void api_user_auth_userinfo(ZKOfficialAccountsUser outAccountUser, String accessToken, String openid,
            String lang) {
//        # 拉取用户信息(需 scope 为 snsapi_userinfo) 示例：http：GET（请使用 https 协议） https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
//        zk.wechat.wx.officialAccounts.api.user.auth.userinfo=https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang={2}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_user_auth_userinfo);
        url = ZKStringUtils.replaceByPoint(url, accessToken, openid, lang);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.get(url, null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        // *********************************************************************
        // openid 用户的唯一标识
        outAccountUser.setWxOpenid(openid);
        // nickname 用户昵称
        outAccountUser.setWxNickname(resJson.getString(MsgAttr.userInfo.nickname));
        // sex 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
        outAccountUser.setWxSex(resJson.getInteger(MsgAttr.userInfo.sex));
        // province 用户个人资料填写的省份
        outAccountUser.setWxProvince(resJson.getString(MsgAttr.userInfo.province));
        // city 普通用户个人资料填写的城市
        outAccountUser.setWxCity(resJson.getString(MsgAttr.userInfo.city));
        // country 国家，如中国为CN
        outAccountUser.setWxCountry(resJson.getString(MsgAttr.userInfo.country));
        // headimgurl 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像 URL 将失效。
        outAccountUser.setWxHeadimgurl(resJson.getString(MsgAttr.userInfo.headimgurl));
        // privilege 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
        outAccountUser.setWxPrivilege(ZKJsonArray.parse(resJson.getJSONArray(MsgAttr.userInfo.privilege)));
        // unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
        outAccountUser.setWxUnionid(resJson.getString(MsgAttr.userInfo.unionid));
    }

    /**
     * 获取用户基本信息（包括 UnionID 机制）
     *
     * @Title: api_user_auth_info
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 20, 2022 1:06:39 AM
     * @param outAccountUser
     * @param accessToken
     * @param openid
     * @param lang
     * @return void
     */
    public void api_user_auth_info(ZKOfficialAccountsUser outAccountUser, String accessToken, String openid,
            String lang) {
        /*
         {"subscribe":1,
            "openid":"o3YiM6aZPR-v0OmcnZPAv_Q9ruZY",
            "nickname":"", // 不返回值了
            "sex":0, // 不返回值了
            "language":"zh_CN",
            "city":"", // 不返回值了
            "province":"",// 不返回值了
            "country":"",// 不返回值了
            "headimgurl":"",// 不返回值了
            "subscribe_time":1653224833,
            "unionid":"of8UOwmlQ8v1KWqMNdrJRmCNj-m0",
            "remark":"",
            "groupid":0,
            "tagid_list":[],
            "subscribe_scene":"ADD_SCENE_QR_CODE",
            "qr_scene":0,
            "qr_scene_str":""
         }
         */
//        # 获取用户基本信息（包括 UnionID 机制）示例：GET https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
//        zk.wechat.wx.officialAccounts.api.user.auth.info=https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang={2}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_user_auth_info);
        url = ZKStringUtils.replaceByPoint(url, accessToken, openid, lang);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.get(url, null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);
        
        // *********************************************************************
        // openid 用户的标识，对当前公众号唯一
        outAccountUser.setWxOpenid(openid);
        // subscribe 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
//        resJson.getInteger(MsgAttr.userInfo.subscribe_scene)
        outAccountUser.setWxSubscribe(resJson.getInteger(MsgAttr.userInfo.subscribe));
        if (outAccountUser.getWxSubscribe() == null || outAccountUser.getWxSubscribe().intValue() != 1) {
            // 没有关注
            outAccountUser.setWxSubscribe(ZKOfficialAccountsUser.KeyWxSubscribe.unsubscribe);
            return;
        }

        // 不返回值了 用户昵称
        outAccountUser.setWxNickname(resJson.getString(MsgAttr.userInfo.nickname));
        // 不返回值了 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
        outAccountUser.setWxSex(resJson.getInteger(MsgAttr.userInfo.sex));
        // language 用户的语言，简体中文为zh_CN
        outAccountUser.setWxLanguage(resJson.getString(MsgAttr.userInfo.language));
        // 不返回值了 普通用户个人资料填写的城市
        outAccountUser.setWxCity(resJson.getString(MsgAttr.userInfo.city));
        // 不返回值了 用户个人资料填写的省份
        outAccountUser.setWxProvince(resJson.getString(MsgAttr.userInfo.province));
        // 不返回值了 国家，如中国为CN
        outAccountUser.setWxCountry(resJson.getString(MsgAttr.userInfo.country));
        // 不返回值了 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像 URL 将失效。
        outAccountUser.setWxHeadimgurl(resJson.getString(MsgAttr.userInfo.headimgurl));
        // subscribe_time 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
        outAccountUser.setWxSubscribeTimeStr(resJson.getString(MsgAttr.userInfo.subscribe_time));
        if (!ZKStringUtils.isEmpty(outAccountUser.getWxSubscribeTimeStr())) {
            outAccountUser.setWxSubscribeDate(
                    ZKDateUtils.parseDate(Long.valueOf(outAccountUser.getWxSubscribeTimeStr()) * 1000));
        }
        // unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
        outAccountUser.setWxUnionid(resJson.getString(MsgAttr.userInfo.unionid));
        // remark 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
        outAccountUser.setWxRemark(resJson.getString(MsgAttr.userInfo.remark));
        // groupid 用户所在的分组ID（暂时兼容用户分组旧接口）
        outAccountUser.setWxGroupid(resJson.getString(MsgAttr.userInfo.groupid));
        // tagid_list 用户被打上的标签 ID 列表
        outAccountUser.setWxTagidList(resJson.getString(MsgAttr.userInfo.tagid_list));
        // subscribe_scene 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE
        // 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载
        // ，ADD_SCENE_LIVESTREAM 视频号直播， ADD_SCENE_CHANNELS 视频号, ADD_SCENE_OTHERS 其他
        outAccountUser.setWxSubscribeScene(resJson.getString(MsgAttr.userInfo.subscribe_scene));
        // qr_scene 二维码扫码场景（开发者自定义）
        outAccountUser.setWxQrScene(resJson.getString(MsgAttr.userInfo.qr_scene));
        // qr_scene_str 二维码扫码场景描述（开发者自定义）
        outAccountUser.setWxQrSceneStr(resJson.getString(MsgAttr.userInfo.qr_scene_str));

    }

}
