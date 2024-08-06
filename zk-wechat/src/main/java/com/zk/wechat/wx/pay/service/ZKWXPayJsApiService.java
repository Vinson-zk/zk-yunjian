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
* @Title: ZKWXPayJsApiService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 9:25:04 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.pay.entity.ZKPayGetAmount;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.entity.ZKPayGetPayer;
import com.zk.wechat.pay.enumType.ZKPayGetChannel;
import com.zk.wechat.pay.enumType.ZKPayStatus;
import com.zk.wechat.pay.service.ZKPayGetOrderService;
import com.zk.wechat.pay.service.ZKPlatformCertService;
import com.zk.wechat.wx.pay.ZKWXPayConstants;
import com.zk.wechat.wx.pay.ZKWXPayUtils;
import com.zk.wechat.wx.pay.ZKWXV3ApiPayUtils;
import com.zk.wechat.wx.pay.entity.ZKWXApiCert;
import com.zk.wechat.wx.pay.entity.ZKWXJsPay;
import com.zk.wechat.wx.pay.entity.jsapi.ZKWXV3JsApiOrder;

/** 
* @ClassName: ZKWXPayJsApiService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKWXPayJsApiService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKPayGetOrderService payGetOrderService;

    @Autowired
    ZKPlatformCertService platformCertService;

    @Autowired
    ZKWXPayService wxPayService;

    @Value("${zk.wechat.wx.pay.valid.time:300}")
    private int payVaildTime;

    private int getPrepayIdValidTime() {
        return ZKEnvironmentUtils.getInteger("zk.wechat.wx.pay.prepayId.valid.time", 0);
    }

    // 制作支付过期时间
    private Date makeTimeExpire() {
        return ZKDateUtils.addDate(ZKDateUtils.getToday(), 0, 0, 0, 0, 0, payVaildTime);
    }

    /**
     * 判断预支付标识是否有效；
     *
     * @Title: checkPrepayIdValid
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 22, 2021 1:22:19 PM
     * @param payGetOrder
     * @return int 返回已生成时长，单位秒；大于 0 时，为预支付标识还有效；
     */
    public int checkPrepayIdValid(ZKPayGetOrder payGetOrder) {
        if (ZKPayStatus.NOTPAY == payGetOrder.getPayStatus()) {
            // 是在待支付中
            long time = ZKDateUtils.getToday().getTime() - payGetOrder.getPrepayIdDate().getTime();
            if (time < (this.getPrepayIdValidTime() * 1000l)) {
                return (int) (time / 1000);
            }
        }
        return -1;
    }

    // 校验支付订单支付是否过期; 大于 0 时，还未过期；
    public int checkPayValidTime(ZKPayGetOrder payGetOrder) {
        if (ZKPayStatus.CREATE == payGetOrder.getPayStatus() || ZKPayStatus.NOTPAY == payGetOrder.getPayStatus()) {
            // 是在待支付中
            long time = payGetOrder.getTimeExpire().getTime() - ZKDateUtils.getToday().getTime();
            return (int) (time / 1000);
        }
        return -1;
    }

    // 取支付通知地址
    public String getNotifyUrl(ZKPayGetChannel wxChannel, String payGroupCode, String businessCode) {
        return ZKEnvironmentUtils.getString("zk.wechat.wx.pay.transactions.notify_url");
    }

    // 统一下单
    public ZKPayGetOrder jsapi(String payGroupCode, String businessCode, String businessNo, String description,
            String attach, String goodsTag, String openid, int amountTotal, String amountCurrency)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {

        ZKPayGetOrder oldPayGetOrder = this.payGetOrderService.getByBusiness(businessCode, businessNo);

        if (oldPayGetOrder != null) {
            if (ZKPayStatus.CLOSED == oldPayGetOrder.getPayStatus()) {
                // 支付订单已关闭，先删除订单; 即：订单关闭后，这个业务单号可以继续支付
                this.payGetOrderService.diskDel(oldPayGetOrder);
            }
            else {
                // 支付订单已存在，不能重复支付
                log.error("[>_<:20210221-1049-001] 支付订单[{}-{}]已存在，请务重复支付", businessCode, businessNo);
                throw ZKBusinessException.as("zk.wechat.000016", null, businessNo);
            }
        }

        ZKPayGetOrder payGetOrder = new ZKPayGetOrder();
        ZKPayGetAmount payGetAmount = ZKPayGetAmount.as(amountTotal, amountCurrency);
        ZKPayGetPayer payGetPayer = ZKPayGetPayer.as(openid);

        // 一些非必须的 下单参数
        payGetOrder.setAttach(attach);
        payGetOrder.setGoodsTag(goodsTag);

        String notifyUrl = this.getNotifyUrl(ZKPayGetChannel.JSAPI, payGroupCode, businessCode);

        // 创建后台系统支付订单
        // 错误码为 zk.wechat.000016 时，为订单已存在，需要关闭后再下单；
        payGetOrder = payGetOrderService.create(ZKPayGetChannel.JSAPI, payGroupCode, businessCode, businessNo,
                description, this.makeTimeExpire(), notifyUrl, payGetOrder, payGetAmount, payGetPayer);
        payGetOrder = this.jsapiByZKPayGetOrder(payGetOrder);
        return payGetOrder;
    }

    // 根据支付ID，再次支付
    public ZKPayGetOrder jsapi(String payGetOrderId) {
        
        ZKPayGetOrder payGetOrder = this.payGetOrderService.getDetail(payGetOrderId);
        if(payGetOrder == null) {
            log.error("[>_<:20210222-1326-001] 支付订单[{}]不存在", payGetOrderId);
            throw ZKBusinessException.as("zk.wechat.000017", payGetOrderId);
        }
        // 如果支付成功，返回支付成功
        if (ZKPayStatus.SUCCESS == payGetOrder.getPayStatus()) {
            log.error("[>_<:20210222-1326-002] 订单[{0}]支付成功", payGetOrderId);
            throw ZKBusinessException.as("zk.wechat.000018", payGetOrder);
        }
        // 如果支付成功，返回支付失败
        if (ZKPayStatus.PAYERROR == payGetOrder.getPayStatus()) {
            log.error("[>_<:20210222-1326-003] 订单[{0}]支付失败", payGetOrderId);
            throw ZKBusinessException.as("zk.wechat.000019", payGetOrder);
        }
        // 如果订单关闭，返回支付关闭
        if (ZKPayStatus.CLOSED == payGetOrder.getPayStatus()) {
            log.error("[>_<:20210222-1326-004] 订单[{0}]已关闭支付", payGetOrderId);
            throw ZKBusinessException.as("zk.wechat.000020", payGetOrder);
        }
        // 支付中，如果支付标识已过期，重新下单
        if (this.checkPayValidTime(payGetOrder) < 1) {
            // 更新支付过期时间，重新向微信支付平台 下单；zk.wechat.000023=订单已过期，请重新支付
            log.info("[^_^:20210331-1438-001] 订单[{}]已过期，请重新支付。[{}]；}", payGetOrder.getPkId(),
                    ZKDateUtils.formatDate(payGetOrder.getTimeExpire(), ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ));
            this.payGetOrderService.updatePayStatus(payGetOrder.getPkId(), ZKPayStatus.CLOSED);
            throw ZKBusinessException.as("zk.wechat.000023", payGetOrder);
        }

        // 支付中，如果支付标识已过期，重新下单
        if (this.checkPrepayIdValid(payGetOrder) < 1) {
            payGetOrder = this.jsapiByZKPayGetOrder(payGetOrder);
        }
        return payGetOrder;
    }

    /**
     * 统一下单
     *
     * @Title: jsapi
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 21, 2021 12:16:23 AM
     * @param payGetOrder
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @return ZKPayGetOrder
     */
    public ZKPayGetOrder jsapiByZKPayGetOrder(ZKPayGetOrder payGetOrder) {
        ZKWXV3JsApiOrder jsApiOrder = new ZKWXV3JsApiOrder(payGetOrder);
        try {
            // 支付中，如果支付标识已过期，重新下单
            if (this.checkPayValidTime(payGetOrder) < 1) {
                // 更新支付过期时间，重新向微信支付平台 下单；zk.wechat.000023=订单已过期，请重新支付
                log.info("[^_^:20210331-1438-001] 订单[{}]已过期，请重新下单。[{}]；}", payGetOrder.getPkId(),
                        ZKDateUtils.formatDate(payGetOrder.getTimeExpire(), ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ));
                this.payGetOrderService.updatePayStatus(payGetOrder.getPkId(), ZKPayStatus.CLOSED);
                throw ZKBusinessException.as("zk.wechat.000023", payGetOrder);
            }
            // 取商户 api 证书
            ZKWXApiCert apiCert = wxPayService.getApiCertKey(payGetOrder.getMchid());
            // 调用微信支付平台统一下单
            String resStr = ZKWXV3ApiPayUtils.jsapi(null, jsApiOrder, apiCert.getPrivateKey(), apiCert.getSerialNo());
            JSONObject obj = ZKJsonUtils.parseJSONObject(resStr);
            // 更新预支付ID 到支付订单中
            payGetOrder.setPrepayId(obj.getString(ZKWXPayConstants.MsgAttr.prepayId));
            payGetOrderService.updatePrepay(payGetOrder.getPkId(), ZKPayStatus.NOTPAY, null, payGetOrder.getPrepayId());
            return payGetOrder;
        }
        catch(ZKCodeException e) {
            // 更新统一下时的应答错误码到 支付订单中
            log.error("[>_<:20210222-1411-002] 订单[{}]统一下单失败: {}", payGetOrder.getPkId(), e.getCode());
            // 下单失败，关闭订单
            payGetOrderService.updatePrepay(payGetOrder.getPkId(), ZKPayStatus.CLOSED, e.getCode(), null);
            throw e;
        }
        catch(Exception e) {
            // 更新统一下时的应答错误码到 支付订单中
            log.error("[>_<:20210222-1411-003] 订单[{}]统一下单失败: {}", payGetOrder.getPkId(), e.getMessage());
            // 下单失败，关闭订单
            payGetOrderService.updatePrepay(payGetOrder.getPkId(), ZKPayStatus.CLOSED, "-1", null);
            throw new RuntimeException("统一下单异常", e);
        }
    }

    // 生成 JsApi 调起支付参数对象
    public ZKWXJsPay genJsApiPayParams(ZKPayGetOrder payGetOrder)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
        Date date = ZKDateUtils.getToday();
        // 更新调起支付签名时间
        this.payGetOrderService.updatePaySignDate(payGetOrder.getPkId(), date);
        // 生成签名及调起支付信息；
        ZKWXJsPay jsPay = new ZKWXJsPay(payGetOrder.getAppid(), payGetOrder.getPrepayId(),
                String.valueOf(date.getTime() / 1000), wxPayService.getApiCertKey(payGetOrder.getMchid()));
        return jsPay;
    }

    // 关闭订单
    public ZKPayGetOrder close(String payGetOrderPkId)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
        ZKPayGetOrder payGetOrder = this.payGetOrderService.get(new ZKPayGetOrder(payGetOrderPkId));
        if (payGetOrder == null) {
            log.error("[>_<:20210223-2048-001] 支付订单[{}]不存在", payGetOrderPkId);
            throw ZKBusinessException.as("zk.wechat.000017", null, payGetOrderPkId);
        }
        return this.close(payGetOrder);
    }

    // 关闭订单
    public ZKPayGetOrder close(ZKPayGetOrder payGetOrder)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
        // 取商户 api 证书
        ZKWXApiCert apiCert = wxPayService.getApiCertKey(payGetOrder.getMchid());
        try {
            // 调用微信支付平台统一下单
            String resStr = ZKWXV3ApiPayUtils.close(null, payGetOrder.getMchid(), payGetOrder.getPkId(), apiCert.getPrivateKey(),
                    apiCert.getSerialNo());
            payGetOrderService.updatePrepay(payGetOrder.getPkId(), ZKPayStatus.CLOSED, resStr, null);
            return this.payGetOrderService.get(payGetOrder);
        }
        catch(ZKCodeException e) {
            // 更新统一下时的应答错误码到 支付订单中
            log.error("[>_<:20210222-1411-001] 订单[{}]关闭异常: {}", payGetOrder.getPkId(), e.getCode());
            payGetOrderService.updatePrepay(payGetOrder.getPkId(), payGetOrder.getPayStatus(), e.getCode(), null);
            throw e;
        }
    }

    // 下载微信平台证书
    public void dowloadPlatformCert(String mchid) throws Exception {
        // 取商户 api 证书
        ZKWXApiCert apiCert = wxPayService.getApiCertKey(mchid);
        String resStr = ZKWXV3ApiPayUtils.dowloadCerts(null, mchid, apiCert.getPrivateKey(), apiCert.getSerialNo());
        JSONObject resObj = ZKJsonUtils.parseJSONObject(resStr);

        JSONArray certArray = resObj.getJSONArray(ZKWXPayConstants.MsgAttr.data);
        JSONObject cert = null;

        // "serial_no": "5157F09EFDC096DE15EBE81A47057A7232F1B8E1",
        String serialNo = null;
        // "effective_time ": "2018-06-08T10:34:56+08:00",
        String effectiveTime = null;
        // "expire_time ": "2018-12-08T10:34:56+08:00",
        String expireTime = null;
        // "encrypt_certificate"
        JSONObject certObj = null;

        byte[] apiV3AesKey = wxPayService.getApiv3AesKey(mchid);
        String ciphertext = null;
        String associatedData = null;
        String nonce = null;
        byte[] certBytes = null;
        for (int i = 0; i < certArray.size(); ++i) {
            cert = certArray.getJSONObject(0);
            serialNo = cert.getString(ZKWXPayConstants.MsgAttr.CertKey.serialNo);
            effectiveTime = cert.getString(ZKWXPayConstants.MsgAttr.CertKey.effectiveTime);
            expireTime = cert.getString(ZKWXPayConstants.MsgAttr.CertKey.expireTime);
            certObj = cert.getJSONObject(ZKWXPayConstants.MsgAttr.CertKey.encryptCertificate);

            ciphertext = certObj.getString(ZKWXPayConstants.MsgAttr.EncKey.ciphertext);
            associatedData = certObj.getString(ZKWXPayConstants.MsgAttr.EncKey.associatedData);
            nonce = certObj.getString(ZKWXPayConstants.MsgAttr.EncKey.nonce);

            certBytes = ZKWXPayUtils.decrypt(ciphertext, apiV3AesKey, associatedData.getBytes(), nonce.getBytes());

            // 创建保存新下载的微信平台证书
            this.platformCertService.create(certBytes, mchid, serialNo, effectiveTime, expireTime);
        }
    }

}
