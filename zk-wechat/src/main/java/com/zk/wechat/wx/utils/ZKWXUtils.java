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
* @Title: ZKWXUtils.java 
* @author Vinson 
* @Package com.zk.wechat.wx 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2021 4:07:48 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.utils;

import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.alibaba.fastjson2.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.zk.core.encrypt.utils.ZKEncryptUtils;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKXmlUtils;
import com.zk.wechat.wx.aes.ZKAesException;
import com.zk.wechat.wx.aes.ZKWXBizMsgCrypt;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.MsgAttr;

/** 
* @ClassName: ZKWXUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXUtils {
    
    private final static Logger logger = LogManager.getLogger(ZKWXUtils.class);

    /**
     * 获得32个长度的十六进制的UUID
     * 
     * @return UUID
     */
    public static String getUUID32() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

//    /**
//     * 进行 js 签名并设置签名相关配置
//     * 
//     * 设置 nonceStr 随机串
//     * 
//     * 设置签名时间戳 秒
//     *
//     * @Title: doingJsSignature
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date 2018年9月17日 上午11:42:38
//     * @param jsTicket
//     * @param url
//     * @param jsConfig
//     * @return
//     * @return JsConfig
//     */
//    public static JsConfig doingJsSignature(String jsTicket, String url) {
//
//        JsConfig jsConfig = new JsConfig();
//        jsConfig.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
//        jsConfig.setTimestamp(System.currentTimeMillis() / 1000);
//
//        String str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", jsTicket, jsConfig.getNonceStr(),
//                jsConfig.getTimestamp(), url);
//        jsConfig.setSignature(WXMsgUtils.SHA1Encode(str.getBytes()));
//
//        return jsConfig;
//    }

    /***
     * 加密xml
     *
     * @Title: encryptXml
     * @Description:
     * @author Wyman liu
     * @date Aug 3, 2018 11:08:12 AM
     * @param token
     * @param encodingAesKey
     * @param appId
     * @param xml
     * @return
     * @throws AesException
     *             String
     */
    public static String encryptXml(String token, String encodingAesKey, String appId, String xml) throws AesException {
        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        String timestamp = System.currentTimeMillis() + "";
        String nonce = Double.valueOf(Math.random() * 1000000).intValue() + "";
        return pc.encryptMsg(xml, timestamp, nonce);
    }

    /***
     * 解密xml
     *
     * @Title: decryptXml
     * @Description:
     * @author Wyman liu
     * @date Aug 3, 2018 11:07:49 AM
     * @param token
     * @param encodingAesKey
     * @param appId
     * @param xml
     * @return
     * @throws DocumentException
     * @throws AesException
     *             String
     */
    public static String decryptXml(String token, String encodingAesKey, String appId, String source)
            throws ZKAesException {
        try {
            ZKWXBizMsgCrypt pc = new ZKWXBizMsgCrypt(token, encodingAesKey, appId);

            Document document = ZKXmlUtils.getDocument(source);
            Element root = document.getRootElement();

            String msgSignature = root.element("MsgSignature") == null ? "" : root.element("MsgSignature").getText();
            String timestamp = root.element("TimeStamp") == null ? "" : root.element("TimeStamp").getText();
            String nonce = root.element("Nonce") == null ? "" : root.element("Nonce").getText();

            return pc.decryptMsg(msgSignature, timestamp, nonce, source);
        }
        catch(ZKAesException e) {
            logger.error("[>_<:20180906-1959-001] WeChat 消息加解密失败；\ntoken: {}\nencodingAesKey: {}\nappId: {}\nsource: {}",
                    token, encodingAesKey, appId, source);
            throw e;
        }
    }

    /**
     * 微信消息 sha1 加密
     *
     * @Title: SHA1Encode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date 2018年9月17日 上午11:35:51
     * @param sources
     * @return
     * @return String
     */
    public static String SHA1Encode(byte[] sources) {
        String resultString = null;
        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-1");
//            resultString = ZKEncodingUtils.encodeHex(md.digest(sources));
            resultString = ZKEncodingUtils
                    .encodeHex(ZKEncryptUtils.encryptDigest(sources, null, ZKEncryptUtils.DIGEST_MODE.SHA_1));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * 微信消息 Md5 加密
     *
     * @Title: SHA1Encode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date 2018年9月17日 上午11:35:51
     * @param sources
     * @return
     * @return String
     */
    public static String Md5Encode(byte[] sources) {
        String resultString = null;
        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            resultString = ZKEncodingUtils.encodeHex(md5.digest(sources));
            resultString = ZKEncodingUtils.encodeHex(ZKEncryptUtils.md5Encode(sources));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * 检查 请求 WeChat 响应码
     *
     * @Title: checkResStatusCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 5, 2021 12:24:21 AM
     * @param resStatusCode
     *            请求 WeChat 响应码
     * @return void
     */
    public static void checkResStatusCode(int resStatusCode) {
        if (resStatusCode < 200 || resStatusCode > 299) {
            logger.error("[>_<:20211105-0016-001] 请求微信平台链接异常, resposeStatusCode:{}", resStatusCode);
            throw ZKBusinessException.as("zk.wechat.110001", null, resStatusCode);
        }
    }

    /**
     * 检查 WeChat 返回消息是否正常，若包含错误，返回异常；
     *
     * @Title: checkJsonMsg
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date 2018年9月7日 下午1:50:55
     * @param msgMap
     *            消息 map
     * @return void
     */
    public static void checkJsonMsg(Map<String, ?> msgMap) {
        if (msgMap != null && msgMap.get(MsgAttr.Error.errcode) != null
                && !"0".equals(msgMap.get(MsgAttr.Error.errcode).toString())) {
            logger.error("[>_<:20211105-0016-002] 微信返回消息异常！微信异常信息：{}", ZKJsonUtils.toJsonStr(msgMap));
            throw ZKBusinessException.asMsg("wx." + msgMap.get(MsgAttr.Error.errcode),
                    msgMap.get(MsgAttr.Error.errmsg).toString(), msgMap);
        }
    }

    public static void checkJsonMsg(JSONObject resMsg) {
        if (resMsg != null && resMsg.containsKey(MsgAttr.Error.errcode)
                && !"0".equals(resMsg.getString(MsgAttr.Error.errcode))) {
            logger.error("[>_<:20211105-0016-002] 微信返回消息异常！微信异常信息：{}", resMsg.toJSONString());
            throw ZKBusinessException.asMsg("wx." + resMsg.getString(MsgAttr.Error.errcode),
                    resMsg.getString(MsgAttr.Error.errmsg).toString(), resMsg);
        }
    }

}
