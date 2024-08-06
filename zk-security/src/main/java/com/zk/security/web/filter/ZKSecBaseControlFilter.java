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
* @Title: ZKSecBaseControlFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 5, 2021 12:52:35 PM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecBaseControlFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecBaseControlFilter extends ZKSecAccessControlFilter {

    @Override
    public boolean onPreHandle(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, Object mappedValue)
            throws Exception {

        return isAccessAllowed(zkReq, zkRes, mappedValue) || onAccessDenied(zkReq, zkRes, mappedValue);

//        ZKMsgRes resMsg = null;
//        try {
//            return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
//        }
//        catch(ZKCodeException e) {
//            resMsg = ZKMsgRes.as(e);
//            e.printStackTrace();
//        }
//        catch(ZKMsgException e) {
//            resMsg = ZKMsgRes.as(e);
//            e.printStackTrace();
//        }
//        catch(Exception e) {
//            resMsg = ZKMsgRes.as("zk.1", e.getMessage(), e);
//            e.printStackTrace();
//        }
//        String resStr = ZKJsonUtils.toJsonStr(resMsg);
//        logger.error("[>_<: 20171201-1229-001] err msg:{}", resStr);
//
////        /* 使用response返回 */ 使用异常处理拦截器统一返回
//////        response.setStatus(HttpStatus.OK.value()); // 设置状态码
////        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
////        response.setCharacterEncoding("UTF-8"); // 避免乱码
//////        response.setHeader("Cache-Control", "no-cache, must-revalidate");
////        response.getWriter().write(resStr);
//        return false;
    }

}
