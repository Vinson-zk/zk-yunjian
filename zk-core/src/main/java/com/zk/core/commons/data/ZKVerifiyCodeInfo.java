/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKVerifiyCodeInfo.java 
* @author Vinson 
* @Package com.zk.sys.org.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 17, 2024 10:36:31 AM 
* @version V1.0 
*/
package com.zk.core.commons.data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEnvironmentUtils;

/** 
* @ClassName: ZKVerifiyCodeInfo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKVerifiyCodeInfo<T> implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    // 默认的有效时长; 默认为15分钟；单位 毫秒
    public static Long defaultValidityDuration = ZKEnvironmentUtils.getLong("zk.core.verifiy.code.validity.duration",
            15 * 60 * 1000l);

    String verifiyCode;

    T data;

    // 有效时长，单位为 毫秒
    long validityDuration = 0;

    Date date;

    private ZKVerifiyCodeInfo(String verifiyCode, T data, long validityDuration) {
        this.verifiyCode = verifiyCode;
        this.data = data;
        this.date = ZKDateUtils.getToday();
        this.validityDuration = validityDuration;
    }

    public static <T> ZKVerifiyCodeInfo<T> as(String verifiyCode, T data) {
        return as(verifiyCode, data, ZKVerifiyCodeInfo.defaultValidityDuration);
    }

    public static <T> ZKVerifiyCodeInfo<T> as(String verifiyCode, T data, long validityDuration) {
        return new ZKVerifiyCodeInfo<T>(verifiyCode, data, validityDuration);
    }

    /**
     * @return verifiyCode sa
     */
    public String getVerifiyCode() {
        return verifiyCode;
    }

    /**
     * @param verifiyCode
     *            the verifiyCode to set
     */
    public void setVerifiyCode(String verifiyCode) {
        this.verifiyCode = verifiyCode;
    }

    /**
     * @return data sa
     */
    public T getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return date sa
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return validityDuration sa
     */
    public long getValidityDuration() {
        return validityDuration;
    }

    /**
     * 是否有效
     *
     * @Title: isValidity
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 17, 2024 11:21:28 AM
     * @return boolean true-有效；false-无效；
     */
    public boolean isValidity() {
        if (System.currentTimeMillis() - this.date.getTime() < this.validityDuration) {
            return true;
        }
        return false;
    }

    /**
     * 校验验证码
     *
     * @Title: doVerifiyCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 23, 2024 6:02:13 PM
     * @param isAlmighty
     *            是否使用万能验证码？true-是；false-否；
     * @param verifiyCode
     * @return boolean
     */
    @JsonIgnore
    public boolean doVerifiyCode(boolean isAlmighty, String verifiyCode) {
        if (this.getVerifiyCode().equals(verifiyCode)) {
            return true;
        }
        if (isAlmighty && "9527".equals(verifiyCode)) {
            // 开发环境，9527 为万能验证码
            return true;
        }
        return false;
    }

}

