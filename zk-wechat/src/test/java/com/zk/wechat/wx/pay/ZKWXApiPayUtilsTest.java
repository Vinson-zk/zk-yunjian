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
* @Title: ZKWXApiPayUtilsTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 23, 2021 11:31:28 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay;

import java.security.PrivateKey;

import org.junit.Test;

import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKIdUtils;
import com.zk.wechat.pay.entity.ZKPayGetAmount;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.enumType.ZKPayCurrency;
import com.zk.wechat.wx.pay.entity.nativeapi.ZKWXV3PartnerNativeApiOrder;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXApiPayUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXApiPayUtilsTest {

    @Test
    public void testDowloadCerts() {
        try {
            String resStr;
            String certUrl = "https://api.mch.weixin.qq.com/v3/certificates";
            String merchantId, apiCertSerialNo, pathCertKey;
//            String pathCertP12, pathApiCert;

            merchantId = "1600315970";
//          apiCertKey = "c8417b30fe294565a8caa57d7b053c1a";
            apiCertSerialNo = "49D449B97E493D95D6A11FED5B6B1715746CB6B0";
//            pathCertP12 = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_cert.p12";
//            pathApiCert = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_cert.pem";
            pathCertKey = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_key.pem";
            
//            X509Certificate x509cert;
//            PublicKey publicKey = null;
            PrivateKey privateKey = null;
            
            /////
            privateKey = ZKWXPayUtils.getRSAPrivateKey(pathCertKey);
            
            resStr = ZKWXV3ApiPayUtils.dowloadCerts(certUrl, merchantId, privateKey, apiCertSerialNo);
            System.out.println(resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testPartnerNativeapi(){
        /**
         * appId:
         * 建采网科技	服务号: wx54d979ff9836e8a1
         */
        /**
         * merchantId:
         * 建采网科技 服务商: 1600315970
         * 珠海市建筑业协会 商户号: 1633190975
         *
         */
        /**
         * API-证书：
         * 建采网科技 服务商: 7E68EBDA80D3458F3388FD48878B91EC1E26AB38 有效	 2022/10/27至2027/10/26 0
         */
        merchantId:
        try{
            String nativeApiUrl = "https://api.mch.weixin.qq.com/v3/pay/partner/transactions/native";
            ZKWXV3PartnerNativeApiOrder partnerNativeApiOrder;
            PrivateKey privateKey;

            String sp_merchantId, sp_appId, sub_merchantId, sub_appId = null;
            String pathCertKey, certMyPrivateSerialNo;

            ZKPayGetOrder payGetOrder;
            ZKPayGetAmount getAmount;
            String resStr;

            certMyPrivateSerialNo = "7E68EBDA80D3458F3388FD48878B91EC1E26AB38";
            pathCertKey = "/Users/bs/bs_cert/1600315970_20221027_cert/apiclient_key.pem";
            privateKey = ZKWXPayUtils.getRSAPrivateKey(pathCertKey);

            sp_merchantId = "1600315970";
            sub_merchantId = "1633190975";

            sp_appId = "wx54d979ff9836e8a1";
            sub_appId = "wxbd854fedeb2c8913";

            getAmount = new ZKPayGetAmount();
            getAmount.setTotal(1);
            getAmount.setCurrency(ZKPayCurrency.CNY);
            payGetOrder = new ZKPayGetOrder();
            payGetOrder.setTimeExpire(ZKDateUtils.addSeconds(ZKDateUtils.getToday(), 60*30));
            payGetOrder.setPayGetAmount(getAmount);
            payGetOrder.setDescriptionRename("Vinson.test");
            payGetOrder.setPkId(ZKIdUtils.genId().toString()); // 创建ID，ID 作用于商户订单号 out_trade_no
            payGetOrder.setNotifyUrl("https://www.weixin.qq.com/wxpay/pay.php"); // https://www.jiancaiyi.com
            partnerNativeApiOrder = ZKWXV3PartnerNativeApiOrder.asByZKPayGetOrder(payGetOrder);
            partnerNativeApiOrder.setSp_appid(sp_appId);
            partnerNativeApiOrder.setSp_machid(sp_merchantId);
            partnerNativeApiOrder.setSub_appid(sub_appId);
            partnerNativeApiOrder.setSub_machid(sub_merchantId);

            resStr = ZKWXV3ApiPayUtils.partnerNativeapi(nativeApiUrl, partnerNativeApiOrder, privateKey, certMyPrivateSerialNo);
            System.out.println("[^_^:20221027-1123-001] resStr: " + resStr);

        }catch (Exception e){
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }



}
