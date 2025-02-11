/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKServletUtils.java 
* @author Vinson 
* @Package com.zk.core.web.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 18, 2024 12:52:41 AM 
* @version V1.0 
*/
package com.zk.core.web.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.zk.core.commons.ZKCoreConstants;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKUtils;
import com.zk.core.web.resolver.ZKLocaleResolver;
import com.zk.core.web.support.servlet.resolver.ZKServletLocaleResolver;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @ClassName: ZKServletUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKServletUtils {

    protected static Logger log = LogManager.getLogger(ZKServletUtils.class);

    public static interface ZKReqHeaderKey {

        public static final String AddrXForwardedIP = "X-Forwarded-For";

        public static final String AddrProxyClientIP = "Proxy-Client-IP";

        public static final String AddrWLProxyClientIP = "WL-Proxy-Client-IP";

        public static final String AddrHttpClientIp = "HTTP_CLIENT_IP";

        public static final String AddrHttpXForwardedFor = "HTTP_X_FORWARDED_FOR";

        public static final String AddrXRealIP = "X-Real-IP";

        public static final String AddrHost = "Host";

        public static final String UserAgent = "user-agent";
    }

    /* 8.请求基础系列 *****************************************************************/
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

    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    /* 7.参数系列 *****************************************************************/
    /**
     * 
     *
     * @Title: getCleanParam
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 2:22:12 PM
     * @param request
     * @param paramName
     * @return
     * @return String
     */
    public static String getCleanParam(ServletRequest request, String paramName) {
        return ZKStringUtils.clean(request.getParameter(paramName));
    }

    /**
     * 判断请求中的参数是否为 true: "true", "t", "1", "yes", "enabled", "y", "yes", "on" 为 true;
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
        return ZKServletUtils.isTrue(hReq, ZKCoreConstants.Global.isForceSubmit);
    }

    /* 6.请求响应读写系列 *****************************************************************/
    /**
     * 从 request 中取字节参数。
     *
     * @Title: getBytesByRequest
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 10, 2021 3:27:33 PM
     * @param req
     * @return byte[] getBytesByRequest
     */
    public static byte[] read(HttpServletRequest req) {
        InputStream is = null;
        ByteArrayOutputStream baOs = null;
        try {
            is = req.getInputStream();
            baOs = new ByteArrayOutputStream();
            ZKServletUtils.read(req, baOs);
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

    public static void read(HttpServletRequest req, OutputStream os) {
        InputStream is = null;
        try {
            is = req.getInputStream();
            ZKStreamUtils.readAndWrite(is, os);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180905-0821-001] 读请 HttpServletRequest 输入流失败！");
        } finally {
            if (is != null) {
                ZKStreamUtils.closeStream(is);
            }
        }
    }

    /**
     * 向响应中写字符串
     *
     * @Title: write
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 2:22:22 PM
     * @param hRes
     * @param content
     * @return void
     */
    public static void write(HttpServletResponse hRes, String content) {
        PrintWriter pw = null;
        try {
            pw = hRes.getWriter();
            pw.write(content);
        }
        catch(IOException e) {
            log.error("[>_<:20190902-1556-001] Response 通信异常！");
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * 向响应中写 字节
     *
     * @Title: write
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 2:25:05 PM
     * @param hRes
     * @param content
     * @return void
     */
    public static void write(HttpServletResponse hRes, byte[] content) {
        OutputStream os = null;
        try {
            os = hRes.getOutputStream();
            os.write(content);
        }
        catch(IOException e) {
            log.error("[>_<:20190902-1556-011] Response 通信异常！");
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            ZKStreamUtils.closeStream(os);
        }
    }

    /**
     * 从一个输入流中写出响应中
     *
     * @Title: write
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 3:18:59 PM
     * @param hRes
     * @param is
     * @return void
     */
    public static void write(HttpServletResponse hRes, InputStream is) {
        OutputStream os = null;
        try {
            os = hRes.getOutputStream();
            ZKStreamUtils.readAndWrite(is, os);
        }
        catch(IOException e) {
            log.error("[>_<:20190902-1556-011] Response 通信异常！");
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            ZKStreamUtils.closeStream(os);
        }
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
    public static void renderObject(HttpServletResponse hRes, Object object) {

        HttpServletRequest hReq = ZKServletUtils.getRequest();
        String uri = hReq.getRequestURI();
        if (ZKStringUtils.endsWithIgnoreCase(uri, ".xml")
                || ZKStringUtils.equalsIgnoreCase(hReq.getParameter("__ajax"), "xml")) {
//            return renderString(hRes, ZKXmlMapper.toXml(object));
            renderString(hRes, ZKJsonUtils.toJsonStr(object), null);
        }
        else {
//            String functionName = hReq.getParameter("__callback");
//            if (ZKStringUtils.isNotBlank(functionName)){
//                return renderString(hRes, ZKJsonUtils.toJsonStr(new JSONObject(functionName, object)));
//            }else{
//                return renderString(hRes, ZKJsonUtils.toJsonStr(object));
//            }
            renderString(hRes, ZKJsonUtils.toJsonStr(object), null);
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
    public static void renderString(HttpServletResponse hRes, String str, String contentType) {
//          response.reset(); // 注释掉，否则以前设置的Header会被清理掉，如ajax登录设置记住我的Cookie信息
        if (contentType == null) {
            contentType = ZKWebUtils.contentTypeRenderString(str);
        }
        hRes.setContentType(contentType);
//        hRes.setCharacterEncoding("utf-8");
        ZKServletUtils.write(hRes, str);
    }

    public static HttpServletResponse downloadFile(HttpServletResponse hRes, HttpServletRequest hReq, String filePath)
            throws IOException {
        return downloadFile(hRes, hReq, new File(filePath));
    }

    public static HttpServletResponse downloadFile(HttpServletResponse hRes, HttpServletRequest hReq, File file)
            throws IOException {
        String fileName = file.getName();
        if (file.isDirectory()) {
            fileName = null;
        }
        return downloadFile(hRes, hReq, file, fileName);
    }

    /**
     * 下载文件
     * 
     * @param file
     * @param response
     * @return
     * @throws IOException
     */
    public static HttpServletResponse downloadFile(HttpServletResponse hRes, HttpServletRequest hReq, File file,
            String fileName)
            throws IOException {
        if (file == null) {
            log.error("[>_<:20210417-1612-001] 下载文件为 null");
        }

        File deleteFile = null;
        if (file.isDirectory()) {
            // 文件是一个目录，先压缩，再下载，下载后，删除压缩文件；
            log.info("[^_^:20210417-1612-002] 文件是个目录，先压缩，再下载；文件目录是:{}", file.getPath());
            file = ZKFileUtils.compress(file);
            log.info("[^_^:20210417-1612-003] 文件是个目录，压缩后的文件是:{}", file.getAbsoluteFile());
            if (fileName == null) {
                fileName = file.getName();
            }
            deleteFile = file;
        }

        try {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                ZKServletUtils.downloadFile(hRes, hReq, is, fileName);
            } finally {
                ZKStreamUtils.closeStream(is);
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            if (deleteFile != null) {
                ZKFileUtils.deleteFile(deleteFile);
            }
        }
        return hRes;
    }

    public static void setDownloadFileHeader(HttpServletResponse hRes, HttpServletRequest hReq, String fileName)
            throws UnsupportedEncodingException {
        // 清空response
        hRes.reset();
        hRes.setStatus(200);

        hRes.setHeader("Pragma", "No-cache");
        hRes.setHeader("Cache-Control", "No-cache");
        hRes.setDateHeader("Expires", 0);

        // 以流的形式下载文件。
        hRes.setContentType("application/octet-stream");
        // 如果有输入文件名，会设置响应头的文件名；如果没有文件名，直接出输出文件流
        // 此处会用URLEncoder.encode方法进行处理，解决中文文件名乱码的问题；
        if (!ZKStringUtils.isEmpty(fileName)) {
            // 给文件名进行URL编码
            hRes.setHeader("Content-Disposition",
                    "attachment;filename=" + ZKEncodingUtils.urlEncode(fileName, "UTF-8"));

//            String userAgent = hReq == null ? null : ZKServletUtils.getUserAgent(hReq);
//            if (userAgent != null && userAgent.contains("Firefox")) {
//                // 是火狐浏览器，使用BASE64编码
//                fileName = "=?utf-8?b?" + new BASE64Encoder().encode(fileName.getBytes("utf-8")) + "?=";
//                hRes.setHeader("Content-Disposition", "attachment;filename==?utf-8?b?"
//                        + ZKEncodingUtils.encodeBase64ToStr(userAgent.getBytes("UTF-8")) + "?=");
//
////                (new BASE64Encoder()).encode(fileName.getBytes("utf-8"))
//            }
//            else {
//                // 给文件名进行URL编码
//                // URLEncoder.encode() 需要两个参数，第一个参数时要编码的字符串，第二个是编码所采用的字符集
//                hRes.setHeader("Content-Disposition",
//                        "attachment;filename=" + ZKEncodingUtils.urlEncode(fileName, "UTF-8"));
//            }
        }
    }

    public static HttpServletResponse downloadFile(HttpServletResponse hRes, HttpServletRequest hReq, InputStream fileIs, String fileName)
            throws IOException {
        if (fileIs == null) {
            log.error("[>_<:20210417-1612-001] 下载文件为 null");
        }

        OutputStream toClient = null;
        try {
            setDownloadFileHeader(hRes, hReq, fileName);

            toClient = new BufferedOutputStream(hRes.getOutputStream());
            ZKStreamUtils.readAndWrite(fileIs, toClient);
            toClient.flush();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            ZKStreamUtils.closeStream(toClient);
        }
        return hRes;
    }

    /* 5.国际化系列 *****************************************************************/

    public static ZKServletLocaleResolver getLocaleResolver() {
        return ZKServletUtils.getLocaleResolver(ZKWebUtils.getAppCxt());
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
     * @return
     * @return LocaleResolver
     */
    public static ZKServletLocaleResolver getLocaleResolver(ApplicationContext ctx) {
        ZKLocaleResolver localeResolver = ZKWebUtils.getLocaleResolver(ctx);
        if (localeResolver != null && localeResolver instanceof ZKServletLocaleResolver) {
            return (ZKServletLocaleResolver) localeResolver;
        }
//        if (localeResolver == null) {
//            localeResolver = getLocaleResolverByRequest(hReq);
//        }
        return null;
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
        return getLocale(ZKServletUtils.getRequest());
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
        return getLocale(null, httpServletRequest);
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
    public static Locale getLocale(ZKServletLocaleResolver localeResolver, HttpServletRequest hReq) {

        Locale locale = null;
        if (hReq != null) {
            // 优先从 HttpServletRequest 请求中取国际化语言参数；
            locale = getLocaleByRequest(hReq);
        }

        if (locale == null) {
            if (hReq == null) {
                hReq = ZKServletUtils.getRequest();
            }
            if (localeResolver == null) {
                localeResolver = getLocaleResolver(ZKWebUtils.getAppCxt());
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
    public static Locale getLocaleByLocaleResolver(ZKServletLocaleResolver localeResolver, HttpServletRequest hReq) {
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
        String localeStr = request.getHeader(ZKWebUtils.Locale_Flag_In_Header);
        localeStr = localeStr == null ? "" : localeStr.replace("-", "_");
        if (ZKStringUtils.isEmpty(localeStr)) {
            return null;
        }
        return ZKLocaleUtils.distributeLocale(localeStr);
    }

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
        setLocale(locale, getLocaleResolver(), ZKServletUtils.getRequest(), ZKServletUtils.getResponse());
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
    public static void setLocale(Locale locale, ZKServletLocaleResolver localLocaleResolver, HttpServletRequest hReq,
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
            if (hRes != null) {
                hRes.setLocale(locale);
            }
            else {
                ZKLocaleUtils.setLocale(locale);
            }
        }
    }

    /* 4.请求路径系列 *****************************************************************/
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

    public static String getContextPath(HttpServletRequest request) {
        String contextPath = (String) request.getAttribute(ZKWebUtils.INCLUDE_CONTEXT_PATH_ATTRIBUTE);
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        contextPath = ZKWebUtils.normalize(decodeRequestString(request, contextPath));
        if ("/".equals(contextPath)) {
            // the normalize method will return a "/" and includes on Jetty,
            // will also be a "/".
            contextPath = "";
        }
        return contextPath;
    }

    public static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return ZKWebUtils.normalize(decodeAndCleanUriString(request, uri));
    }

    /* 3.请求标记系列 *****************************************************************/
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
        String requestSign = ZKServletUtils.getCleanParam(hReq, ZKCoreConstants.Global.requestSign);
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

    /* 2.字符集编码系列 *****************************************************************/
    @SuppressWarnings({ "deprecation" })
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
            enc = ZKWebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        return enc;
    }

    private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
        uri = decodeRequestString(request, uri);
        int semicolonIndex = uri.indexOf(';');
        return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
    }

    /* 1.cookie 系列 *****************************************************************/

    /*** 从请求中，取 cookie 信息 ********************************************/
    public static String getCookieValueByRequest(String cookieName) {
        return getCookieValueByRequest(ZKServletUtils.getRequest(), cookieName);
    }

    public static Cookie getCookieByRequest(String cookieName) {
        return getCookieByRequest(ZKServletUtils.getRequest(), cookieName);
    }

    public static String getCookieValueByRequest(HttpServletRequest request, String cookieName) {
        Cookie c = getCookieByRequest(request, cookieName);
        if (c == null) {
            log.error("[>_<:20191220-1051-003] 从 HttpServletRequest 中取 cookie:{} 为 null；cookieName：{}", cookieName);
            return null;
        }
        return c.getValue();
    }

    public static Cookie getCookieByRequest(HttpServletRequest request, String cookieName) {
        if (request == null) {
            log.error(
                    "[>_<:20191220-1051-001] 从 HttpServletRequest 中取 cookie 时，HttpServletRequest 为 null；cookieName：{}",
                    cookieName);
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        return ZKCookieUtils.getCookie(cookies, cookieName, request.getRequestURI());
    }

    /*** 从响应头中，取 cookie 信息 ********************************************/
    public static String getCookieValueByResponse(String cookieName) {
        Cookie c = getCookieByResponse(ZKServletUtils.getResponse(), cookieName);
        if (c == null) {
            log.error("[>_<:20191220-1051-004] 从 HttpServletResponse 中取 cookie:{} 为 null；cookieName：{}", cookieName);
            return null;
        }
        return c.getValue();
    }

    public static Cookie getCookieByResponse(String cookieName) {
        return getCookieByResponse(ZKServletUtils.getResponse(), cookieName);
    }

    public static Cookie getCookieByResponse(HttpServletResponse response, String cookieName) {
        if (response == null) {
            log.error(
                    "[>_<:20191220-1051-002] 从 HttpServletResponse 中取 cookie 时，HttpServletResponse 为 null；cookieName：{}",
                    cookieName);
        }
        Cookie[] cookies = getCookieByResponse(response);
        return ZKCookieUtils.getCookie(cookies, cookieName, null);
    }

    public static Cookie[] getCookieByResponse(HttpServletResponse response) {
        Collection<String> cookieStrList = response.getHeaders(ZKCookieUtils.COOKIE_HEADER_NAME);
        return ZKCookieUtils.parseCookieStrs(cookieStrList);
    }

    /* 00.其他 *****************************************************************/
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

//    public static void issueRedirect(ServletRequest request, ServletResponse response, String url) throws IOException {
////        response.send
////        request.getRequestDispatcher(url).
////        request.getRequestDispatcher(url).forward(request, response);
////        issueRedirect(request, response, url, null, true, true);
//    }

    /**
     * 获得用户远程地址
     */
    protected static String getRemoteAddr(HttpServletRequest request) {

        String remoteAddr = request.getHeader(ZKReqHeaderKey.AddrXForwardedIP);

        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader(ZKReqHeaderKey.AddrProxyClientIP);
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader(ZKReqHeaderKey.AddrWLProxyClientIP);
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader(ZKReqHeaderKey.AddrHttpClientIp);
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader(ZKReqHeaderKey.AddrHttpXForwardedFor);
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader(ZKReqHeaderKey.AddrXRealIP);
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader(ZKReqHeaderKey.AddrHost);
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }

        return remoteAddr;
    }

//    /** 
//     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
//     * 参考文章： http://developer.51cto.com/art/201111/305181.htm 
//     *  
//     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
//     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
//     *  
//     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
//     * 192.168.1.100 
//     *  
//     * 用户真实IP为： 192.168.1.110 
//     *  
//     * @param request 
//     * @return 
//     */  
//    public static String getIpAddress(HttpServletRequest request) {  
//        String ip = request.getHeader("x-forwarded-for");  
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
//            ip = request.getHeader("Proxy-Client-IP");  
//        }  
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
//            ip = request.getHeader("WL-Proxy-Client-IP");  
//        }  
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
//            ip = request.getHeader("HTTP_CLIENT_IP");  
//        }  
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
//        }  
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
//            ip = request.getRemoteAddr();  
//        }  
//        return ip;  
//    }  

    /**
     * 支持AJAX的页面跳转
     */
    public static void redirectUrl(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            if (ZKServletUtils.isAjaxRequest(request)) {
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

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(ZKReqHeaderKey.UserAgent);
    }

}


