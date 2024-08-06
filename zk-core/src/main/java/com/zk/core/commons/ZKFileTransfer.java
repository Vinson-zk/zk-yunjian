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

/** 
* @ClassName: ZKFileTransfer 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKFileTransfer {

//    List<String> transferFile(String path, boolean isNewName, boolean isOverride, Collection<MultipartFile> files);
//
//    List<String> transferFile(String path, boolean isNewName, boolean isOverride, MultipartFile... files);
//
//    /**
//     * 
//     *
//     * @Title: transferFile
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Feb 28, 2024 4:28:24 PM
//     * @param path
//     *            文件路径
//     * @param fileName
//     *            指定文件名，可以为空
//     * @param isNewName
//     *            是否新建文件名？true 是；false 否
//     * @param isOverride
//     *            文件存在时，是否覆盖？true 是；false 否
//     * @param mFile
//     *            MultipartFile 文件
//     * @return
//     * @return String
//     */
//    String transferFile(String path, String fileName, boolean isNewName, boolean isOverride, MultipartFile mFile);
//    // ======================================================================================

//    String transferFile(InputStream inputStream, String path, String fileName, boolean isOverride, boolean appendFlag);

    /**
     * 上传文件
     *
     * @Title: transferFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 1, 2024 3:25:37 PM
     * @param inputStream
     *            文件输入流
     * @param path
     *            文件路径
     * @param fileName
     *            文件名
     * @param isOverride
     *            如果文件存在时是否覆盖，不覆盖时会创建同名带括号加序号的文件名；true-覆盖；false-不覆盖；
     * @param appendFlag
     *            当文件存在时，是追加内容，还是从头开始写内容; true 追加，false 从头开始写;
     * @param outFileSize
     *            输出参数，文件大小，单位 b
     * @param outFileType
     *            输出参数，文件类型
     * @return String 文件获取的唯一标识
     */
    String transferFile(InputStream inputStream, String path, String fileName, boolean isOverride, boolean appendFlag,
            long[] outFileSize, ZKFileType[] outFileType);


    /**
     * 取文件
     *
     * @Title: getFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2023 3:50:20 PM
     * @param fileId
     * @param os
     * @return boolean 返回 false 文件不存在；
     */
    long getFile(String fileId, OutputStream os);

    /**
     * 删除文件
     *
     * @Title: del
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 1, 2024 2:19:50 PM
     * @param fileId
     * @return
     * @return boolean
     */
    boolean del(String fileId);

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
    void checkFileSize(long fileSize);

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
    void checkFileType(ZKFileType fileType);

}
