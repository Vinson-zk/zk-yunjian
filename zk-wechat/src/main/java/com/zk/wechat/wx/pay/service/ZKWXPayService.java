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
* @Title: ZKWXPayService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 5:05:25 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.service;

import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.common.ZKWechatUtils;
import com.zk.wechat.pay.entity.ZKPayMerchant;
import com.zk.wechat.pay.entity.ZKPlatformCert;
import com.zk.wechat.pay.service.ZKPayMerchantService;
import com.zk.wechat.pay.service.ZKPlatformCertService;
import com.zk.wechat.wx.pay.ZKWXPayUtils;
import com.zk.wechat.wx.pay.entity.ZKWXApiCert;
import com.zk.wechat.wx.pay.entity.ZKWXPlatformCert;

/** 
* @ClassName: ZKWXPayService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKWXPayService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKPayMerchantService merchantService;

    @Autowired
    ZKPlatformCertService platformCertService;

    @Autowired
    ZKWXPayJsApiService wxPayJsApiService;

    /******************************************************************/
    /*** 微信平台证书 ***************************************************/
    /******************************************************************/

    Map<String, Map<String, ZKWXPlatformCert>> platformCertMap = new HashMap<>();

    // 根据商户ID 取商户对应的平台证书，取最近生效的一个证书
    public ZKWXPlatformCert getPlatformCert(String mchid) throws Exception {
        String key = "-default";
        ZKWXPlatformCert cert = this.getPlatformCertMap(mchid).get(key);
        // 证书不存在，或过期，更新证书
        if (cert == null || cert.isExpiration()) {
            // 缓存中的证书过期，清理缓存，并更新证书
            if (cert != null) {
                this.cleanPlatformCert(mchid);
                wxPayJsApiService.dowloadPlatformCert(mchid);
            }

            ZKPlatformCert pCert = platformCertService.getCertByMchid(mchid);
            // 证书不存在或证书过期，更新证书
            if (pCert == null || pCert.isExpiration()) {
                wxPayJsApiService.dowloadPlatformCert(mchid);
                pCert = platformCertService.getCertByMchid(mchid);
            }
            else {
                try {
                    cert = this.getPlatformCert(pCert);
                }
                catch(FileNotFoundException e) {
                    // 文件不存在，重新下载
                    wxPayJsApiService.dowloadPlatformCert(mchid);
                    pCert = platformCertService.getCertByMchid(mchid);
                }
            }
            if (cert == null) {
                if (pCert == null) {
                    log.error("[>_<:20210220-1745-001] 平台证书[{0}]不存在", mchid);
                    throw ZKBusinessException.as("zk.wechat.000021", null, mchid);
                }
                cert = this.getPlatformCert(pCert);
            }
            this.getPlatformCertMap(mchid).put(key, cert);
        }
        
        return cert;
    }

    // 根据商户ID 和证书序列号 取商户对应的平台证书
    public ZKWXPlatformCert getPlatformCert(String mchid, String serialNo) throws Exception {
        ZKWXPlatformCert cert = this.getPlatformCertMap(mchid).get(serialNo);
        if (cert == null || cert.isExpiration()) {
            // 缓存中的证书过期，清理缓存，并更新证书
            if (cert != null) {
                this.cleanPlatformCert(mchid);
                wxPayJsApiService.dowloadPlatformCert(mchid);
                cert = null;
            }

            ZKPlatformCert pCert = platformCertService.getBySerial(mchid, serialNo);
            if (pCert == null) {
                // 证书不存在，更新证书
                wxPayJsApiService.dowloadPlatformCert(mchid);
                pCert = platformCertService.getBySerial(mchid, serialNo);
            }
            else {
                try {
                    cert = this.getPlatformCert(pCert);
                }
                catch(FileNotFoundException e) {
                    // 文件不存在，重新下载
                    wxPayJsApiService.dowloadPlatformCert(mchid);
                    pCert = platformCertService.getBySerial(mchid, serialNo);
                }
            }

            if (cert == null) {
                if (pCert == null) {
                    log.error("[>_<:20210222-1847-001] 平台证书[{}-{}]不存在", mchid, serialNo);
                    throw ZKBusinessException.as("zk.wechat.000022", null, mchid, serialNo);
                }
                cert = this.getPlatformCert(pCert);
            }

            this.getPlatformCertMap(mchid).put(serialNo, cert);
        }
        return this.getPlatformCertMap(mchid).get(serialNo);
    }

    public ZKWXPlatformCert getPlatformCert(ZKPlatformCert pCert) throws FileNotFoundException {
        X509Certificate cert = ZKWXPayUtils.getX509Certificate(ZKWechatUtils.getFilePath(pCert.getCertPath()));
        return new ZKWXPlatformCert(pCert.getCertSerialNo(), cert, cert.getNotAfter());
    }

    private Map<String, ZKWXPlatformCert> getPlatformCertMap(String mchid) {
        if (platformCertMap.get(mchid) == null) {
            platformCertMap.put(mchid, new HashMap<>());
        }
        return platformCertMap.get(mchid);
    }

    public void cleanAllPlatformCert() {
        platformCertMap.clear();
    }

    public void cleanPlatformCert(String mchid) {
        platformCertMap.remove(mchid);
    }

    /******************************************************************/
    /*** 商户 API 证书 *************************************************/
    /******************************************************************/

    Map<String, ZKWXApiCert> apiCertMap = new HashMap<>();
    // 根据商户ID 取商户对应的证书私钥
    public ZKWXApiCert getApiCertKey(String mchid) throws FileNotFoundException {
        if (apiCertMap.get(mchid) == null) {
            ZKPayMerchant merchant = merchantService.get(new ZKPayMerchant(mchid));
            if (merchant == null) {
                log.error("[>_<:20210220-1745-001] 商户号[{0}]未对接", mchid);
                throw ZKBusinessException.as("zk.wechat.000006", null, mchid);
            }
            PrivateKey privateKey = ZKWXPayUtils
                    .getRSAPrivateKey(ZKWechatUtils.getFilePath(merchant.getApiCertPathKeyPem()));
            apiCertMap.put(mchid, new ZKWXApiCert(merchant.getApiCertSerialNo(), privateKey));
        }
        return apiCertMap.get(mchid);
    }

    public void cleanAllApiCert() {
        apiCertMap.clear();
    }

    public void cleanApiCert(String mchid) {
        apiCertMap.remove(mchid);
    }

    /******************************************************************/
    /*** 商户 API V3 AES KEY *******************************************/
    /******************************************************************/
    Map<String, byte[]> apiv3AesKeyMap = new HashMap<>();

    // 根据商户ID 取商户对应的 apiv3AesKey
    public byte[] getApiv3AesKey(String mchid) {
        if (apiv3AesKeyMap.get(mchid) == null) {
            ZKPayMerchant merchant = merchantService.get(new ZKPayMerchant(mchid));
            if (merchant == null) {
                log.error("[>_<:20210220-1745-001] 商户号[{0}]未对接", mchid);
                throw ZKBusinessException.as("zk.wechat.000006", null, mchid);
            }
            apiv3AesKeyMap.put(mchid, merchant.getApiv3AesKey().getBytes());
        }
        return apiv3AesKeyMap.get(mchid);
    }

    public void cleanAllApiv3AesKey() {
        apiv3AesKeyMap.clear();
    }

    public void cleanApiv3AesKey(String mchid) {
        apiv3AesKeyMap.remove(mchid);
    }

    // 校验微信平台响应签名
    public boolean checkSign(String mchid, String wechatpaySerial, String wechatpaySignature, String wechatpayTimestamp,
            String wechatpayNonce,
            String bodyStr) throws Exception {
        ZKWXPlatformCert cert = null;
        if (ZKStringUtils.isEmpty(wechatpaySerial)) {
            cert = this.getPlatformCert(mchid);
        }
        else {
            cert = this.getPlatformCert(mchid, wechatpaySerial);
        }
        String signStr = ZKWXPayUtils.buildResponseSignStr(wechatpayTimestamp, wechatpayNonce, bodyStr);
        return ZKWXPayUtils.signVerifySHA256withRSA(wechatpaySignature, signStr.getBytes(), cert.getCert());
    }

}
