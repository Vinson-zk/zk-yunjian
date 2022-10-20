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
 * @Title: ZKCacheManager.java 
 * @author Vinson 
 * @Package com.zk.cache 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 8:58:02 AM 
 * @version V1.0   
*/
package com.zk.cache;

import java.util.Collection;

/** 
* @ClassName: ZKCacheManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKCacheManager<K> {

    /**
     * 取一个缓存
     * 
     * @param name
     * @return
     */
    public <V> ZKCache<K, V> getCache(String name);

    /**
     * 取一个，有设置有效时间的缓存
     * 
     * @param name
     * @param validTime，有效时长，0-不过期，单位为毫秒
     * @return
     */
    public <V> ZKCache<K, V> getCache(String name, long validTime);

    /**
     * 取当前所有缓存的名称
     * 
     * @return
     */
    public Collection<String> getCacheNames();

}
