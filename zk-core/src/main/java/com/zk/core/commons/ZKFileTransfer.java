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
 * @Title: ZKFileTransfer.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 16, 2019 2:22:24 PM 
 * @version V1.0   
*/
package com.zk.core.commons;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/** 
* @ClassName: ZKFileTransfer 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKFileTransfer {

    /**
     * 
     *
     * @Title: transferFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 17, 2019 10:08:50 AM
     * @param inputStream
     *            输入流
     * @param path
     *            路径
     * @param fileName
     *            文件名
     * @param isOverride
     *            如果文件存在时是否覆盖，不覆盖时会创建同名带括号加序号的文件名；true-覆盖；false-不覆盖；
     * @param appendFlag
     *            当文件存在时，是追加内容，还是从头开始写内容
     * @return File
     */
    String transferFile(InputStream inputStream, String path, String fileName, boolean isOverride, boolean appendFlag);

    String transferFile(String path, String fileName, boolean isOverride, MultipartFile mFile);

    /**
     *
     * @Title: transferFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 16, 2019 2:25:29 PM
     * @param files
     *            MultipartFile 文件 List
     * @param path
     *            文件子路径
     * @param isNewName
     *            是否新建文件名？true 是；false 否
     * @param isOverride
     *            文件存在时，是否覆盖？true 是；false 否
     * @return List<File>
     */
    List<String> transferFile(String path, boolean isNewName, boolean isOverride, Collection<MultipartFile> files);

    List<String> transferFile(String path, boolean isNewName, boolean isOverride, MultipartFile... files);

//    /**
//     * 
//     * @Title: getFile
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Dec 16, 2019 4:17:31 PM
//     * @param fileId
//     *            文件唯一标识，磁盘存储时，可以是文件路径和名称；
//     * @return File 返回 null 文件不存在；
//     */
//    File getFile(String fileId);

    /**
     * 
     *
     * @Title: getFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2023 3:50:20 PM
     * @param fileId
     * @param os
     * @return boolean 返回 false 文件不存在；
     */
    boolean getFile(String fileId, OutputStream os);

    /**
     * 验证文件大小
     *
     * @Title: checkFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 16, 2019 5:09:01 PM
     * @param fileSize
     *            文件大小
     * @return boolean
     */
    boolean checkFileSize(long fileSize);

    /**
     * 验证文件
     *
     * @Title: checkFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 16, 2019 5:09:01 PM
     * @param fileType
     *            文件类型
     * @return boolean
     */
    boolean checkFileType(ZKFileType fileType);

}
