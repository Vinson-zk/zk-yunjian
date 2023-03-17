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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKDiskFileTransfer 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDiskFileTransfer extends ZKAbstractFileTransfer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * @param minSize
     * @param maxSize
     *            -1 不限制；0 默认 10M 限制；
     * @param checkMode
     * @param extTypes
     */
    public ZKDiskFileTransfer(long minSize, long maxSize, int checkMode, String[] extTypes) {
        super(minSize, maxSize, checkMode, extTypes);
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
    public List<File> transferFile(String path, boolean isNewName, boolean isOverride,
            Collection<MultipartFile> files) {
        return this.transferFile(path, isNewName, isOverride, files.toArray(new MultipartFile[files.size()]));
    }

    public List<File> transferFile(String path, boolean isNewName, boolean isOverride, MultipartFile... files) {

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

}
