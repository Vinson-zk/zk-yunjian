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
* @Title: ZKLogAccessService.java 
* @author Vinson 
* @Package com.zk.log.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 8:31:12 AM 
* @version V1.0 
*/
package com.zk.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.ZKEnvironment;
import com.zk.core.commons.ZKPatternMatcher;
import com.zk.core.commons.ZKUrlPathMatcher;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.log.dao.ZKLogAccessDao;
import com.zk.log.entity.ZKLogAccess;

import jakarta.annotation.PostConstruct;

/** 
* @ClassName: ZKLogAccessService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKLogAccessService extends ZKBaseService<String, ZKLogAccess, ZKLogAccessDao> {

    @Autowired
    ZKEnvironment zkEnv;

    public ZKLogAccessService() {
//        this.initUriFilters();
    }

    private String[] uriFilters = null;

    private final ZKPatternMatcher pathMatcher = new ZKUrlPathMatcher();

    private final String SEPARATOR = ";";

    // 初始化日志记录的过虑 uri
    @PostConstruct
    private String[] initUriFilters() {
        if (uriFilters == null) {
            String uris = this.zkEnv.getString("zk.log.uri.filters");
            if (!ZKStringUtils.isEmpty(uris)) {
                uriFilters = uris.split(SEPARATOR);
                log.info("[^_^:20180509-0914-002] 需要过滤的请求URI有 {}", ZKJsonUtils.toJsonStr(uriFilters));
            }
        }
        return uriFilters;
    }

    /**
     * 判断日志是否要过滤; 返回 true-过虑；false-不过虑，需要记录日志；
     *
     * @Title: isFilters
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 14, 2022 8:59:17 AM
     * @param uri
     *            需要判断的请求地址 uri; ZKWebUtils.getPathWithinApplication(request)
     * @return boolean 返回 true-过虑，不记录日志；false-不过虑，需要记录日志；
     */
    public boolean isFilters(String uri) {
        if (this.uriFilters != null && this.uriFilters.length > 0) {
            for (String filterUrl : this.uriFilters) {
                if (pathMatcher.matches(filterUrl, uri)) {
                    log.info("[^_^:20220614-0900-001] Matched path pattern [{}] for requestURI [{}]. 请求URI过滤，不记录日志！",
                            filterUrl, uri);
                    return true;
                }
            }
        }
        return false;
    }



}
