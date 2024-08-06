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
* @Title: ZKConfigProperties.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2024 10:09:57 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.bean;
/** 
* @ClassName: ZKConfigProperties 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBeanProperties {

    String valueContextChild;

    String valueContext;

    String value;

    /**
     * @return valueContextChild sa
     */
    public String getValueContextChild() {
        return valueContextChild;
    }

    /**
     * @return valueContext sa
     */
    public String getValueContext() {
        return valueContext;
    }

    /**
     * @return value sa
     */
    public String getValue() {
        return value;
    }

    /**
     * @param valueContextChild
     *            the valueContextChild to set
     */
    public void setValueContextChild(String valueContextChild) {
        this.valueContextChild = valueContextChild;
    }

    /**
     * @param valueContext
     *            the valueContext to set
     */
    public void setValueContext(String valueContext) {
        this.valueContext = valueContext;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
