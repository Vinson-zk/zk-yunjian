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
 * @Title: ZKHttpApiUtils.java 
 * @author Vinson 
 * @Package com.zk.webmvc.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 12, 2019 9:54:59 AM 
 * @version V1.0   
*/
package com.zk.core.web.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKFileType;
import com.zk.core.commons.ZKX509TrustManager;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKHttpApiUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKHttpApiUtils {

    private final static Logger logger = LogManager.getLogger(ZKHttpApiUtils.class);

    private final static String default_charset = "UTF-8";

    // 连接管理器
    private static PoolingHttpClientConnectionManager httpConnectionPool;

    // 请求配置
    private static RequestConfig requestConfig;

    private static interface defaultConfig {
        // 最大连接数 默认 100
        public static final int maxTotal = 2;

        // 最大路由 默认 2
        public static final int maxPerRoute = 2;

        // socket 超时值（单位：毫秒） 默认 50000 毫秒
        public static final int socketTimeout = 50000;

        // connect 超时值（单位：毫秒）默认 50000 毫秒
        public static final int connectTimeout = 50000;

        // request 超时值（单位：毫秒）默认 50000 毫秒
        public static final int requestTimeout = 50000;
    }

    static {
        init(0, 0, 0, 0, 0, null);
    }

    /**
     * 
     *
     * @Title: init
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 28, 2019 2:09:25 PM
     * @param maxTotal
     *            最大连接数 默认 100; 传入为 0 时取默认值；
     * @param maxPerRoute
     *            最大路由 默认 2; 传入为 0 时取默认值；
     * @param socketTimeout
     *            socket 超时值（单位：毫秒） 默认 50000 毫秒; 传入为 0 时取默认值；
     * @param connectTimeout
     *            connect 超时值（单位：毫秒）默认 50000 毫秒; 传入为 0 时取默认值；
     * @param requestTimeout
     *            request 超时值（单位：毫秒）默认 50000 毫秒; 传入为 0 时取默认值；
     * @param tm
     * @return void
     */
    public static void init(int maxTotal, int maxPerRoute, int socketTimeout, int connectTimeout, int requestTimeout,
            TrustManager[] tm) {

        // 最大连接数 默认 100
        if (maxTotal == 0) {
            maxTotal = defaultConfig.maxTotal;
        }
        // 最大路由 默认 2
        if (maxPerRoute == 0) {
            maxPerRoute = defaultConfig.maxPerRoute;
        }
        // socket 超时值（单位：毫秒） 默认 50000 毫秒
        if (socketTimeout == 0) {
            socketTimeout = defaultConfig.socketTimeout;
        }
        // connect 超时值（单位：毫秒）默认 50000 毫秒
        if (connectTimeout == 0) {
            connectTimeout = defaultConfig.connectTimeout;
        }
        // request 超时值（单位：毫秒）默认 50000 毫秒
        if (requestTimeout == 0) {
            requestTimeout = defaultConfig.requestTimeout;
        }

        if (tm == null) {
            tm = new TrustManager[] { new ZKX509TrustManager() };
        }

        try {
            // SSLContextBuilder builder = new SSLContextBuilder();
            // builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            // SSLConnectionSocketFactory sslcsf = new
            // SSLConnectionSocketFactory(builder.build());

            /**************************************************/

            // 创建SSLContext，绕开 HTTPS 的 SSL 证书
            SSLContext sslContext = SSLContext.getInstance("SSL");
//            TrustManager[] tm = { new ZKX509TrustManager() };
            // 初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            // // 获取SSLSocketFactory对象
            // SSLSocketFactory sslsf = sslContext.getSocketFactory();
            SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext);

            /**************************************************/
            // 配置同时支持 HTTP 和 HTPPS；即设置 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslcsf)
                    .build();

            // 初始化连接管理器
            httpConnectionPool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
            httpConnectionPool.setMaxTotal(maxTotal);
            // 设置最大路由
            httpConnectionPool.setDefaultMaxPerRoute(maxPerRoute);
            // 根据默认超时限制初始化requestConfig; 设置请求超时时间
            requestConfig = RequestConfig.custom().setConnectionRequestTimeout(requestTimeout)
                    .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        logger.info("[20170912-0819-001] HttpUtils init success!");

    }

    /**
     * 创建或取一个可用链接
     * @return
     */
    public static CloseableHttpClient getHttpClient() {

        return getHttpClient(null);
    }

    /***
     * 创建或取一个可用链接
     * @param proxy
     *            代理对象,可以为空
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getHttpClient(HttpHost proxy) {

        HttpClientBuilder clientBuilder = HttpClients.custom();
        // 设置连接池管理
        clientBuilder.setConnectionManager(httpConnectionPool);
        // 设置请求配置
        clientBuilder.setDefaultRequestConfig(requestConfig);
        // 设置重试次数
        clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        // 设置代理
        if (proxy != null) {
            // RequestConfig defaultRequestConfig =
            // RequestConfig.custom().setProxy(proxy).build();
            // clientBuilder.setDefaultRequestConfig(defaultRequestConfig);
            clientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy));
        }
        return clientBuilder.build();
    }

    /**
     * 取微信 api 接口 url 的配置
     *
     * @Title: getWxApiUrlConfig
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年9月14日 下午2:34:23
     * @param key
     *            配置API名称 key
     * @return
     * @return String
     */
    public static String getUrlConfig(String key) {
        String url = ZKEnvironmentUtils.getString(key);
        if (ZKStringUtils.isEmpty(url)) {
            logger.error("[>_<:20180918-0822-001] ZK: 配置的 API 地址错误; {key:{}, url:{}}", key, url);
            // 配置不存在
//            throw new ZKUnknownException(String.format("配置的 API 地址错误; {key:%s, url:%s}", key, url));
            // 环境配置项目有误
            throw ZKSystemException.as("zk.000003", String.format("配置的 API 地址错误; {key:%s, url:%s}", key, url));
        }
        return url;
    }

//    /**
//     * 从响应中把内容字节读出来
//     */
//    private static byte[] getContent(CloseableHttpResponse chRes) {
//        HttpEntity entity = chRes.getEntity();
//        try {
//            byte[] contentByte = EntityUtils.toByteArray(chRes.getEntity());
//            EntityUtils.consume(entity);
//            return contentByte;
//        }
//        catch(IOException e) {
//            logger.error("[>_<:20180907-1308-001] 从响应中把内容字节失败！");
//            e.printStackTrace();
//        } finally {
//            if (chRes != null) {
//                // 释放资源
//                // HttpClientUtils.closeQuietly(httpClient);
//                HttpClientUtils.closeQuietly(chRes);
//            }
//        }
//        return null;
//    }

    private static void setHeadersByMap(HttpRequestBase req, Map<String, String> headers) {
        // 设置请求头
        if (headers != null) {
            Iterator<Entry<String, String>> hIterator = headers.entrySet().iterator();
            Entry<String, String> entry = null;
            while (hIterator.hasNext()) {
                entry = hIterator.next();
                req.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 发送请求
     *
     * @Title: sendRequest
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 12, 2019 3:44:41 PM
     * @param reqBase
     *            请求
     * @param proxy
     *            代理
     * @param os
     *            输出响应内容；
     * @param outHeaders
     *            输出响应头；
     * @return int 响应码
     */
    public static int sendRequest(HttpRequestBase reqBase, HttpHost proxy, OutputStream os,
            Map<String, String> outHeaders) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        int resStatusCode = -1;
        try {
            // 配置请求信息
            reqBase.setConfig(requestConfig);
            // 创建默认的httpClient实例.
            httpClient = getHttpClient(proxy);
            // 执行请求
            response = httpClient.execute(reqBase);
            if (response != null) {
                resStatusCode = response.getStatusLine().getStatusCode();
                // 输出响应内容
                if (os != null) {
                    ZKWebUtils.readResponse(response, os);
                }
                // 输出响应头
                if (outHeaders != null) {
                    Header[] hs = response.getAllHeaders();
                    for (Header h : hs) {
                        outHeaders.put(h.getName(), h.getValue());
                    }
                }
            }
        }
        catch(Exception e) {
            logger.error("[>_<:20180907-1247-001] 请求异常，url:{}", reqBase.getURI());
            e.printStackTrace();
        } finally {
            if (response != null) {
                // 释放资源
//                HttpClientUtils.closeQuietly(httpClient);
                HttpClientUtils.closeQuietly(response);
            }
        }
        return resStatusCode;
    }

    /***
     * GET 请求，参数自行拼接
     *
     * @Title: get
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 12, 2019 4:24:13 PM
     * @param url
     *            请求 url
     * @param headers
     *            请求头
     * @param proxy
     *            代理
     * @param os
     *            输出响应内容；
     * @param outHeaders
     *            输出响应头；
     * @return int 响应码
     */
    public static int get(String url, Map<String, String> headers, HttpHost proxy, OutputStream os,
            Map<String, String> outHeaders) {
        logger.info("[^_^:20191212-1623-001] 发起 get 请求:{}, headers:{}", url, ZKJsonUtils.toJsonStr(headers));
        // 创建get请求
        HttpGet httpGet = new HttpGet(url);
        // 设置请求头
        setHeadersByMap(httpGet, headers);
        return sendRequest(httpGet, proxy, os, outHeaders);
    }

    /**
     * get 请求，重载，以字符串形式返回响应内容
     *
     * @Title: get
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 12, 2019 4:08:46 PM
     * @param url
     *            请求 url
     * @param headers
     *            请求头
     * @param proxy
     *            代理
     * @param outStringBuffer
     *            响应内容字符串输出
     * @param outCharset
     *            响应内容输出时的字符集编码
     * @param outHeaders
     *            响应头输出
     * @return int
     */
    public static int get(String url, Map<String, String> headers, HttpHost proxy, StringBuffer outStringBuffer,
            String outCharset, Map<String, String> outHeaders) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            int statusCode = get(url, headers, proxy, os, outHeaders);
            if (ZKStringUtils.isEmpty(outCharset)) {
                outCharset = default_charset;
            }
            outStringBuffer.append(new String(os.toByteArray(), outCharset));
            return statusCode;
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码异常", e);
        } finally {
            if (os != null) {
                ZKStreamUtils.closeStream(os);
            }
        }
    }

    /*** get 请求，重载；以 UTF-8 的字符集编码返回响应字符串 ***/
    public static int get(String url, Map<String, String> headers, StringBuffer outStringBuffer) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            int statusCode = get(url, headers, null, os, null);
            outStringBuffer.append(new String(os.toByteArray(), default_charset));
            return statusCode;
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码异常", e);
        } finally {
            if (os != null) {
                ZKStreamUtils.closeStream(os);
            }
        }
    }

    /*** get 请求，重载；以 UTF-8 的字符集编码返回响应字符串 ***/
    public static int get(String url, Map<String, String> headers, OutputStream os, Map<String, String> outHeaders) {
        return get(url, headers, null, os, outHeaders);
    }

    /***
     * POSR 请求
     *
     * @Title: post
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 12, 2019 4:25:25 PM
     * @param url
     *            请求 url
     * @param content
     *            请求内容
     * @param reqContentType
     *            请求内容的类型
     * @param reqCharset
     *            请求内容类型的字符集编码
     * @param headers
     *            请求头
     * @param proxy
     *            代理
     * @param os
     *            输出响应内容；
     * @param outHeaders
     *            输出响应头；
     * @return int 响应码；
     */
    public static int post(String url, String content, String reqContentType, String reqCharset,
            Map<String, String> headers, HttpHost proxy, OutputStream os, Map<String, String> outHeaders) {
        logger.info("[^_^:20191212-1627-001] 发起 post 请求:{}, content:{}, reqContentType:{}, reqCharset:{}, headers:{}",
                url, content, reqContentType, reqCharset, ZKJsonUtils.toJsonStr(headers));

        // 创建 httpPost 请求
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        setHeadersByMap(httpPost, headers);
        // 设置请求内容
        if (content != null && content.trim().length() > 0) {
            reqCharset = (ZKStringUtils.isEmpty(reqCharset)) ? default_charset : reqCharset;
            StringEntity stringEntity = new StringEntity(content, reqCharset);
            if (reqContentType != null) {
                stringEntity.setContentType(reqContentType);
            }
            else {
                stringEntity.setContentType(ZKContentType.X_FORM_UTF8.toString());
            }
            stringEntity.setContentEncoding(reqCharset);
            httpPost.setEntity(stringEntity);
        }
        return sendRequest(httpPost, proxy, os, outHeaders);
    }

    /*** POSR 请求 重载，以输入注设置请求内容 ***/
    public static int post(String url, InputStream contentIs, String reqContentType, String reqCharset,
            Map<String, String> headers, HttpHost proxy, OutputStream os, Map<String, String> outHeaders) {
        logger.info("[^_^:20191212-1627-002] 发起 post 请求:{}, content:{}, reqContentType:{}, reqCharset:{}, headers:{}",
                url, "contentIs", reqContentType, reqCharset, ZKJsonUtils.toJsonStr(headers));

        // 创建 httpPost 请求
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        setHeadersByMap(httpPost, headers);
        // 设置请求内容
        if (contentIs != null) {
            InputStreamEntity inputStreamEntity = new InputStreamEntity(contentIs);
            reqCharset = (ZKStringUtils.isEmpty(reqCharset)) ? default_charset : reqCharset;
            if (reqContentType != null) {
                inputStreamEntity.setContentType(reqContentType);
            }
            else {
                inputStreamEntity.setContentType(ZKContentType.X_FORM_UTF8.toString());
            }
            inputStreamEntity.setContentEncoding(reqCharset);
            httpPost.setEntity(inputStreamEntity);
        }
        return sendRequest(httpPost, proxy, os, outHeaders);
    }


    /*** POSR 请求 重载，以字符串形式返回响应内容 ***/
    public static int post(String url, String content, String reqContentType, String reqCharset,
            Map<String, String> headers, HttpHost proxy, StringBuffer outStringBuffer, String outCharset,
            Map<String, String> outHeaders) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            int statusCode = post(url, content, reqContentType, reqCharset, headers, proxy, os, outHeaders);
            if (ZKStringUtils.isEmpty(outCharset)) {
                outCharset = default_charset;
            }
            outStringBuffer.append(new String(os.toByteArray(), outCharset));
            return statusCode;
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码异常", e);
        } finally {
            if (os != null) {
                ZKStreamUtils.closeStream(os);
            }
        }
    }

    /*** POSR 请求 重载，以 UTF-8 字符集编码，返回响应内容字符串 ***/
    public static int post(String url, String content, String reqContentType, String reqCharset,
            Map<String, String> headers, HttpHost proxy, StringBuffer outStringBuffer, Map<String, String> outHeaders) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            int statusCode = post(url, content, reqContentType, reqCharset, headers, proxy, os, outHeaders);
            outStringBuffer.append(new String(os.toByteArray(), default_charset));
            return statusCode;
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码异常", e);
        } finally {
            if (os != null) {
                ZKStreamUtils.closeStream(os);
            }
        }
    }

    /*** POSR 请求 重载，请求内容以 Json 方式发送，并以 UTF-8 字符集编码，返回响应内容字符串 ***/
    public static int postJson(String url, String content, String reqCharset, Map<String, String> headers,
            HttpHost proxy, OutputStream os, Map<String, String> outHeaders) {
        int statusCode = post(url, content, ZKContentType.JSON_UTF8.toString(), reqCharset, headers, proxy, os,
                outHeaders);
        return statusCode;
    }

    public static int postJson(String url, String content, String reqCharset, Map<String, String> headers,
            HttpHost proxy, StringBuffer outStringBuffer, Map<String, String> outHeaders) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            int statusCode = postJson(url, content, reqCharset, headers, proxy, os, outHeaders);
            outStringBuffer.append(new String(os.toByteArray(), default_charset));
            return statusCode;
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码异常", e);
        } finally {
            if (os != null) {
                ZKStreamUtils.closeStream(os);
            }
        }
    }

    public static int postJson(String url, String content, Map<String, String> headers) {
        int statusCode = post(url, content, ZKContentType.JSON_UTF8.toString(), null, headers, null,
                (OutputStream) null, null);
        return statusCode;
    }

    public static int postJson(String url, String content, Map<String, String> headers, StringBuffer outStringBuffer) {
        return postJson(url, content, null, headers, outStringBuffer);
    }

    public static int postJson(String url, String content, String reqCharset, Map<String, String> headers,
            StringBuffer outStringBuffer) {
        return postJson(url, content, reqCharset, headers, null, outStringBuffer, null);
    }

    public static int postJson(String url, String content, String reqCharset, Map<String, String> headers,
            OutputStream os, Map<String, String> outHeaders) {
        return postJson(url, content, reqCharset, headers, null, os, outHeaders);
    }

    /*** POSR 请求 重载，请求内容以 XML 方式发送，并以 UTF-8 字符集编码，返回响应内容字符串 ***/
    public static int postXml(String url, String content, String reqCharset, Map<String, String> headers,
            HttpHost proxy, StringBuffer outStringBuffer, Map<String, String> outHeaders) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            int statusCode = post(url, content, ZKContentType.XML.toString(), reqCharset, headers, proxy,
                    os, outHeaders);
            outStringBuffer.append(new String(os.toByteArray(), default_charset));
            return statusCode;
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码异常", e);
        } finally {
            if (os != null) {
                ZKStreamUtils.closeStream(os);
            }
        }
    }

    public static int postXml(String url, String content, String reqCharset, Map<String, String> headers,
            StringBuffer outStringBuffer) {
        return postXml(url, content, reqCharset, headers, null, outStringBuffer, null);
    }
    
    /*** 文件上传 ***/
    public static int uploadFileByStream(String url, File file, String fileName, Map<String, String> headers,
            HttpHost proxy, OutputStream os, Map<String, String> outHeaders) {
        InputStream contentIs = null;
        try {
            HttpPost httpPost = new HttpPost(url); // 创建httpPost
            // 设置请求头
            if (headers == null) {
                headers = new HashMap<>();
            }
//            headers.put("Content-Type", ZKContentType.OCTET_STREAM_UTF8.toString());
            // 请求头中是以 ISO-8859-1 编码的，不能改变，在后台接收后，再自行转换为 UTF-8
            headers.put("fileName", ZKStringUtils.encodedString(fileName, "ISO-8859-1"));
            setHeadersByMap(httpPost, headers);
            contentIs = new FileInputStream(file);
            return post(url, contentIs, ZKContentType.OCTET_STREAM.toString(), null, headers, proxy, os,
                    outHeaders);
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            if (contentIs != null) {
                ZKStreamUtils.closeStream(contentIs);
            }
        }
    }

    /**
     * 文件上传
     *
     * @Title: uploadFileByMultipart
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 18, 2019 11:17:37 AM
     * @param url
     * @param files
     *            文件，以及对应文件上传的参数名
     * @param headers
     *            请求头，不要包含 Content-Type
     * @param proxy
     * @param os
     * @param outHeaders
     * @return
     * @return int
     */
    public static int uploadFileByMultipart(String url, Map<File, String> files,
            Map<String, String> headers, HttpHost proxy, OutputStream os, Map<String, String> outHeaders) {
        logger.info("[^_^:20190312-1134-001] 发起 uploadFileByMultipart 请求:{}, files:{}, headers:{}", url,
                ZKJsonUtils.toJsonStr(files),
                ZKJsonUtils.toJsonStr(headers));

        try {
            if (files != null && !files.isEmpty()) {
                // 创建 httpPost 请求
                HttpPost httpPost = new HttpPost(url);
                final String BOUNDARY = "7cd4a6d158c";
                final String MP_BOUNDARY = "--" + BOUNDARY;
                final String LINE_FEED = "\r\n";
                final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
//                final String contentType = ZKContentType.X_FORM.getContentType();
    
                // 添加请求头为 Multipart file 及文件 分隔符
                if (headers == null) {
                    headers = new HashMap<>();
                }
                headers.put("Content-Type", ZKContentType.MULTIPART_FORM_DATA_UTF8 + "; boundary=" + BOUNDARY);
//                headers.put("Content-Type",
//                        org.apache.http.entity.ContentType.MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);

                // 设置请求头
                setHeadersByMap(httpPost, headers);
                // 内容输入流组
                ArrayList<InputStream> cIs = new ArrayList<InputStream>();
                // 上传内容开头; 无
                // 上传内容
                StringBuffer strBuffer = null;
                InputStream boundaryHeaderIs = null;
                ZKFileType fileType = null;
                ZKContentType contenType = null;
                for (Entry<File, String> fileEntry : files.entrySet()) {

                    strBuffer = new StringBuffer();
                    /**
                     * Content-Disposition: 说明
                     * 
                     * @name 参数名
                     * @fileName 文件名
                     */
                    strBuffer.append(LINE_FEED).append(MP_BOUNDARY).append(LINE_FEED);
                    strBuffer.append("Content-Disposition: form-data;name=\"")
                            .append(fileEntry.getValue()).append("\"; filename=\"")
                            // ISO-8859-1
                            .append(fileEntry.getKey().getName()).append("\"").append(LINE_FEED);
                    fileType = ZKFileUtils.getFileType(fileEntry.getKey());
                    contenType = fileType == null ? null : ZKContentType.parseByFileExt(fileType.getType());
                    strBuffer.append("Content-Type: ").append(contenType == null ? "" : contenType.toContentTypeStr());
//                    strBuffer.append("Content-Type: ").append(contentType);
//                    strBuffer.append("Content-Type: ").append(ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr());
                    strBuffer.append(LINE_FEED).append(LINE_FEED);
                    boundaryHeaderIs = new ByteArrayInputStream(strBuffer.toString().getBytes());
                    cIs.add(boundaryHeaderIs);
                    // 文件输入流
                    cIs.add(new FileInputStream(fileEntry.getKey()));
                }
                // 上传内容结尾 识符
                strBuffer = new StringBuffer();
                strBuffer.append(LINE_FEED).append(END_MP_BOUNDARY);
                InputStream boundaryEndFlagIs = new ByteArrayInputStream(strBuffer.toString().getBytes());
                cIs.add(boundaryEndFlagIs);
    
                // 上传输入流组合制作成最终上传输入流
                Enumeration<InputStream> e = Collections.enumeration(cIs);
                SequenceInputStream sInputStream = new SequenceInputStream(e);
                InputStreamEntity inputStreamEntity = new InputStreamEntity(sInputStream);
                // inputStreamEntity.setContentType("form-data");
                // inputStreamEntity.setContentEncoding(charset.defaultCharset().displayName());
                httpPost.setEntity(inputStreamEntity);
                return sendRequest(httpPost, proxy, os, outHeaders);
            }
        }catch (Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
        return -1;
    }

    /**
     * 发送文件
     *
     * @Title: uploadFileByMultipart
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 12, 2019 5:04:34 PM
     * @param url
     * @param maps
     *            额外的字段
     * @param fileLists
     *            文件，以及对应文件上传的参数名
     * @param headers
     *            请求头，不要包含 Content-Type
     * @param proxy
     * @param os
     * @param outHeaders
     * @return int
     */
    public static int uploadFileByMultipart(String url, Map<String, String> maps, Map<File, String> files,
            Map<String, String> headers, HttpHost proxy, OutputStream os, Map<String, String> outHeaders) {
        HttpPost httpPost = new HttpPost(url); // 创建httpPost
        // 设置请求头
        // 添加请求头为 Multipart file 及文件 分隔符
        if (headers == null) {
            headers = new HashMap<>();
        }
        // 不能设置，设置会报错
//        headers.put("Content-Type", ZKContentType.MULTIPART_FORM_DATA_UTF8.toString());
//        final String BOUNDARY = "7cd4a6d158c";
//        headers.put("Content-Type", ZKContentType.MULTIPART_FORM_DATA_UTF8 + "; boundary=" + BOUNDARY);
        setHeadersByMap(httpPost, headers);
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        // 此处设置不生效，filename 还是乱码
//        meBuilder = meBuilder.setCharset(ZKCoreConstants.Consts.UTF_8);
        meBuilder.setMode(HttpMultipartMode.RFC6532);

        if (maps != null) {
            for (String key : maps.keySet()) {
                meBuilder.addPart(key, new StringBody(maps.get(key), org.apache.http.entity.ContentType.TEXT_PLAIN));
//                meBuilder.addPart(key, new StringBody(maps.get(key), ZKContentType.TEXT_PLAIN_UTF8));
            }
        }

        if (files != null) {
//            ZKFileType fileType = null;
            for (Entry<File, String> fileEntry : files.entrySet()) {
//                FileBody fileBody = new FileBody(fileEntry.getKey(),
//                        ContentType.create("application/octet-stream", "UTF-8"), fileEntry.getKey().getName());
                FileBody fileBody = new FileBody(fileEntry.getKey());
                meBuilder.addPart(fileEntry.getValue(), fileBody);
            }
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        return sendRequest(httpPost, proxy, os, outHeaders);
    }
}
