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
 * @Title: ZKWebmvcTestEntity.java 
 * @author Vinson 
 * @Package com.zk.core.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 17, 2019 11:03:43 AM 
 * @version V1.0   
*/
package com.zk.webmvc.helper.entity;

/**
 * @ClassName: ZKWebmvcTestEntity
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKWebmvcTestEntity {

    private String name;

    private int age;

    private String remark;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
