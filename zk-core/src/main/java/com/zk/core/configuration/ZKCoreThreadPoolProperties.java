/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKCoreThreadPoolProperties.java 
* @author Vinson 
* @Package com.zk.core.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 9, 2025 4:35:12 PM 
* @version V1.0 
*/
package com.zk.core.configuration;

import java.util.concurrent.TimeUnit;

/** 
* @ClassName: ZKCoreThreadPoolProperties 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCoreThreadPoolProperties {

    /**
     * corePoolSize: 池内线程初始值与最小值，就算是空闲状态，也会保持该数量线程; 除非allowCoreThreadTimeOut设置为true
     */
    int corePoolSize = 2;

    /**
     * maximumPoolSize: 线程池允许的最大线程池数量，线程的增长始终不会超过该值。
     */
    int maximumPoolSize = 64;

    /**
     * keepAliveTime: 当池内线程数高于corePoolSize时，经过多少时间多余的空闲线程才会被回收。回收前处于wait状态
     */
    long keepAliveTime = 60l;

    /**
     * unit: 时间单位，可以使用TimeUnit的实例，如TimeUnit.MILLISECONDS
     */
    TimeUnit unit = TimeUnit.SECONDS;

    /**
     * workQueue: 工作队列，保存未执行的Runnable 任务
     */
    int workQueueCount = 64;

    /**
     * @return corePoolSize sa
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * @param corePoolSize
     *            the corePoolSize to set
     */
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * @return maximumPoolSize sa
     */
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * @param maximumPoolSize
     *            the maximumPoolSize to set
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    /**
     * @return keepAliveTime sa
     */
    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    /**
     * @param keepAliveTime
     *            the keepAliveTime to set
     */
    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    /**
     * @return unit sa
     */
    public TimeUnit getUnit() {
        return unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    /**
     * @return workQueueCount sa
     */
    public int getWorkQueueCount() {
        return workQueueCount;
    }

    /**
     * @param workQueueCount
     *            the workQueueCount to set
     */
    public void setWorkQueueCount(int workQueueCount) {
        this.workQueueCount = workQueueCount;
    }

}
