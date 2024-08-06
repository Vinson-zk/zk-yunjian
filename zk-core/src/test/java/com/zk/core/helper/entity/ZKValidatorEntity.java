/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKValidatorEntity.java 
 * @author Vinson 
 * @Package com.zk.core.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:00:59 PM 
 * @version V1.0   
*/
package com.zk.core.helper.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.zk.core.commons.ZKValidationGroup;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

/** 
* @ClassName: ZKValidatorEntity 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKValidatorEntity {

    @NotNull
    private String pNotNull;

    @NotNull(message = "{test.data.v.msg.notNull}", groups = { Default.class, ZKValidationGroup.Insert.class })
    private String pNotNullLocaleMsg;

    @Length(min = 2, max = 6, message = "{test.data.v.msg.length}", groups = { Default.class,
            ZKValidationGroup.Update.class })
    private String pLength;

    @Range(min = 3, max = 7, message = "{test.data.v.msg.range}", groups = { Default.class,
            ZKValidationGroup.Insert.class })
    private int pRange;

    @Max(value = 6, message = "{test.data.v.msg.max}", groups = { Default.class, ZKValidationGroup.Update.class })
    private int pMax;

    @Min(value = 2, message = "{test.data.v.msg.min}", groups = { Default.class, ZKValidationGroup.Update.class })
    private int pMin;

    @Pattern(regexp = "^[a-zA-Z0-9_\\.-][a-zA-Z0-9_\\.-]{1,6}$", message = "{test.data.v.msg.pattern}")
    private String pPattern;

    /**
     * @return pNotNull
     */
    public String getpNotNull() {
        return pNotNull;
    }

    /**
     * @param pNotNull
     *            the pNotNull to set
     */
    public void setpNotNull(String pNotNull) {
        this.pNotNull = pNotNull;
    }

    /**
     * @return pNotNullLocaleMsg
     */
    public String getpNotNullLocaleMsg() {
        return pNotNullLocaleMsg;
    }

    /**
     * @param pNotNullLocaleMsg
     *            the pNotNullLocaleMsg to set
     */
    public void setpNotNullLocaleMsg(String pNotNullLocaleMsg) {
        this.pNotNullLocaleMsg = pNotNullLocaleMsg;
    }

    /**
     * @return pLength
     */
    public String getpLength() {
        return pLength;
    }

    /**
     * @param pLength
     *            the pLength to set
     */
    public void setpLength(String pLength) {
        this.pLength = pLength;
    }

    /**
     * @return pRang
     */
    public int getpRange() {
        return pRange;
    }

    /**
     * @param pRang
     *            the pRang to set
     */
    public void setpRange(int pRange) {
        this.pRange = pRange;
    }

    /**
     * @return pMax
     */
    public int getpMax() {
        return pMax;
    }

    /**
     * @param pMax
     *            the pMax to set
     */
    public void setpMax(int pMax) {
        this.pMax = pMax;
    }

    /**
     * @return pMin
     */
    public int getpMin() {
        return pMin;
    }

    /**
     * @param pMin
     *            the pMin to set
     */
    public void setpMin(int pMin) {
        this.pMin = pMin;
    }

    /**
     * @return pPattern sa
     */
    public String getpPattern() {
        return pPattern;
    }

    /**
     * @param pPattern
     *            the pPattern to set
     */
    public void setpPattern(String pPattern) {
        this.pPattern = pPattern;
    }

}
