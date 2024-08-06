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
 * @Title: ZKDelegatingFilterProxyRegistrationBean.java 
 * @author Vinson 
 * @Package com.zk.core.web.support.servlet.filterr 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:15:36 PM 
 * @version V1.0   
*/
package com.zk.core.web.support.servlet.filter;

import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

/** 
* @ClassName: ZKDelegatingFilterProxyRegistrationBean 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDelegatingFilterProxyRegistrationBean extends DelegatingFilterProxyRegistrationBean {

    public ZKDelegatingFilterProxyRegistrationBean(String targetBeanName,
            ServletRegistrationBean<?>... servletRegistrationBeans) {
        super(targetBeanName, servletRegistrationBeans);
    }

    private ApplicationContext applicationContext;

    private WebApplicationContext getWebApplicationContext() {
        Assert.notNull(this.applicationContext, "ApplicationContext be injected");
        Assert.isInstanceOf(WebApplicationContext.class, this.applicationContext);
        return (WebApplicationContext) this.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private DelegatingFilterProxy dfp = null;

    @Override
    public DelegatingFilterProxy getFilter() {
        if (dfp == null) {
            dfp = new DelegatingFilterProxy(this.getTargetBeanName(), getWebApplicationContext());
            dfp.setTargetFilterLifecycle(true);
        }
        return dfp;
    }

}
