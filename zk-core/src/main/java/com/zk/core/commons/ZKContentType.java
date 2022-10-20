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
 * @Title: ZKContentType.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:45:50 PM 
 * @version V1.0   
*/
package com.zk.core.commons;

import java.nio.charset.Charset;

import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKContentType 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public enum ZKContentType {

    DEFUALT("application/octet-stream"),
    DOC("application/msword"),
    DTD("text/xml"),
    PDF("application/pdf"),
    PPT("applications-powerpoint"),
    RTF("application/msword"),
    SWF("application/x-shockwave-flash"),
    TIF("image/tiff"),
    TIFF("image/tiff"),
    VSD("application/vnd.visio"),
    VSS("application/vnd.visio"),
    VST("application/vnd.visio"),
    VSW("application/vnd.visio"),
    VSX("application/vnd.visio"),
    VTX("application/vnd.visio"),
    WAV("audio/wav"),
    XLS("application/x-xls"),
    XML("text/xml"),
    DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    PPSX("application/vnd.openxmlformats-officedocument.presentationml.slideshow"),
    POTX("application/vnd.openxmlformats-officedocument.presentationml.template"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLTX("application/vnd.openxmlformats-officedocument.spreadsheetml.template"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PNG("image/png"),
    JPG("image/jpeg"),
    JPEG("image/jpeg"),
    BMP("image/bmp"),
    OCTET_STREAM("application/octet-stream"), OCTET_STREAM_UTF8("application/octet-stream", ZKCoreConstants.Consts.UTF_8),
    X_FORM("application/x-www-form-urlencoded"), X_FORM_UTF8("application/x-www-form-urlencoded", ZKCoreConstants.Consts.UTF_8),
    JSON("application/json"), JSON_UTF8("application/json", ZKCoreConstants.Consts.UTF_8),
    XHTML("text/html"), XHTML_UTF8("text/html", ZKCoreConstants.Consts.UTF_8),
    MULTIPART_FORM_DATA("multipart/form-data"), MULTIPART_FORM_DATA_UTF8("multipart/form-data", ZKCoreConstants.Consts.UTF_8),
    TEXT_PLAIN("text/plain"), TEXT_PLAIN_UTF8("text/plain", ZKCoreConstants.Consts.UTF_8);

    private String contentType;

    private Charset charset;

    private ZKContentType(String contentType) {
        this.contentType = contentType;
        this.charset = ZKCoreConstants.Consts.ISO_8859_1;
    }

    private ZKContentType(String contentType, Charset charset) {
        this.contentType = contentType;
        this.charset = charset;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Charset getCharset() {
        return this.charset;
    }
    
    public Charset setCharset(Charset charset) {
        return this.charset = charset;
    }

    // 不包含 字符集编码
    @Override
    public String toString() {
        return contentType;
    }

//    @Override
//    public final String name() {
//        return contentType;
//    }

    // 包含 字符集编码
    public String toContentTypeStr() {

        if (charset == null) {
            return contentType;
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(contentType);
            sb.append(";charset=");
            sb.append(charset.toString());
            return sb.toString();
        }
    }

    //
    public static ZKContentType parseByType(String type) {
        return parseByType(type, ZKCoreConstants.Consts.ISO_8859_1);
    }

    public static ZKContentType parseByType(String type, Charset charset) {
        for (ZKContentType et : ZKContentType.values()) {
            if (et.getContentType().equals(type) && et.getCharset() == charset) {
                return et;
            }
        }
        return null;
    }

    public static ZKContentType parseByTypeUtf8(String type) {
        return parseByType(type, ZKCoreConstants.Consts.UTF_8);
    }

    /**
     * 根据文件扩展名取文件的 ContentType, 扩展名不名含 点
     * 
     * @param fileExtName
     * @return
     */
    public static ZKContentType getContentTypeByFileExt(String fileExtName) {
        if (!ZKStringUtils.isEmpty(fileExtName)) {
            try {
                return ZKContentType.valueOf(fileExtName.toUpperCase());
            }
            catch(Exception e) {
                e.printStackTrace();
                return DEFUALT;
            }
        }
        return DEFUALT;
    }
}
