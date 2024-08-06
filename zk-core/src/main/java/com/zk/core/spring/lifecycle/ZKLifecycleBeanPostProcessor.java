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
* @Title: ZKLifecycleBeanPostProcessor.java 
* @author Vinson 
* @Package com.zk.core.spring.lifecycle 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 6, 2021 2:22:00 PM 
* @version V1.0 
*/
package com.zk.core.spring.lifecycle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.PriorityOrdered;

/** 
* @ClassName: ZKLifecycleBeanPostProcessor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLifecycleBeanPostProcessor implements DestructionAwareBeanPostProcessor, PriorityOrdered {

    /**
     * Private internal class log instance.
     */
    private static final Logger log = LogManager.getLogger(ZKLifecycleBeanPostProcessor.class);

    /**
     * Order value of this BeanPostProcessor.
     */
    private int order;

    /**
     * Default Constructor.
     */
    public ZKLifecycleBeanPostProcessor() {
        this(LOWEST_PRECEDENCE);
    }

    /**
     * Constructor with definable {@link #getOrder() order value}.
     *
     * @param order
     *            order value of this BeanPostProcessor.
     */
    public ZKLifecycleBeanPostProcessor(int order) {
        this.order = order;
    }

    /**
     * Calls the <tt>init()</tt> methods on the bean if it implements
     * {@link org.apache.shiro.util.ZKInitializable}
     *
     * @param object
     *            the object being initialized.
     * @param name
     *            the name of the bean being initialized.
     * @return the initialized bean.
     * @throws BeansException
     *             if any exception is thrown during initialization.
     */
    public Object postProcessBeforeInitialization(Object object, String name) throws BeansException {
        if (object instanceof ZKInitializable) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Initializing bean [" + name + "]...");
                }

                ((ZKInitializable) object).init();
            }
            catch(Exception e) {
                throw new FatalBeanException("Error initializing bean [" + name + "]", e);
            }
        }
        return object;
    }

    /**
     * Does nothing - merely returns the object argument immediately.
     */
    public Object postProcessAfterInitialization(Object object, String name) throws BeansException {
        // Does nothing after initialization
        return object;
    }

    /**
     * Calls the <tt>destroy()</tt> methods on the bean if it implements
     * {@link org.apache.shiro.util.ZKDestroyable}
     *
     * @param object
     *            the object being initialized.
     * @param name
     *            the name of the bean being initialized.
     * @throws BeansException
     *             if any exception is thrown during initialization.
     */
    public void postProcessBeforeDestruction(Object object, String name) throws BeansException {
        if (object instanceof ZKDestroyable) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Destroying bean [" + name + "]...");
                }

                ((ZKDestroyable) object).destroy();
            }
            catch(Exception e) {
                throw new FatalBeanException("Error destroying bean [" + name + "]", e);
            }
        }
    }

    /**
     * Order value of this BeanPostProcessor.
     *
     * @return order value.
     */
    public int getOrder() {
        // LifecycleBeanPostProcessor needs Order. See
        // https://issues.apache.org/jira/browse/SHIRO-222
        return order;
    }

    /**
     * Return true only if <code>bean</code> implements ZKDestroyable.
     * 
     * @param bean
     *            bean to check if requires destruction.
     * @return true only if <code>bean</code> implements ZKDestroyable.
     * @since 1.4
     */
    public boolean requiresDestruction(Object bean) {
        return (bean instanceof ZKDestroyable);
    }

}
