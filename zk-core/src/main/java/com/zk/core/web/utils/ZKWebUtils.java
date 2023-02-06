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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.zk.core.commons.ZKCoreConstants;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKUtils;
import com.zk.core.web.resolver.ZKLocaleResolver;

/** 
* @ClassName: ZKWebUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebUtils extends org.springframework.web.util.WebUtils {

    protected static Logger log = LoggerFactory.getLogger(ZKWebUtils.class);

    /**
     * 请求的语言参数标识
     */
    public static final String Locale_Flag_In_Header = "locale";

    public static ApplicationContext getAppCxt() {
        return ZKEnvironmentUtils.getApplicationContext();
    }

    /**
     * 取当前请求的 HttpServletRequest
     *
     * @Title: getRequest
     * @author Vinson
     * @date Mar 16, 2019 7:33:56 AM
     * @return
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    /**
     * 取当前请求的 HttpServletResponse
     *
     * @Title: getResponse
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 2, 2019 9:23:50 AM
     * @return
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getResponse();
        }

        return null;
    }

    public static String getCleanParam(ServletRequest request, String paramName) {
        return ZKStringUtils.clean(request.getParameter(paramName));
    }

    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    /**
     * 支持AJAX的页面跳转
     */
    public static void redirectUrl(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            if (ZKWebUtils.isAjaxRequest(request)) {
                request.getRequestDispatcher(url).forward(request, response); // AJAX不支持Redirect改用Forward
            }
            else {
                response.sendRedirect(request.getContextPath() + url);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是Ajax异步请求
     * 
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }

        String uri = request.getRequestURI();
        if (ZKStringUtils.endsWithIgnoreCase(uri, ".json") || ZKStringUtils.endsWithIgnoreCase(uri, ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (ZKStringUtils.inStringIgnoreCase(ajax, "json", "xml")) {
            return true;
        }

        return false;
    }

    /**
     * 将对象转换为JSON、XML、JSONP字符串渲染到客户端（JsonP，请求参数加：__callback=回调函数名）
     * 
     * @param request
     *            请求对象，用来得到输出格式的指令：JSON、XML、JSONP
     * @param response
     *            渲染对象
     * @param object
     *            待转换JSON并渲染的对象
     * @return null
     */
    public static String renderObject(HttpServletResponse hRes, Object object) {

        HttpServletRequest hReq = getRequest();
        String uri = hReq.getRequestURI();
        if (ZKStringUtils.endsWithIgnoreCase(uri, ".xml")
                || ZKStringUtils.equalsIgnoreCase(hReq.getParameter("__ajax"), "xml")) {
//            return renderString(hRes, ZKXmlMapper.toXml(object));
            return renderString(hRes, ZKJsonUtils.writeObjectJson(object), null);
        }
        else {
//            String functionName = hReq.getParameter("__callback");
//            if (ZKStringUtils.isNotBlank(functionName)){
//                return renderString(hRes, ZKJsonUtils.writeObjectJson(new JSONObject(functionName, object)));
//            }else{
//                return renderString(hRes, ZKJsonUtils.writeObjectJson(object));
//            }
            return renderString(hRes, ZKJsonUtils.writeObjectJson(object), null);
        }
    }

    /**
     * 将字符串渲染到客户端
     * 
     * @param response
     *            渲染对象
     * @param string
     *            待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse hRes, String str, String contentType) {
//          response.reset(); // 注释掉，否则以前设置的Header会被清理掉，如ajax登录设置记住我的Cookie信息
        if (contentType == null) {
            if ((ZKStringUtils.startsWith(str, "{") && ZKStringUtils.endsWith(str, "}"))
                    || (ZKStringUtils.startsWith(str, "[") && ZKStringUtils.endsWith(str, "]"))) {
                contentType = "application/json";
            }
            else if (ZKStringUtils.startsWith(str, "<") && ZKStringUtils.endsWith(str, ">")) {
                contentType = "application/xml";
            }
            else {
                contentType = "text/html";
            }
        }
        hRes.setContentType(contentType);
//        hRes.setCharacterEncoding("utf-8");
        OutputStream os = null;
        try {
            os = hRes.getOutputStream();
            os.write(str.getBytes());
            os.flush();
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            ZKStreamUtils.closeStream(os);
        }

        return null;
    }

    /**
     * 判断请求中的参数是否为 true: "true", "t", "1", "yes", "enabled", "y", "yes", "on" 为
     * true;
     *
     * @Title: isTrue
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 3, 2019 5:36:56 PM
     * @param request
     * @param paramName
     * @return
     * @return boolean
     */
    public static boolean isTrue(ServletRequest request, String paramName) {
        String value = getCleanParam(request, paramName);
        return ZKUtils.isTrue(value);
    }

    /******************************************************************/
    /**
     * 添加请求标记
     * 
     * @param hReq
     * @return 请求标记
     * @throws NoSuchAlgorithmException
     */
    public static String setRequestSign(HttpServletRequest hReq) throws NoSuchAlgorithmException {
        String sign = ZKUtils.genRequestSign(); // 创建 请求标记
        // 在服务器使用 session 保存 请求标记
        hReq.getSession().setAttribute(ZKCoreConstants.Global.requestSign, sign);
        return sign;
    }

    /**
     * 验证请求标记，判断上送请求标记与服务端生成的请求标记是否一致。
     * 
     * @param hReq
     * @return true 用户重复提交了表单 false 用户没有重复提交表单
     */
    public static boolean isRepeatSubmit(HttpServletRequest hReq) {
        String requestSign = ZKWebUtils.getCleanParam(hReq, ZKCoreConstants.Global.requestSign);
        // 1、如果用户提交的表单数据中没有 请求标记，则用户是重复提交了表单
        if (requestSign == null) {
//            hReq.setAttribute(ZKCoreConstants.Global.isRepeatSubmit, "true");
            return true;
        }
        // 取出存储在 Session 中的请求标记 requestSign
        String sessionRequestSign = (String) hReq.getSession().getAttribute(ZKCoreConstants.Global.requestSign);
        // 2、如果当前用户的 Session 中不存在请求标记，则用户是重复提交了表单
        if (sessionRequestSign == null) {
//            hReq.setAttribute(ZKCoreConstants.Global.isRepeatSubmit, "true");
            return true;
        }
        // 3、存储在 Session 中的请求标记 与表单提交中的请求标记 不同，则用户是重复提交了表单
        if (!requestSign.equals(sessionRequestSign)) {
//            hReq.setAttribute(ZKCoreConstants.Global.isRepeatSubmit, "true");
            return true;
        }
        // 清除请求标记
        cleanRequestSign(hReq);
        return false;
    }

    /**
     * 判断是否是强制提交
     *
     * @Title: isForceSubmit
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 27, 2019 11:24:57 AM
     * @param hReq
     * @return
     * @return boid
     */
    public static boolean isForceSubmit(HttpServletRequest hReq) {
        return ZKWebUtils.isTrue(hReq, ZKCoreConstants.Global.isForceSubmit);
    }

    /**
     * 移除 session 中的 请求标记
     *
     * @Title: cleanRequestSign
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 27, 2019 11:06:35 AM
     * @param hReq
     * @return void
     */
    public static void cleanRequestSign(HttpServletRequest hReq) {
        // 移除 session 中的 请求标记
        hReq.getSession().removeAttribute(ZKCoreConstants.Global.requestSign);
    }

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
    
    /**
     * 从 request 中取字节参数。
    *
    * @Title: getBytesByRequest 
    * @Description: TODO(simple description this method what to do.) 
    * @author Vinson 
    * @date Feb 10, 2021 3:27:33 PM 
    * @param req
    * @return
    * @return byte[]
     */
    public static byte[] getBytesByRequest(HttpServletRequest req) {
        InputStream is = null;
        ByteArrayOutputStream baOs = null;
        try {
            is = req.getInputStream();
            baOs = new ByteArrayOutputStream();
            ZKStreamUtils.readAndWrite(is, baOs);
            return baOs.toByteArray();
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180905-0821-001] 读请 HttpServletRequest 输入流失败！");
        } finally {
            if (is != null) {
                ZKStreamUtils.closeStream(is);
            }
            if (baOs != null) {
                ZKStreamUtils.closeStream(baOs);
            }
        }
        return null;
    }

    public static HttpServletResponse downloadFile(HttpServletResponse hRes, String filePath) throws IOException {
        return downloadFile(hRes, new File(filePath));
    }

    /**
     * 下载文件
     * 
     * @param file
     * @param response
     * @return
     * @throws IOException
     */
    public static HttpServletResponse downloadFile(HttpServletResponse hRes, File file) throws IOException {
        if (file == null) {
            log.error("[>_<:20210417-1612-001] 下载文件为 null");
        }

        File deleteFile = null;
        if (file.isDirectory()) {
            // 文件是一个目录，先压缩，再下载，下载后，删除压缩文件；
            log.info("[^_^:20210417-1612-002] 文件是个目录，先压缩，再下载；文件目录是:{}", file.getPath());
            file = ZKFileUtils.compress(file);
            deleteFile = file;
        }

        OutputStream toClient = null;
        try {
            // 清空response
            hRes.reset();
            hRes.setStatus(200);

            hRes.setHeader("Pragma", "No-cache");
            hRes.setHeader("Cache-Control", "No-cache");
            hRes.setDateHeader("Expires", 0);

            // 以流的形式下载文件。
            hRes.setContentType("application/octet-stream");
            // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            hRes.setHeader("Content-Disposition",
                    "attachment;filename=" + ZKEncodingUtils.urlEncode(file.getName(), "UTF-8"));

            toClient = new BufferedOutputStream(hRes.getOutputStream());
            ZKFileUtils.readFile(file, toClient);
            toClient.flush();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            if (deleteFile != null) {
                ZKFileUtils.deleteFile(deleteFile);
            }
            ZKStreamUtils.closeStream(toClient);
        }
        return hRes;
    }

