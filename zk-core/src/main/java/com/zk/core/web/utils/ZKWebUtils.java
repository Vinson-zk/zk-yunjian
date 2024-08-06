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
 * @Title: ZKWebUtils.java 
 * @author Vinson 
 * @Package com.zk.webmvc.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:59:24 PM 
 * @version V1.0   
*/
package com.zk.core.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.reactive.ServerHttpRequest;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.resolver.ZKLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKWebUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebUtils extends org.springframework.web.util.WebUtils {

    protected static Logger log = LogManager.getLogger(ZKWebUtils.class);

    /**
     * 请求的语言参数标识
     */
    public static final String Locale_Flag_In_Header = "locale";

    public static ApplicationContext getAppCxt() {
        return ZKEnvironmentUtils.getApplicationContext();
    }

    /**
     * 判断自符串输出的 content type
     *
     * @Title: contentTypeRenderString
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 1:13:02 PM
     * @param str
     * @return
     * @return String
     */
    public static String contentTypeRenderString(String str) {
        if (ZKStringUtils.isEmpty(str)) {
            return null;
        }
        if ((ZKStringUtils.startsWith(str, "{") && ZKStringUtils.endsWith(str, "}"))
                || (ZKStringUtils.startsWith(str, "[") && ZKStringUtils.endsWith(str, "]"))) {
            return "application/json";
        }
        else if (ZKStringUtils.startsWith(str, "<") && ZKStringUtils.endsWith(str, ">")) {
            return "application/xml";
        }
        else {
            return "text/html";
        }
    }

//    public static String getCleanParam(ZKRequestWrapper request, String paramName) {
//        return ZKStringUtils.clean(request.getParameter(paramName));
//    }
//
//    public static boolean isTrue(ZKRequestWrapper request, String paramName) {
//        String value = getCleanParam(request, paramName);
//        return ZKUtils.isTrue(value);
//    }
    /* 2.国际化系列 *****************************************************************/

    /**
     * 取语言解析器
     *
     * @Title: getLocaleResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 17, 2021 12:07:59 AM
     * @return
     * @return LocaleResolver
     */
    public static ZKLocaleResolver getLocaleResolver() {
        return getLocaleResolver(ZKWebUtils.getAppCxt());
    }

    public static ZKLocaleResolver getLocaleResolver(ApplicationContext ctx) {
        if (ctx != null) {
            try {
                return ctx.getBean(ZKLocaleResolver.class);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Locale getLocale(HttpServletRequest hReq) {
        return ZKServletUtils.getLocale(hReq);
    }

    public static Locale getLocale(ServerHttpRequest req) {
        return ZKReactiveUtils.getLocale(req);
    }

    public static Locale getLocale() {
        if (ZKEnvironmentUtils.getWebApplicationType() == WebApplicationType.SERVLET) {
            return ZKServletUtils.getLocale();
        }
        else {
            if (ZKEnvironmentUtils.getWebApplicationType() == WebApplicationType.REACTIVE) {
                return ZKReactiveUtils.getLocale(null);
            }
        }
        return ZKLocaleUtils.getDefautLocale();
    }

    public static void setLocale(Locale locale) {
        if (ZKEnvironmentUtils.getWebApplicationType() == WebApplicationType.SERVLET) {
            ZKServletUtils.setLocale(locale);
        }
    }

    /* 1.httpApi 请求系列 *****************************************************************/
    /**
     * 读取一个 CloseableHttpResponse；读取到输出流中；注：此方法中不会将 CloseableHttpResponse
     * 进行关闭，需要方法外自行管理；
     *
     * @Title: readResponse
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 6, 2020 5:08:44 PM
     * @param response
     * @param os
     * @throws IOException
     * @return void
     */
    public static void readResponse(HttpResponse response, OutputStream os) throws IOException {
        if (response.getEntity() != null) {
            response.getEntity().writeTo(os);
        }
    }

    /**
     * 读取一个 CloseableHttpResponse；直接返回读取到的数组；注：此方法中不会将 CloseableHttpResponse
     * 进行关闭，需要方法外自行管理；
     *
     * @Title: readResponse
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 6, 2020 5:07:55 PM
     * @param response
     * @return
     * @return byte[]
     */
    public static byte[] readResponse(HttpResponse response) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (response.getEntity() != null) {
                response.getEntity().writeTo(baos);
            }
        }
        catch(Exception e) {
            log.error("[>_<:20200706-1706-001] 读取 CloseableHttpResponse 失败；errMsg: {}", e.getMessage());
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /* 00.其他 *****************************************************************/
    public static String normalize(String path) {
        return normalize(path, true);
    }

    private static String normalize(String path, boolean replaceBackSlash) {

        if (path == null)
            return null;

        // Create a place for the normalized path
        String normalized = path;

        if (replaceBackSlash && normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');

        if (normalized.equals("/."))
            return "/";

        // Add a leading "/" if necessary
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) + normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) + normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null); // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);

    }

}


