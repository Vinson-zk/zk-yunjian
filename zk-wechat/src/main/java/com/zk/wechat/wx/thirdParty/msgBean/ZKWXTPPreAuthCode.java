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
* @Title: ZKWXTPPreAuthCode.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.msgBean 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 5, 2021 10:45:02 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.msgBean;

import java.io.Serializable;
import java.util.Date;

import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.wechat.wx.common.ZKWXConstants;

/** 
* @ClassName: ZKWXTPPreAuthCode 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXTPPreAuthCode implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 5922134691555810909L;

    public ZKWXTPPreAuthCode(ZKSysOrgCompany company, String thirdPartyAppid, String preAuthCode, int expiresIn) {
        this.identification = makeIdentification(thirdPartyAppid, preAuthCode);
        this.company = company;
        this.thirdPartyAppid = thirdPartyAppid;
        this.preAuthCode = preAuthCode;
        this.expiresIn = expiresIn;
        this.updateDate = new Date();
    }

    /**
     * 标识；[第三方平台 APPID][分隔符 ZKWXConstants.cacheKeySeparator][预授权码]
     */
    private String identification;

    /**
     * 第三方 appId
     */
    private String thirdPartyAppid;

    /**
     * 预授权码
     */
    private String preAuthCode;

    /**
     * 有效时间，单位秒
     */
    private int expiresIn;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 公司信息
     */
    private ZKSysOrgCompany company;

    /**
     * @return company sa
     */
    public ZKSysOrgCompany getCompany() {
        return company;
    }

    /**
     * @return identification sa
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * @return thirdPartyAppid sa
     */
    public String getThirdPartyAppid() {
        return thirdPartyAppid;
    }


    /**
     * @return updateDate sa
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 预授权码
     */
    public String getPreAuthCode() {
        return preAuthCode;
    }

    /**
     * 有效时间，单位秒
     */
    public int getExpiresIn() {
        return expiresIn;
    }

    /**
     * 判断是否过期
     *
     * @Title: isExpires
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月7日 下午2:50:02
     * @return
     * @return boolean true-过期；false-不过期
     */
    public boolean isExpires() {
        if (this.getUpdateDate() == null || System.currentTimeMillis()
                - this.getUpdateDate().getTime() > (this.getExpiresIn() * 1000 - 100000)) {
            // 过期了，过期难测偏移量为 100 秒
            return true;
        }
        return false;
    }

    /**
     * 制作 预授权码 的缓存ID
     * 
     * [第三方平台 APPID][KEY 组合时 分隔符 ZKWXConstants.cacheKeySeparator][预授权码]
     *
     * @Title: makeIdentification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 5, 2021 11:27:58 AM
     * @param thirdPartyAppid
     * @param preAuthCode
     * @return
     * @return String
     */
    public static String makeIdentification(String thirdPartyAppid, String preAuthCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(thirdPartyAppid);
        sb.append(ZKWXConstants.cacheKeySeparator);
        sb.append(preAuthCode);
        return sb.toString();
    }

}
