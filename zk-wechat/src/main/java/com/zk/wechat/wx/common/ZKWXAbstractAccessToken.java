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
* @Title: ZKWXAbstractAccessToken.java 
* @author Vinson 
* @Package com.zk.wechat.wx.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 5, 2021 8:19:41 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.common;

import java.io.Serializable;
import java.util.Date;

/** 
* @ClassName: ZKWXAbstractAccessToken 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKWXAbstractAccessToken implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 凭证 token
     */
    private String accessToken;

    /**
     * 有效时间，单位秒
     */
    private int expiresIn;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 凭证 token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 凭证 token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        this.lastUpdate();
    }

    /**
     * 有效时间，单位秒
     */
    public int getExpiresIn() {
        return expiresIn;
    }

    /**
     * 有效时间，单位秒
     */
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * 判断是否过期
     *
     * @Title: isExpires
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月7日 下午2:50:02
     * @return boolean true-过期；false-不过期
     */
    public boolean isExpires() {
        if (this.updateDate == null || System.currentTimeMillis()
                - this.updateDate.getTime() > ((this.getExpiresIn() - getOffsetExpiresTime()) * 1000)) {
            // 过期了，过期可自定义测偏移量，单位为秒
            return true;
        }
        return false;
    }

    /**
     * 更新时间
     */
    public void lastUpdate() {
        this.updateDate = new Date();
    }

    /**
     * 过期判断时，偏移量，单位秒；正数为提前多少秒；负数为延迟多少秒;
     *
     * @Title: getOffsetExpiresTime
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月13日 下午12:32:56
     * @return
     * @return int
     */
    protected abstract int getOffsetExpiresTime();

}
