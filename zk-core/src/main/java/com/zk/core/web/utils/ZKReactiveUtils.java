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
* @Title: ZKReactiveUtils.java 
* @author Vinson 
* @Package com.zk.core.web.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date May 29, 2024 4:29:11 PM 
* @version V1.0 
*/
package com.zk.core.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;

import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKReactiveUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKReactiveUtils {

    protected static Logger log = LogManager.getLogger(ZKReactiveUtils.class);

    public static String getPathWithinApplication(ServerHttpRequest request) {
        return request.getPath().pathWithinApplication().value();
    }

    public static String getRemoteAddr(ServerHttpRequest request) {
        String remoteAddr = request.getHeaders().getFirst("X-Real-IP");
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("X-Forwarded-For");
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ZKStringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("Host");
        }
        return ZKStringUtils.isNotBlank(remoteAddr) ? remoteAddr : request.getRemoteAddress().toString();
    }

    public static void get() {

    }

    /* 5.国际化系列 *****************************************************************/
    public static Locale getLocale(ServerHttpRequest request) {
        if (request != null) {
            return getLocaleByRequest(request);
        }
        return ZKLocaleUtils.getDefautLocale();
    }

    /**
     * 从 request 请求头中，通过参数取语言
     */
    public static Locale getLocaleByRequest(ServerHttpRequest request) {
        if (request == null) {
            return null;
        }
        String localeStr = request.getHeaders().getFirst(ZKWebUtils.Locale_Flag_In_Header);
        localeStr = localeStr == null ? "" : localeStr.replace("-", "_");
        if (ZKStringUtils.isEmpty(localeStr)) {
            return null;
        }
        return ZKLocaleUtils.distributeLocale(localeStr);
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
//    public static ServerHttpRequest getRequest() {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
//            return ((ServletRequestAttributes) requestAttributes).getRequest();
//        }
//        return null;
//    }

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
//    public static ServerHttpResponse getResponse() {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
//            return ((ServletRequestAttributes) requestAttributes).getResponse();
//        }
//
//        return null;
//    }

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
    public static byte[] read(ServerHttpRequest req) {
//        InputStream is = null;
//        ByteArrayOutputStream baOs = null;
//        try {
//            is = req.getInputStream();
//            baOs = new ByteArrayOutputStream();
//            ZKServletUtils.read(req, baOs);
//            return baOs.toByteArray();
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            log.error("[>_<:20180905-0821-001] 读请 HttpServletRequest 输入流失败！");
//        } finally {
//            if (is != null) {
//                ZKStreamUtils.closeStream(is);
//            }
//            if (baOs != null) {
//                ZKStreamUtils.closeStream(baOs);
//            }
//        }
        return null;
    }

    public static void read(ServerHttpRequest req, OutputStream os) {
//        InputStream is = null;
//        try {
//            is = req.getInputStream();
//            ZKStreamUtils.readAndWrite(is, os);
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            log.error("[>_<:20180905-0821-001] 读请 HttpServletRequest 输入流失败！");
//        } finally {
//            if (is != null) {
//                ZKStreamUtils.closeStream(is);
//            }
//        }
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
    public static Mono<Void> write(ServerHttpResponse hRes, String content) {
        OutputStream os = null;
        try {
            DataBufferFactory factory = hRes.bufferFactory();
            DataBuffer bf = factory.allocateBuffer(content.getBytes().length);
            // 获得 OutputStream 的引用
            os = bf.asOutputStream();
            // 操作 outputStream 往里面写入数据
            os.write(content.getBytes());
            os.flush();
            return hRes.writeWith(Mono.just(bf));
        }
        catch(IOException e) {
            log.error("[>_<:20240218-2323-001] Response 通信异常！");
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            ZKStreamUtils.closeStream(os);
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
    public static Mono<Void> write(ServerHttpResponse hRes, byte[] content) {
        OutputStream os = null;
        try {
            DataBufferFactory factory = hRes.bufferFactory();
            @SuppressWarnings("deprecation")
            DataBuffer bf = factory.allocateBuffer();
            os = bf.asOutputStream();
            os.write(content);
            os.flush();
            return hRes.writeWith(Mono.just(bf));
        }
        catch(IOException e) {
            log.error("[>_<:20240218-2322-001] Response 通信异常！");
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
    @SuppressWarnings("deprecation")
    public static Mono<Void> write(ServerHttpResponse hRes, InputStream is) {

        OutputStream os = null;
        try {
            DataBufferFactory factory = hRes.bufferFactory();
            DataBuffer bf = factory.allocateBuffer();
            os = bf.asOutputStream();
            ZKStreamUtils.readAndWrite(is, os);
            os.flush();
            return hRes.writeWith(Mono.just(bf));
        }
        catch(IOException e) {
            log.error("[>_<:20240218-2321-001] Response 通信异常！");
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            ZKStreamUtils.closeStream(os);
        }
    }

    /* 1.cookie 系列 *****************************************************************/

    /*** 从请求中，取 cookie 信息 ********************************************/
//    public static String getCookieValueByRequest(String cookieName) {
//        return getCookieValueByRequest(ZKServletUtils.getRequest(), cookieName);
//    }

//    public static Cookie getCookieByRequest(String cookieName) {
//        return getCookieByRequest(ZKServletUtils.getRequest(), cookieName);
//    }

    public static String getCookieValueByRequest(ServerHttpRequest request, String cookieName) {
        HttpCookie c = getCookieByRequest(request, cookieName);
        if (c == null) {
            log.error("[>_<:20191220-1051-003] 从 HttpServletRequest 中取 cookie:{} 为 null；cookieName：{}", cookieName);
            return null;
        }
        return c.getValue();
    }

    public static HttpCookie getCookieByRequest(ServerHttpRequest request, String cookieName) {
        if (request == null) {
            log.error(
                    "[>_<:20191220-1051-001] 从 HttpServletRequest 中取 cookie 时，HttpServletRequest 为 null；cookieName：{}",
                    cookieName);
        }
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (cookies == null || cookies.size() == 0) {
            return null;
        }

        return cookies.getFirst(cookieName);
    }

}
