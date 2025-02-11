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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKUtils;
import com.zk.core.web.net.ZKGetIpDesc;
import com.zk.core.web.net.ZKIpDesc;
import com.zk.core.web.net.IpDesc.ZKGetIpDescIpApiImpl;
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
                return ZKReactiveUtils.getLocale();
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
    
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = ZKServletUtils.getRemoteAddr(request);
        return cleanInetAddressByRemoteAddr(remoteAddr);
    }

    public static String getRemoteAddr(ServerHttpRequest request) {
        String remoteAddr = ZKReactiveUtils.getRemoteAddr(request);
        return cleanInetAddressByRemoteAddr(remoteAddr);
    }

    public static String cleanInetAddressByRemoteAddr(String ip) {
        // 本机访问
        if ("localhost".equalsIgnoreCase(ip) || "127.0.0.1".equalsIgnoreCase(ip)
                || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            }
            catch(UnknownHostException e) {
                log.error("[>_<:20250208-1706-001] 读取本机IP失败；errMsg: {}", e.getMessage());
                e.printStackTrace();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ip && ip.length() > 15) {
            if (ip.indexOf(",") > 15) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    protected static ZKGetIpDesc getIpDesc = null;

    protected static ZKGetIpDesc getGetIpDescImpl() {
        if (getIpDesc == null) {
            getIpDesc = new ZKGetIpDescIpApiImpl();
        }
        return getIpDesc;
    }

    public static void setGetIpDescImpl(ZKGetIpDesc getIpDescImpl) {
        getIpDesc = getIpDescImpl;
    }

    // 取IP归属地
    public static ZKIpDesc getIpDesc(String ip) {
        return getGetIpDescImpl().getIpDesc(ip, getLocale());
    }

    // 取参数 =================================================
    // HttpServletRequest -----------------
    public static Boolean getBooleanParameter(HttpServletRequest hReq, String parmaName, Boolean defaultValue) {
        if (defaultValue == null) {
            try {
                return ServletRequestUtils.getBooleanParameter(hReq, parmaName);
            }
            catch(ServletRequestBindingException e) {
                e.printStackTrace();
                return null;
            }
         }
        return ServletRequestUtils.getBooleanParameter(hReq, parmaName, defaultValue);
    }

    public static String getStringParameter(HttpServletRequest hReq, String parmaName, String defaultValue) {
        if (defaultValue == null) {
            try {
                return ServletRequestUtils.getStringParameter(hReq, parmaName);
            }
            catch(ServletRequestBindingException e) {
                e.printStackTrace();
                return null;
            }
         }
        return ServletRequestUtils.getStringParameter(hReq, parmaName, defaultValue);
    }

    public static Integer getIntParameter(HttpServletRequest hReq, String parmaName, Integer defaultValue) {
        if (defaultValue == null) {
            try {
                return ServletRequestUtils.getIntParameter(hReq, parmaName);
            }
            catch(ServletRequestBindingException e) {
                e.printStackTrace();
                return null;
            }
         }
        return ServletRequestUtils.getIntParameter(hReq, parmaName, defaultValue);
    }

    public static Long getLongParameter(HttpServletRequest hReq, String parmaName, Long defaultValue) {
        if (defaultValue == null) {
            try {
                return ServletRequestUtils.getLongParameter(hReq, parmaName);
            }
            catch(ServletRequestBindingException e) {
                e.printStackTrace();
                return null;
            }
         }
        return ServletRequestUtils.getLongParameter(hReq, parmaName, defaultValue);
    }

    public static Float getFloatParameter(HttpServletRequest hReq, String parmaName, Float defaultValue) {
        if (defaultValue == null) {
            try {
                return ServletRequestUtils.getFloatParameter(hReq, parmaName);
            }
            catch(ServletRequestBindingException e) {
                e.printStackTrace();
                return null;
            }
         }
        return ServletRequestUtils.getFloatParameter(hReq, parmaName, defaultValue);
    }

    public static Double getDoubleParameter(HttpServletRequest hReq, String parmaName, Double defaultValue) {
        if (defaultValue == null) {
            try {
                return ServletRequestUtils.getDoubleParameter(hReq, parmaName);
            }
            catch(ServletRequestBindingException e) {
                e.printStackTrace();
                return null;
            }
         }
        return ServletRequestUtils.getDoubleParameter(hReq, parmaName, defaultValue);
    }

    // ---
    public static boolean[] getBooleanParameters(HttpServletRequest hReq, String parmaName) {
        return ServletRequestUtils.getBooleanParameters(hReq, parmaName);
    }

    public static String[] getStringParameters(HttpServletRequest hReq, String parmaName) {
        return ServletRequestUtils.getStringParameters(hReq, parmaName);
    }

    public static int[] getIntParameters(HttpServletRequest hReq, String parmaName) {
        return ServletRequestUtils.getIntParameters(hReq, parmaName);
    }

    public static long[] getLongParameters(HttpServletRequest hReq, String parmaName) {
        return ServletRequestUtils.getLongParameters(hReq, parmaName);
    }

    public static float[] getFloatParameters(HttpServletRequest hReq, String parmaName) {
        return ServletRequestUtils.getFloatParameters(hReq, parmaName);
    }

    public static double[] getDoubleParameters(HttpServletRequest hReq, String parmaName) {
        return ServletRequestUtils.getDoubleParameters(hReq, parmaName);
    }

    // ServerHttpRequest -----------------
    public static Boolean getBooleanParameter(ServerHttpRequest hReq, String parmaName, Boolean defaultValue) {
        String v = hReq.getQueryParams().getFirst(parmaName);
        if (v == null) {
            return defaultValue;
        }
        else {
            return ZKUtils.isTrue(v);
        }
    }

    public static String getStringParameter(ServerHttpRequest hReq, String parmaName, String defaultValue) {
        String v = hReq.getQueryParams().getFirst(parmaName);
        if (v == null) {
            return defaultValue;
        }
        else {
            return v;
        }
    }

    public static Integer getIntParameter(ServerHttpRequest hReq, String parmaName, Integer defaultValue) {
        String v = hReq.getQueryParams().getFirst(parmaName);
        if (v == null) {
            return defaultValue;
        }
        else {
            return Integer.parseInt(v);
        }
    }

    public static Long getLongParameter(ServerHttpRequest hReq, String parmaName, Long defaultValue) {
        String v = hReq.getQueryParams().getFirst(parmaName);
        if (v == null) {
            return defaultValue;
        }
        else {
            return Long.parseLong(v);
        }
    }

    public static Float getFloatParameter(ServerHttpRequest hReq, String parmaName, Float defaultValue) {
        String v = hReq.getQueryParams().getFirst(parmaName);
        if (v == null) {
            return defaultValue;
        }
        else {
            return Float.parseFloat(v);
        }
    }

    public static Double getDoubleParameter(ServerHttpRequest hReq, String parmaName, Double defaultValue) {
        String v = hReq.getQueryParams().getFirst(parmaName);
        if (v == null) {
            return defaultValue;
        }
        else {
            return Double.parseDouble(v);
        }
     }

     // ---
     public static List<Boolean> getBooleanParameters(ServerHttpRequest hReq, String parmaName) {
         List<String> vs = hReq.getQueryParams().get(parmaName);
         List<Boolean> rs = new ArrayList<>();
         for (String v : vs) {
             rs.add(ZKUtils.isTrue(v));
         }
         return rs;
     }

     public static List<String> getStringParameters(ServerHttpRequest hReq, String parmaName) {
         return hReq.getQueryParams().get(parmaName);
     }

     public static List<Integer> getIntParameters(ServerHttpRequest hReq, String parmaName) {
         List<String> vs = hReq.getQueryParams().get(parmaName);
         List<Integer> rs = new ArrayList<>();
         for (String v : vs) {
             rs.add(Integer.parseInt(v));
         }
         return rs;
     }

     public static List<Long> getLongParameters(ServerHttpRequest hReq, String parmaName) {
         List<String> vs = hReq.getQueryParams().get(parmaName);
         List<Long> rs = new ArrayList<>();
         for (String v : vs) {
             rs.add(Long.parseLong(v));
         }
         return rs;
     }

     public static List<Float> getFloatParameters(ServerHttpRequest hReq, String parmaName) {
         List<String> vs = hReq.getQueryParams().get(parmaName);
         List<Float> rs = new ArrayList<>();
         for (String v : vs) {
             rs.add(Float.parseFloat(v));
         }
         return rs;
     }

     public static List<Double> getDoubleParameters(ServerHttpRequest hReq, String parmaName) {
         List<String> vs = hReq.getQueryParams().get(parmaName);
         List<Double> rs = new ArrayList<>();
         for (String v : vs) {
             rs.add(Double.parseDouble(v));
         }
         return rs;
     }

    // =================================================
    public static void test() {

    }

}


