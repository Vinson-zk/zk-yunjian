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
* @Package com.zk.test.shiro.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:53:41 PM 
* @version V1.0 
*/
package com.zk.test.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.zk.core.exception.ZKCodeException;
import com.zk.core.exception.ZKMsgException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.ZKMsgRes;

/** 
* @ClassName: ZKShiroBaseFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKShiroBaseFilter extends AccessControlFilter {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {

        ZKMsgRes resMsg = null;
        try {
            return super.onPreHandle(request, response, mappedValue);
        }
        catch(ZKCodeException e) {
            resMsg = ZKMsgRes.as(e);

        }
        catch(ZKMsgException e) {
            resMsg = ZKMsgRes.as(e);

        }
        catch(Exception e) {
            resMsg = new ZKMsgRes();
            resMsg.setCode("zk.core.1");
            resMsg.setData(e.getMessage());
        }
        String resStr = ZKJsonUtils.writeObjectJson(resMsg);
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
