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
* @Title: ZKSecFilterChainManager.java 
* @author Vinson 
* @Package com.zk.security.web.filter.common
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 8:13:54 PM 
* @version V1.0 
*/
package com.zk.security.web.filter.common;

import java.util.Map;
import java.util.Set;

import com.zk.security.web.filter.ZKSecFilter;

/** 
* @ClassName: ZKSecFilterChainManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecFilterChainManager {

    /**
     * 添加拦截器
     * 
     * @param name
     *            拦截名称
     * @param filter
     *            拦截器
     */
    public void addFilter(String name, ZKSecFilter zkSecFilter);

    /**
     * 添加拦截器
     * 
     * @param name
     *            拦截名称
     * @param filter
     *            拦截器
     * @param init
     *            是否初始化 filterConfig，如果事，filterConfig 不能为空；默认为不初始化
     */
    public void addFilter(String name, ZKSecFilter zkSecFilter, boolean init);

    /**
     * 添加拦截器
     * 
     * @param name
     *            拦截名称
     * @param filter
     *            拦截器
     * @param init
     *            是否初始化 filterConfig，如果事，filterConfig 不能为空；
     * @param overwrite
     *            存在时，是否覆盖？true-覆盖; false-不覆盖；默认为覆盖
     */
    void addFilter(String name, ZKSecFilter zkSecFilter, boolean init, boolean overwrite);

    /**
     * 添加默认拦截器
     * 
     * @param init
     */
    void addDefaultFilters();

    /**
     * 
     * @param chainName
     * @param chainDefinition
     */
    void createChain(String urlPath, String chainDefinition);

    /**
     * 取一个路径下对应的过虑器
     * 
     * @param urlPath
     * @return
     */
    ZKSecFilterChain proxy(ZKSecFilterChain original, String urlPath);

    /**
     * 判断配置的过虑器是否为空，true-空；false-不空
     * 
     * @return
     */
    boolean hasChains();

    /**
     * 取所有过虑器路径
     * 
     * @return
     */
    Set<String> getChainNames();

    /**
     * 取拦截器 map
     * 
     * @return
     */
    Map<String, ZKSecFilter> getFilters();

}
