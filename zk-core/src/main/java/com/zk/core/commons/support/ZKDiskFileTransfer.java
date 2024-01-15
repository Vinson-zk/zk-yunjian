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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;

/**
 * 单元测试在
 * 
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
    public String transferFile(InputStream inputStream, String path, String fileName, boolean isOverride,
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
        return file.getAbsolutePath();
    }

    public String transferFile(String path, String fileName, boolean isOverride, MultipartFile mFile) {

        try {
            if (ZKStringUtils.isEmpty(fileName)) {
                fileName = mFile.getOriginalFilename();
            }
            File file = null;
            if (!this.checkFileSize(mFile.getSize())) {
                log.error("[>_<20191217-1041-001] 文件大小校验失败");
            }
            if (ZKStringUtils.isEmpty(fileName)) {
                log.error("[>_<20191217-1919-001] 文件上传失败失败，无文件名！");
                return null;
            }
            file = ZKFileUtils.createFile(path, fileName, isOverride);
            mFile.transferTo(file);

            if (file != null) {
                if (!this.checkFileType(ZKFileUtils.getFileType(file))) {
                    log.error("[>_<20191217-1041-002] 文件类型校验失败，删除文件");
                    file.delete();
                    file = null;
                }
            }
            return file.getAbsolutePath();
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    @Override
    public List<String> transferFile(String path, boolean isNewName, boolean isOverride,
            Collection<MultipartFile> files) {
        return this.transferFile(path, isNewName, isOverride, files.toArray(new MultipartFile[files.size()]));
    }

    public List<String> transferFile(String path, boolean isNewName, boolean isOverride, MultipartFile... files) {
        List<String> resultList = new ArrayList<>(); // 类型检验不通过时，要在 catch
        try {
            String fileName = null;
            String fileExtNames = null;
            String fileAbsolutePath = null;
            for (MultipartFile mFile : files) {
                fileName = mFile.getOriginalFilename();
                if (ZKStringUtils.isEmpty(fileName)) {
                    log.error("[>_<20191217-1919-001] 文件上传失败失败，无 OriginalFilename ");
                    continue;
                }
                if (isNewName) {
                    if (fileName.lastIndexOf(".") != -1) {
                        fileExtNames = fileName.substring(fileName.lastIndexOf("."));
                    }
                    fileExtNames = fileExtNames == null ? "" : fileExtNames;
                    fileName = UUID.randomUUID().toString() + fileExtNames;
                }
                fileAbsolutePath = this.transferFile(path, fileName, isOverride, mFile);
                if (!ZKStringUtils.isEmpty(fileAbsolutePath)) {
                    resultList.add(fileAbsolutePath);
                }
            }
            return resultList;
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    /**
     * 返回 null 时，表示文件不存在或为 null
     * 
     * @param filePathAndName
     * @return
     * @see com.zk.core.commons.ZKFileTransfer#getFile(java.lang.String)
     */
//    @Override
    public File getFile(String fileId) {
        File file = new File(fileId);
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    @Override
    public boolean getFile(String fileId, OutputStream os) {
        File f = getFile(fileId);
        if (f == null) {
            return false;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            ZKStreamUtils.readAndWrite(fis, os);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            ZKStreamUtils.closeStream(fis);
        }

        return true;
    }

}
