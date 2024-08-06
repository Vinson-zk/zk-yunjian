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
* @Title: ZKWXApiPayV3Utils.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 9:21:08 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson2.JSONObject;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKHttpApiUtils;
import com.zk.wechat.wx.common.ZKWXConstants;
import com.zk.wechat.wx.pay.entity.jsapi.ZKWXV3JsApiOrder;
import com.zk.wechat.wx.pay.entity.nativeapi.ZKWXV3PartnerNativeApiOrder;
import com.zk.wechat.wx.utils.ZKWXUtils;

/** 
* @ClassName: ZKWXApiPayV3Utils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXV3ApiPayUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKWXV3ApiPayUtils.class);

    public static interface Key {

        /**
         * 关闭订单；其中 {0} 替换为商户订单号 out_trade_no
         */
        public static final String close = "zk.wechat.wx.pay.transactions.close";

        /**
         * GET 获取平台证书列表
         */
        public static final String certs = "zk.wechat.wx.pay.certificates";

        /**
         * jsapi 统一下单
         */
        public static final String jsapi = "zk.wechat.wx.pay.transactions.jsapi";

        /**
         * Native 下单 API
         */
        public static final String nativeApiUrl = "zk.wechat.wx.pay.transactions.native";
    }

    private static void addHeaderAuthSign(Map<String, String> header, RequestMethod method, String apiUrl, String body,
            String mchid, PrivateKey privateKey, String certMyPrivateSerialNo) {
        // 生成签名
//      String method, String apiUrl,
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = ZKWXUtils.getUUID32();
        String sign = ZKWXPayUtils.buildRequestSignStr(method.name(), apiUrl, timestamp, nonceStr, body);
        sign = ZKWXPayUtils.signSHA256withRSA(sign.getBytes(), privateKey);
        // 添加请求签名
        StringBuffer authStrBuf = new StringBuffer();
        authStrBuf = authStrBuf.append(ZKWXPayConstants.WXReqHeader.schema).append(" ");
        authStrBuf = authStrBuf.append(ZKWXPayConstants.WXReqHeader.ContentKey.mchid).append("=\"").append(mchid).append("\",");
        authStrBuf = authStrBuf.append(ZKWXPayConstants.WXReqHeader.ContentKey.nonce_str).append("=\"").append(nonceStr).append("\",");
        authStrBuf = authStrBuf.append(ZKWXPayConstants.WXReqHeader.ContentKey.serial_no).append("=\"").append(certMyPrivateSerialNo).append("\",");
        authStrBuf = authStrBuf.append(ZKWXPayConstants.WXReqHeader.ContentKey.timestamp).append("=\"").append(timestamp).append("\",");
        authStrBuf = authStrBuf.append(ZKWXPayConstants.WXReqHeader.ContentKey.signature).append("=\"").append(sign).append("\"");

//        return "mchid=\"" + yourMerchantId + "\","
//        + "nonce_str=\"" + nonceStr + "\","
//        + "timestamp=\"" + timestamp + "\","
//        + "serial_no=\"" + yourCertificateSerialNo + "\","
//        + "signature=\"" + signature + "\"";
        
        header.put(ZKWXPayConstants.WXReqHeader.Authorization, authStrBuf.toString());
    }
    
    // 关闭订单
    public static String close(String closeUrl, String mchid, String out_trade_no, PrivateKey privateKey,
            String certMyPrivateSerialNo) {
        if (closeUrl == null) {
            closeUrl = ZKEnvironmentUtils.getString(Key.close);
        }
        closeUrl = ZKStringUtils.replaceByPoint(closeUrl, out_trade_no);

//        httpPost.setHeader("Accept", "application/json");
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");

        String postBody = String.format("{\"%s\":\"%s\"}", ZKWXPayConstants.ParamName.mchid, mchid);
        // 添加请求头签名
        addHeaderAuthSign(header, RequestMethod.POST, closeUrl, postBody, mchid, privateKey, certMyPrivateSerialNo);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatus = ZKHttpApiUtils.postJson(closeUrl, postBody, header, outStringBuffer);

        String resStr = outStringBuffer.toString();

        if (resStatus != 204) {
            throw ZKBusinessException.as(parseErrCode(resStatus, resStr), "向微信平台请求关闭订单异常 " + resStr);
        }
        return parseErrCode(resStatus, resStr);
    }

    // 下载平台证书
    public static String dowloadCerts(String certsUrl, String mchid, PrivateKey privateKey,
            String certMyPrivateSerialNo) {

        if (certsUrl == null) {
            certsUrl = ZKEnvironmentUtils.getString(Key.certs);
        }

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");

        String postBody = "";
        // 添加请求头签名
        addHeaderAuthSign(header, RequestMethod.GET, certsUrl, postBody, mchid, privateKey, certMyPrivateSerialNo);

//        System.out.println("[^_^:20210223-1145-001] header: " + ZKJsonUtils.toJsonStr(header));

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatus = ZKHttpApiUtils.get(certsUrl, header, outStringBuffer);

        String resStr = outStringBuffer.toString();

        if (resStatus != 200) {
            throw ZKBusinessException.as(parseErrCode(resStatus, resStr), "向微信平台请求下载平台证书异常 " + resStr);
        }
        return resStr;
    }

    private static String parseErrCode(int resStatus, String resStr) {
        try {
            if (!ZKStringUtils.isEmpty(resStr)) {
                log.error("[>_<:20210223-1837-001] 与微信平台数据交互异常：{}", resStr);
                JSONObject obj = ZKJsonUtils.parseJSONObject(resStr);
                resStr = obj.getString(ZKWXPayConstants.ResErr.code);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return String.format("%s.%s.%s", ZKWXConstants.errCodePrefix, resStatus, resStr);
    }

    /************************************************************/
    /* JSAPI 支付 */

    /**
     * JsApi 统一下单
     *
     * @Title: transactionsJsapi
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 19, 2021 11:46:11 AM
     * @param jsApiOrder
     * @return
     * @throws UnsupportedEncodingException
     * @return String
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String jsapi(String jsApiUrl, ZKWXV3JsApiOrder jsApiOrder, PrivateKey privateKey,
                               String certMyPrivateSerialNo) {

        if (jsApiUrl == null) {
            jsApiUrl = ZKEnvironmentUtils.getString(Key.jsapi);
        }

//        httpPost.setHeader("Accept", "application/json");
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");

        String postBody = ZKJsonUtils.toJsonStr(jsApiOrder);

        // 添加请求头签名
        addHeaderAuthSign(header, RequestMethod.POST, jsApiUrl, postBody, jsApiOrder.getMchid(), privateKey,
                certMyPrivateSerialNo);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatus = ZKHttpApiUtils.postJson(jsApiUrl, postBody, header, outStringBuffer);

        String resStr = outStringBuffer.toString();

        if (resStatus != 200 && resStatus != 204) {
            throw ZKBusinessException.as(parseErrCode(resStatus, resStr), "向微信平台请求统一下单异常 " + resStr);
        }

        return resStr;
    }

    /************************************************************/
    /* 服务商 partner Native 支付 */

    // 服务商 partner Native 下单
    public static String partnerNativeapi(String nativeApiUrl, ZKWXV3PartnerNativeApiOrder partnerNativeApiOrder,
        PrivateKey privateKey, String certMyPrivateSerialNo) {
        if (nativeApiUrl == null) {
            nativeApiUrl = ZKEnvironmentUtils.getString(Key.nativeApiUrl);
        }

//        httpPost.setHeader("Accept", "application/json");
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");

        String postBody = ZKJsonUtils.toJsonStr(partnerNativeApiOrder);

        // 添加请求头签名
        addHeaderAuthSign(header, RequestMethod.POST, nativeApiUrl, postBody, partnerNativeApiOrder.getSp_mchid(), privateKey,
                certMyPrivateSerialNo);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatus = ZKHttpApiUtils.postJson(nativeApiUrl, postBody, header, outStringBuffer);

        String resStr = outStringBuffer.toString();

        if (resStatus != 200 && resStatus != 204) {
            throw ZKBusinessException.as(parseErrCode(resStatus, resStr), "向微信平台请求统一下单异常 " + resStr);
        }

        return resStr;
    }

}
