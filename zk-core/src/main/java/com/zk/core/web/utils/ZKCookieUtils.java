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
 * @Title: ZKCookieUtils.java 
 * @author Vinson 
 * @Package com.zk.webmvc.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:21:36 PM 
 * @version V1.0   
*/
package com.zk.core.web.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.cookie.ZKCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKCookieUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCookieUtils {

    protected final static Logger log = LogManager.getLogger(ZKCookieUtils.class);

    // cookie 在响应头中的名称
    public static final String COOKIE_HEADER_NAME = "Set-Cookie";

    public static final String SET_COOKIE = "Set-Cookie";

    public static final String COOKIE = "Cookie";

    public static final String PATH_ATTRIBUTE_NAME = "Path";

    public static final String EXPIRES_ATTRIBUTE_NAME = "Expires";

    public static final String MAXAGE_ATTRIBUTE_NAME = "Max-Age";

    public static final String DOMAIN_ATTRIBUTE_NAME = "Domain";

    public static final String VERSION_ATTRIBUTE_NAME = "Version";

    public static final String COMMENT_ATTRIBUTE_NAME = "Comment";

    public static final String SECURE_ATTRIBUTE_NAME = "Secure";

    public static final String HTTP_ONLY_ATTRIBUTE_NAME = "HttpOnly";

    // 默认有效时间，一天
    public static final int DEFAULT_VALID_TIME = 60 * 60 * 24;

    //
    public static final String ROOT_PATH = "/";

    //
    protected static final String GMT_TIME_ZONE_ID = "GMT";

    //
    protected static final String COOKIE_DATE_FORMAT_STRING = "EEE, dd-MMM-yyyy HH:mm:ss z";

    /**
     * 创建一个 cookie，除以下参数处，其他参数自行设置；
     * 
     * @param name
     *            cookie 名称；
     * @param value
     *            cookie 值
     * @param path
     *            路径
     * @return
     */
    public static Cookie createCookie(String cookieName, String value, String path) {
        return createCookie(cookieName, value, path, DEFAULT_VALID_TIME, true);
    }

    public static Cookie createCookie(String cookieName, String value, String path, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        if (path == null) {
            cookie.setPath(ROOT_PATH);
        }
        else {
            cookie.setPath(path);
        }

        return cookie;
    }

    /**
     * 设置一个 cookie 到响应中
     * 
     * @param cookie
     * @param response
     */
    public static void setCookie(Cookie cookie, HttpServletResponse response) {
        if (response == null || cookie == null) {
            return;
        }
        response.addCookie(cookie);
    }

    /**
     * 删除一个 cookie
     * 
     * @param response
     * @param cookie
     *            要删除的 Cookie
     */
    public static void removeCookie(Cookie cookie, HttpServletResponse response) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            setCookie(cookie, response);
        }
    }

    public static void saveToHeader(HttpServletResponse response, Cookie cookie) {
        setCookie(cookie, response);
    }

    /**
     * 删除一个 cookie
     * 
     * @param response
     * @param cookie
     *            要删除的 Cookie
     * @param value
     *            删除时修改的值，为 null 时，不修改
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
            String value) {
        Cookie cookie = createCookie(cookieName, value, null);
        cookie.setMaxAge(0);
        removeCookie(cookie, response);
    }

    /**
     * 将 cookie 字符串，转换为 解析为 cookie
     *
     * @Title: parseCookieStrs
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 20, 2019 10:27:09 AM
     * @param cookieList
     * @return
     * @return List<Cookie>
     */
    public static Cookie[] parseCookieStrs(Collection<String> cookieList) {
        List<Cookie> cookies = new ArrayList<>();
        for (String cookieStr : cookieList) {
            String[] cookieItemStr = cookieStr.split(";");
            Cookie cookie = null;
            for (String itemStr : cookieItemStr) {
                if (cookie == null) {
                    cookie = new Cookie(itemStr.substring(0, cookieStr.indexOf("=")).trim(),
                            itemStr.substring(itemStr.indexOf("=") + 1).trim());
                }
                else {
                    if (itemStr.indexOf("=") == -1) {
                        if (ZKCookieUtils.HTTP_ONLY_ATTRIBUTE_NAME.equals(cookieStr)) {
                            cookie.setHttpOnly(true);
                        }
                    }
                    else {
                        String n = itemStr.substring(0, itemStr.indexOf("=")).trim();
                        String v = itemStr.substring(itemStr.indexOf("=") + 1).trim();
                        if (ZKCookieUtils.HTTP_ONLY_ATTRIBUTE_NAME.equals(n)) {
                            if ("true".equals(v)) {
                                cookie.setHttpOnly(true);
                            }
                            else {
                                cookie.setHttpOnly(false);
                            }
                        }
                        else if (ZKCookieUtils.MAXAGE_ATTRIBUTE_NAME.equals(n)) {
                            cookie.setMaxAge(Integer.valueOf(v));
                        }
                        else if (ZKCookieUtils.PATH_ATTRIBUTE_NAME.equals(n)) {
                            cookie.setPath(v);
                        }
                    }
                }
            }
            cookies.add(cookie);
        }
        return cookies.toArray(new Cookie[cookies.size()]);
    }

    /**
     * cookies 转为字符串 List
     *
     * @Title: formatCookies
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 20, 2019 10:35:01 AM
     * @param cookies
     * @return
     * @return List<String>
     */
    public static List<String> formatCookies(Collection<Cookie> cookies) {
        List<String> cookieStrList = new ArrayList<>();
        if (cookies == null || cookies.size() == 0) {
            return cookieStrList;
        }
        for (Cookie cookie : cookies) {
            cookieStrList.add(formatCookie(cookie));
        }
        return cookieStrList;
    }

    public static String formatCookie(Cookie cookie) {
        return String.format("%s=%s", cookie.getName(), cookie.getValue());
    }

    public static String formatCookie(ZKCookie cookie) {
        return String.format("%s=%s", cookie.getName(), cookie.getValue());
    }

    /**
     * 取一个 cookie
     *
     * @Title: getCookie
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 20, 2019 10:36:27 AM
     * @param cookies
     * @param cookieName
     * @return
     * @return Cookie
     */
    public static Cookie getCookie(Cookie[] cookies, String cookieName, String validUri) {
//        Cookie cookie = null;
        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName)) {
                // 判断 cookie 有效路径
                if (ZKStringUtils.clean(c.getPath()) == null
                        || validUri == null
                        || pathMatches(ZKStringUtils.clean(c.getPath()), validUri)) {
                    if (c.getMaxAge() != 0) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public static List<Cookie> getCookies(Cookie[] cookies, String cookieName, String validUri) {
        List<Cookie> cs = new ArrayList<>();
        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName)) {
                // 判断 cookie 有效路径
                if (ZKStringUtils.clean(c.getPath()) == null || validUri == null
                        || pathMatches(ZKStringUtils.clean(c.getPath()), validUri)) {
                    cs.add(c);
                }
            }
        }
        return cs;
    }

