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
 * @Title: ZKDiskFileTransfer.java 
 * @author Vinson 
 * @Package com.zk.core.commons.support 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 16, 2019 2:21:58 PM 
 * @version V1.0   
*/
package com.zk.core.commons.support;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.ZKFileType;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKDiskFileTransfer 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDiskFileTransfer implements ZKFileTransfer {

    private static final Logger log = LoggerFactory.getLogger(ZKDiskFileTransfer.class);

    private static final long DEFUALT_MAX_SIZE = 10 * 1024 * 1024;// 10M

    public static interface FileTypeCheckMode {

        public static final int disabled = -1; // 不检测

        public static final int allow = 0; // 白名单方式检测；即 指定可以上传的文件类型；

        public static final int ignore = 1; // 黑名单方式检测；即 指定不能上传的文件类型；
    }

    /**
     * 上传文件最小限制；-1，不限制
     */
    private long minSize = -1;

    /**
     * 上传文件最大限制；-1，不限制
     */
    private long maxSize = -1;

    /**
     * 文件类型，检查模式；0-白名单检测；1-黑名单检测；-1-不检测;
     */
    private int checkMode = FileTypeCheckMode.disabled;

    /**
     * 文件类型检测
     */
    private ZKFileType[] fileTypes = null; // 文件类型；

    /**
     * @param minSize
     * @param maxSize
     *            -1 不限制；0 默认 10M 限制；
     * @param checkMode
     * @param extTypes
     */
    public ZKDiskFileTransfer(long minSize, long maxSize, int checkMode, String[] extTypes) {
        this.minSize = minSize;
        this.maxSize = maxSize == 0 ? DEFUALT_MAX_SIZE : maxSize;
        this.checkMode = checkMode;
        if (fileTypes != null) {
            this.fileTypes = ZKFileType.getFileTypeByString(extTypes);
        }else {
            this.fileTypes = new ZKFileType[] {};
        }
    }

    @Override
    public File transferFile(InputStream inputStream, String path, String fileName, boolean isOverride,
            boolean appendFlag) {
        File file = null;
        file = ZKFileUtils.createFile(path, fileName, isOverride);
        if (file != null) {
            long fileSize = ZKFileUtils.writeFile(inputStream, file, appendFlag);
            if (!this.checkFileSize(fileSize)) {
                log.error("[>_<20191217-1006-001] 文件大小校验失败，删除文件");
                file.delete();
            }
            if (!this.checkFileType(ZKFileUtils.getFileType(file))) {
                log.error("[>_<20191217-1006-002] 文件类型校验失败，删除文件");
                file.delete();
            }
        }
        return file;
    }

    @Override
    public List<File> transferFile(Collection<MultipartFile> files, String path, boolean isNewName,
            boolean isOverride) {

        List<File> resultList = new ArrayList<>(); // 类型检验不通过时，要在 catch
                                                   // 里删除文件，所以要放到外面
        try {
            String fileName = null;
            String fileExtNames = null;
            File file = null;
            for (MultipartFile mFile : files) {
                if (!this.checkFileSize(mFile.getSize())) {
                    log.error("[>_<20191217-1041-001] 文件大小校验失败");
                }

                file = null;
                fileName = null;
                fileExtNames = null;
                fileName = mFile.getOriginalFilename();
//                fileName = new String(fileName.getBytes("ISO-8859-1"));
                if (ZKStringUtils.isEmpty(fileName)) {
                    log.error("[>_<20191217-1919-001] 文件上传失败失败，无 OriginalFilename ");
                    continue;
                }
                if (fileName.lastIndexOf(".") != -1) {
                    fileExtNames = fileName.substring(fileName.lastIndexOf("."));
                }
                fileExtNames = fileExtNames == null ? "" : fileExtNames;
                if (isNewName) {
                    fileName = UUID.randomUUID().toString() + fileExtNames;
                }

                file = ZKFileUtils.createFile(path, fileName, isOverride);
                mFile.transferTo(file);

                if (file != null) {
                    if (!this.checkFileType(ZKFileUtils.getFileType(file))) {
                        log.error("[>_<20191217-1041-002] 文件类型校验失败，删除文件");
                        file.delete();
                    }
                    else {
                        resultList.add(file);
                    }
                }
            }
            return resultList;
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }

    }

    @Override
    public File getFile(String filePathAndName) {
        File file = new File(filePathAndName);
        return file;
    }

    @Override
    public boolean checkFileSize(long fileSize) {
        // 判断 文件 大小
        if (minSize > -1 && fileSize < minSize) {
            log.error("[>_<:20191216-1726-001] 上传文件大小：{}B，小于了早小阀值：{}B", fileSize, minSize);
            return false;
        }
        // 判断 文件 大小
        if (maxSize > -1 && fileSize > maxSize) {
            log.error("[>_<:20191216-1726-002] 上传文件大小：{}B，大于了早大阀值：{}B", fileSize, maxSize);
            return false;
        }
        return true;
    }

    @Override
    public boolean checkFileType(ZKFileType fileType) {
        if (this.checkMode == FileTypeCheckMode.allow) {
            // 白名单检测
            if (ArrayUtils.contains(this.fileTypes, fileType)) {
                // 上传文件类型；在指定的可以上传的文件类型中;
                return true;
            }
            log.error("[>_<:20191216-1726-003] 文件类型不在白名单中；文件类型：{}, 指定的可上传文件类型：{}", fileType.toString(),
                    ZKJsonUtils.writeObjectJson(fileTypes));
            return false;
        }
        else if (this.checkMode == FileTypeCheckMode.ignore) {
            // 黑名单检测;
            if (ArrayUtils.contains(this.fileTypes, fileType)) {
                // 上传文件类型在指定的不能上传的文件类型中；
                log.error("[>_<:20191216-1726-004] 文件类型在黑名单中；文件类型：{}, 指定的可上传文件类型：{}", fileType.toString(),
                        ZKJsonUtils.writeObjectJson(fileTypes));
                return false;
            }
            return true;
        }
        return true;
    }

}
