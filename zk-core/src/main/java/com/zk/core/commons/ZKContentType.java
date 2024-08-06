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

    DEFUALT("application/octet-stream", null),
    DOC("application/msword", "doc"),
    DTD("text/xml", "xml"),
    PDF("application/pdf", "pdf"),
    PPT("applications-powerpoint", "ppt"),
    RTF("application/msword", "rtf"),
    SWF("application/x-shockwave-flash", "swf"),
    TIF("image/tiff", "tif"),
    TIFF("image/tiff", "tiff"),
    VSD("application/vnd.visio", "vsd"),
    VSS("application/vnd.visio", "vss"),
    VST("application/vnd.visio", "vst"),
    VSW("application/vnd.visio", "vsw"),
    VSX("application/vnd.visio", "vsx"),
    VTX("application/vnd.visio", "vtx"),
    WAV("audio/wav", "wav"),
    XLS("application/x-xls", "xls"),
    XML("text/xml", "xml"),
    DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx"),
    PPSX("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx"),
    POTX("application/vnd.openxmlformats-officedocument.presentationml.template", "potx"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    XLTX("application/vnd.openxmlformats-officedocument.spreadsheetml.template", "xltx"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    PNG("image/png", "png"),
    JPG("image/jpeg", "jpg"),
    JPEG("image/jpeg", "jpeg"),
    BMP("image/bmp", "bmp"),
    OCTET_STREAM("application/octet-stream", null), OCTET_STREAM_UTF8("application/octet-stream", null, ZKCoreConstants.Consts.UTF_8),
    X_FORM("application/x-www-form-urlencoded", null), X_FORM_UTF8("application/x-www-form-urlencoded", null, ZKCoreConstants.Consts.UTF_8),
    JSON("application/json", "json"), JSON_UTF8("application/json", "json", ZKCoreConstants.Consts.UTF_8),
    XHTML("text/html", "html"), XHTML_UTF8("text/html", "html", ZKCoreConstants.Consts.UTF_8),
    MULTIPART_FORM_DATA("multipart/form-data", null), MULTIPART_FORM_DATA_UTF8("multipart/form-data", null, ZKCoreConstants.Consts.UTF_8),
    TEXT_PLAIN("text/plain", "txt"), TEXT_PLAIN_UTF8("text/plain", "txt", ZKCoreConstants.Consts.UTF_8);

    private String contentType;
    
    private String extName;

    private Charset charset;

    private ZKContentType(String contentType, String extName) {
        this.contentType = contentType;
        this.extName = extName;
        this.charset = null;
    }

    private ZKContentType(String contentType, String extName, Charset charset) {
        this.contentType = contentType;
        this.extName = extName;
        this.charset = charset;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Charset getCharset() {
        return this.charset;
    }
    
    /**
     * @return extName sa
     */
    public String getExtName() {
        return extName;
    }

    public Charset setCharset(Charset charset) {
        return this.charset = charset;
    }

    // 不包含 字符集编码
    @Override
    public String toString() {
        return this.contentType;
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
    public static ZKContentType parseByFileExt(String fileExtName) {
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