//    /**
//     * 设置 Cookie（生成时间为1天）
//     * 
//     * @param name
//     *            名称
//     * @param value
//     *            值
//     */
//    public static void setCookie(HttpServletResponse response, String name, String value) {
//        setCookie(response, name, value, 60 * 60 * 24);
//    }
//
//    /**
//     * 设置 Cookie
//     * 
//     * @param name
//     *            名称
//     * @param value
//     *            值
//     * @param maxAge
//     *            生存时间（单位秒）
//     * @param uri
//     *            路径
//     */
//    public static void setCookie(HttpServletResponse response, String name, String value, String path) {
//        setCookie(response, name, value, path, 60 * 60 * 24);
//    }
//
//    /**
//     * 设置 Cookie
//     * 
//     * @param name
//     *            名称
//     * @param value
//     *            值
//     * @param maxAge
//     *            生存时间（单位秒）
//     * @param uri
//     *            路径
//     */
//    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
//        setCookie(response, name, value, "/", maxAge);
//    }
//
//    /**
//     * 设置 Cookie
//     * 
//     * @param name
//     *            名称
//     * @param value
//     *            值
//     * @param maxAge
//     *            生存时间（单位秒）
//     * @param uri
//     *            路径
//     */
//    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setPath(path);
//        cookie.setMaxAge(maxAge);
//        // 设置 cookie 作用域
////       cookie.setDomain(path);
//        try {
//            cookie.setValue(URLEncoder.encode(value, "utf-8"));
//            setCookie(cookie, response);
//        }
//        catch(UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void getCookieValue(String cookieName) {
//        // 先从响应中取；
//        // 响应中未取到，再从请求中取；
//    }

    /**
     * Check whether the given {@code cookiePath} matches the
     * {@code requestPath}
     *
     * @param cookiePath
     * @param requestPath
     * @return
     * @see <a href="https://tools.ietf.org/html/rfc6265#section-5.1.4">RFC
     *      6265, Section 5.1.4 "Paths and Path-Match"</a>
     */
    protected static boolean pathMatches(String cookiePath, String requestPath) {
        if (!requestPath.startsWith(cookiePath)) {
            return false;
        }

        return requestPath.length() == cookiePath.length() || cookiePath.charAt(cookiePath.length() - 1) == '/'
                || requestPath.charAt(cookiePath.length()) == '/';
    }

    /**
     * Formats a date into a cookie date compatible string (Netscape's
     * specification).
     *
     * @param date
     *            the date to format
     * @return an HTTP 1.0/1.1 Cookie compatible date string (GMT-based).
     */
    protected static String toCookieDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone(GMT_TIME_ZONE_ID);
        DateFormat fmt = new SimpleDateFormat(COOKIE_DATE_FORMAT_STRING, Locale.US);
        fmt.setTimeZone(tz);
        return fmt.format(date);
    }

    /**
     * Returns the Cookie's calculated path setting. If the
     * {@link javax.servlet.http.Cookie#getPath() path} is {@code null}, then
     * the {@code request}'s
     * {@link javax.servlet.http.HttpServletRequest#getContextPath() context
     * path} will be returned. If getContextPath() is the empty string or null
     * then the ROOT_PATH constant is returned.
     *
     * @param request
     *            the incoming HttpServletRequest
     * @return the path to be used as the path when the cookie is created or
     *         removed
     */
    public static String calculatePath(HttpServletRequest request, String path) {
        // String path = StringUtils.clean(getPath());
        if (!ZKStringUtils.hasText(path)) {
            path = ZKStringUtils.clean(request.getContextPath());
        }

        if (path == null) {
            path = ROOT_PATH;
        }
        return path;
    }

    public static HttpCookie getCookie(ServerHttpRequest request, String cookieName) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (cookies != null) {
            return cookies.getFirst(cookieName);
        }
        return null;
    }

}
