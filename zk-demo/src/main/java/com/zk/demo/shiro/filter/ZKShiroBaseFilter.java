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
* @Title: ZKShiroBaseFilter.java 
* @author Vinson 
* @Package com.zk.demo.shiro.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:53:41 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.filter;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.MediaType;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.core.web.utils.ZKWebUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/** 
* @ClassName: ZKShiroBaseFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKShiroBaseFilter extends AccessControlFilter {

    private Logger log = LogManager.getLogger(getClass());

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {

        ZKMsgRes resMsg = null;
        try {
            return super.onPreHandle(request, response, mappedValue);
        }
        catch(ZKBusinessException e) {
            resMsg = ZKMsgRes.as(ZKWebUtils.getLocale(ZKServletUtils.toHttp(request)), e);

        }
        catch(Exception e) {
            resMsg = ZKMsgRes.asSysErr(ZKWebUtils.getLocale(ZKServletUtils.toHttp(request)), e.getMessage());
        }
        String resStr = ZKJsonUtils.toJsonStr(resMsg);
        log.error("[>_<: 20171201-1229-001] err msg:{}", resStr);

        /* 使用response返回 */
//        response.setStatus(HttpStatus.OK.value()); // 设置状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
        response.setCharacterEncoding("UTF-8"); // 避免乱码
//        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.getWriter().write(resStr);
        return false;
    }

}
