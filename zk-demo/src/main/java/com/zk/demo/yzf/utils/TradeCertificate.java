/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2018 All Rights Reserved.
 */
package com.zk.demo.yzf.utils;

/**
 * DESC:交易证书，含商户公钥，商户私钥，翼支付公钥
 * Author: yangbo2018
 * Date:   20/8/11
 * Time:   10:35
 */

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author yangbo2018
 * @version Id: TradeCertificate.java, v 0.1 2018/2/28 11:10 yangbo2018 Exp $$
 */
public class TradeCertificate {
    private PrivateKey merchantPrivateKey;
    private PublicKey bestpayPublicKey;
    private String iv;

    public PrivateKey getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(PrivateKey merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public PublicKey getBestpayPublicKey() {
        return bestpayPublicKey;
    }

    public void setBestpayPublicKey(PublicKey bestpayPublicKey) {
        this.bestpayPublicKey = bestpayPublicKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public TradeCertificate(PrivateKey merchantPrivateKey, PublicKey bestpayPublicKey, String iv) {
        this.merchantPrivateKey = merchantPrivateKey;
        this.bestpayPublicKey = bestpayPublicKey;
        this.iv=iv;
    }
}