//    public static void issueRedirect(ServletRequest request, ServletResponse response, String url) throws IOException {
////        response.send
////        request.getRequestDispatcher(url).
////        request.getRequestDispatcher(url).forward(request, response);
////        issueRedirect(request, response, url, null, true, true);
//    }

    /**
     * 从请求中取请求的 uri
     * 
     * @Title: getPathWithinApplication
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 27, 2021 8:20:35 PM
     * @param http
     * @return
     * @return String
     */
    public static String getPathWithinApplication(HttpServletRequest hReq) {
        String contextPath = getContextPath(hReq);
        String requestUri = getRequestUri(hReq);
        if (ZKStringUtils.startsWithIgnoreCase(requestUri, contextPath)) {
            // Normal case: URI contains context path.
            String path = requestUri.substring(contextPath.length());
            return (ZKStringUtils.hasText(path) ? path : "/");
        }
        else {
            // Special case: rather unusual.
            return requestUri;
        }
    }

    public static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return normalize(decodeAndCleanUriString(request, uri));
    }

    private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
        uri = decodeRequestString(request, uri);
        int semicolonIndex = uri.indexOf(';');
        return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
    }

    public static String getContextPath(HttpServletRequest request) {
        String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        contextPath = normalize(decodeRequestString(request, contextPath));
        if ("/".equals(contextPath)) {
            // the normalize method will return a "/" and includes on Jetty,
            // will also be a "/".
            contextPath = "";
        }
        return contextPath;
    }

    @SuppressWarnings({"deprecation"})
    public static String decodeRequestString(HttpServletRequest request, String source) {
        String enc = determineEncoding(request);
        try {
            return URLDecoder.decode(source, enc);
        }
        catch(UnsupportedEncodingException ex) {
            if (log.isWarnEnabled()) {
                log.warn("Could not decode request string [" + source + "] with encoding '" + enc
                        + "': falling back to platform default encoding; exception message: " + ex.getMessage());
            }
            return URLDecoder.decode(source);
        }
    }

    protected static String determineEncoding(HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = DEFAULT_CHARACTER_ENCODING;
        }
        return enc;
    }

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

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return ZKStringUtils.isNotBlank(remoteAddr) ? remoteAddr : request.getRemoteAddr();
    }

    // ===============================================

    /**
     * 
     *
     * @Title: getLocaleResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 17, 2021 12:07:59 AM
     * @return
     * @return LocaleResolver
     */
    public static ZKLocaleResolver getLocaleResolver() {
        return getLocaleResolver(getAppCxt(), null);
    }

    public static ZKLocaleResolver getLocaleResolver(ApplicationContext ctx) {
        return getLocaleResolver(ctx, null);
    }

    /**
     * 取语言解析器
     * 
     * 1、如果有 ApplicationContext 优先从 ApplicationContext 中取语言适配器；
     * 
     * 2、如果没有取到语言适配器, 则通过请求 Request 决定语言适配器；如果 Request 不存在，取系统 Request；
     *
     * @Title: getLocaleResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 2, 2019 9:43:16 AM
     * @param ctx
     * @param hReq
     * @return
     * @return LocaleResolver
     */
    public static ZKLocaleResolver getLocaleResolver(ApplicationContext ctx, HttpServletRequest hReq) {
        ZKLocaleResolver localeResolver = null;
        if (ctx != null) {
            try {
                localeResolver = ctx.getBean(ZKLocaleResolver.class);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
//        if (localeResolver == null) {
//            localeResolver = getLocaleResolverByRequest(hReq);
//        }
        return localeResolver;
    }

//    /**
//     * 通过 Request 请求取语言解析器；
//     *
//     * @Title: getLocaleResolver
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 31, 2019 5:02:59 PM
//     * @param hReq
//     *            hReq 为 null 时，返回 null
//     * @return
//     * @return LocaleResolver
//     */
//    public static ZKLocaleResolver getLocaleResolverByRequest(HttpServletRequest hReq) {
//        if (hReq == null) {
//            hReq = getRequest();
//        }
//        if (hReq != null) {
//            return RequestContextUtils.getLocaleResolver(hReq);
//        }
//        return null;
//    }

    /**
     * 设置整个系统的默认语言
     *
     * @Title: setSystemLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 14, 2019 5:19:02 PM
     * @param locale
     * @return void
     */
    public static void setLocale(Locale locale) {
        setLocale(locale, getLocaleResolver(getAppCxt(), getRequest()), getRequest(), getResponse());
    }

    /**
     * 设置国际化语言到 国际化语言适配器；
     * 
     * 如果 localLocaleResolver 为null，则调用 Locale.setDefault(locale); 设置系统本地语言。
     *
     * @Title: setLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 14, 2019 10:55:37 AM
     * @param locale
     * @param ctx
     * @param req
     * @param rep
     * @return void
     */
    public static void setLocale(Locale locale, ZKLocaleResolver localLocaleResolver, HttpServletRequest hReq,
            HttpServletResponse hRes) {

        if (localLocaleResolver != null && hRes != null) {
            try {
                localLocaleResolver.setLocale(hReq, hRes, locale);
            }
            catch(Exception e) {
                log.error("[>_<:20190314-1056-002] 设置语言到国际化语言适配器失败：locale:[{}]", locale);
                e.printStackTrace();
            }
        }
        else {
            ZKLocaleUtils.setLocale(locale);
        }
    }

    /**
     * 取当前国际批语言；不建议使用；建议使用 getLocale(HttpServletRequest httpServletRequest)
     *
     * @Title: getLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 14, 2019 11:20:07 AM
     * @return
     * @return Locale
     */
    public static Locale getLocale() {
        return getLocale(getRequest());
    }

    /**
     * 根据 httpServletRequest 取当前语言
     *
     * @Title: getLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 14, 2019 10:47:48 AM
     * @param httpServletRequest
     * @return
     * @return Locale
     */
    public static Locale getLocale(HttpServletRequest httpServletRequest) {
        return getLocale(httpServletRequest, null);
    }

    /**
     * 取国际化语言；
     * 
     * 1、优先从 HttpServletRequest 取国际化语言；
     * 
     * 2、如果没有取到国际化语言，但有语言适配器，优先从语言适配器中取国际化语言；
     * 
     * 3、如果以上都没取到国际化语言，则取 系统本地语言
     *
     * @Title: getLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 2, 2019 9:35:48 AM
     * @param localeResolver
     * @param hReq
     * @return
     * @return Locale
     */
    public static Locale getLocale(HttpServletRequest hReq, ZKLocaleResolver localeResolver) {

        Locale locale = null;
        if (hReq != null) {
            // 优先从 HttpServletRequest 请求中取国际化语言参数；
            locale = getLocaleByRequest(hReq);
        }

        if (locale == null) {
            if (hReq == null) {
                hReq = getRequest();
            }
            if (localeResolver == null) {
                localeResolver = getLocaleResolver(getAppCxt(), hReq);
            }
            if (localeResolver != null) {
                locale = getLocaleByLocaleResolver(localeResolver, hReq);
            }
        }

        if (locale == null) {
            return ZKLocaleUtils.getDefautLocale();
        }

        return locale;
    }

    /**
     * 通过语言适配器取国际化语言；
     *
     * @Title: getLocaleByLocaleResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 17, 2021 12:03:37 AM
     * @param localeResolver
     * @param hReq
     * @return
     * @return Locale
     */
    public static Locale getLocaleByLocaleResolver(ZKLocaleResolver localeResolver, HttpServletRequest hReq) {
        if (localeResolver != null) {
            if (hReq == null) {
                return localeResolver.getDefaultLocale();
            }
            else {
                // 如果有语言适配器，优先从语言适配器中取国际化语言；
                return localeResolver.resolveLocale(hReq);
            }
        }
        return null;
    }

    /**
     * 从 request 请求头中，通过参数取语言
     */
    public static Locale getLocaleByRequest(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String localeStr = request.getHeader(Locale_Flag_In_Header);
        localeStr = localeStr == null ? "" : localeStr.replace("-", "_");
        if (ZKStringUtils.isEmpty(localeStr)) {
            return null;
        }
        return ZKLocaleUtils.distributeLocale(localeStr);
    }

}
