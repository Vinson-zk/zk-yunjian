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
* @Title: ZKFileFluxProperties.java 
* @author Vinson 
* @Package com.zk.webflux.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 24, 2024 9:04:27 AM 
* @version V1.0 
*/
package com.zk.webflux.configuration;
/** 
* @ClassName: ZKFileFluxProperties 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileFluxProperties {

    // 允许的最大内存操作大小，单位KB；设置为-1时，不限制；默认-256K；
    private int maxInMemorySize = 256 * 1024;

    // 请求头大小，单位KB；
    private int maxHeadersSize = 10 * 1024;

    // 最大的磁盘空间；默认 -1，不限制；
    private long maxDiskUsagePerPart = -1;

    // 最大文件个数；默认 -1，不限制；
    private int maxParts = -1;

    /**
     * @return maxInMemorySize sa
     */
    public int getMaxInMemorySize() {
        return maxInMemorySize;
    }

    /**
     * @return maxHeadersSize sa
     */
    public int getMaxHeadersSize() {
        return maxHeadersSize;
    }

    /**
     * @return maxDiskUsagePerPart sa
     */
    public long getMaxDiskUsagePerPart() {
        return maxDiskUsagePerPart;
    }

    /**
     * @return maxParts sa
     */
    public int getMaxParts() {
        return maxParts;
    }

    /**
     * @param maxInMemorySize
     *            the maxInMemorySize to set
     */
    public void setMaxInMemorySize(int maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    /**
     * @param maxHeadersSize
     *            the maxHeadersSize to set
     */
    public void setMaxHeadersSize(int maxHeadersSize) {
        this.maxHeadersSize = maxHeadersSize;
    }

    /**
     * @param maxDiskUsagePerPart
     *            the maxDiskUsagePerPart to set
     */
    public void setMaxDiskUsagePerPart(long maxDiskUsagePerPart) {
        this.maxDiskUsagePerPart = maxDiskUsagePerPart;
    }

    /**
     * @param maxParts
     *            the maxParts to set
     */
    public void setMaxParts(int maxParts) {
        this.maxParts = maxParts;
    }

}
