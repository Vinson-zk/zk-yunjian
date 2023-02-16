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
 * @Title: ZKDynamicDataSourceHelper.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:32:24 AM 
 * @version V1.0   
*/
package com.zk.db.dynamic;

/** 
* @ClassName: ZKDynamicDataSourceHelper 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDynamicDataSourceHelper {

    private static final ThreadLocal<ZKPatternType> holder = new ThreadLocal<ZKPatternType>();

    private ZKDynamicDataSourceHelper() {
        //
    }

    public static void putDataSource(ZKPatternType dataSourcePattern) {
        holder.set(dataSourcePattern);
    }

    public static ZKPatternType getDataSource() {
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }

}
