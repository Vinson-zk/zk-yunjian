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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.commons.ZKFileType;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.sub.ZKStreamMaxLengthException;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKFileUtils;

/**
 * 单元测试在
 * 
 * @ClassName: ZKDiskFileTransfer
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKDiskFileTransfer extends ZKAbstractFileTransfer {

    private final Logger log = LogManager.getLogger(this.getClass());

    /**
     * @param minSize
     * @param maxSize
     *            -1 不限制；0 默认 10M 限制；
     * @param checkMode
     * @param extTypes
     */
    public ZKDiskFileTransfer(int minSize, int maxSize, int checkMode, String[] extTypes) {
        super(minSize, maxSize, checkMode, extTypes);
    }

//    @Override
//    public List<String> transferFile(String path, boolean isNewName, boolean isOverride,
//            Collection<MultipartFile> files) {
//        return this.transferFile(path, isNewName, isOverride, files.toArray(new MultipartFile[files.size()]));
//    }
//
//    public List<String> transferFile(String path, boolean isNewName, boolean isOverride, MultipartFile... files) {
//        List<String> resultList = new ArrayList<>(); // 类型检验不通过时，要在 catch
//        try {
//            String fileAbsolutePath = null;
//            for (MultipartFile mFile : files) {
//                fileAbsolutePath = this.transferFile(path, null, isNewName, isOverride, mFile);
//                if (!ZKStringUtils.isEmpty(fileAbsolutePath)) {
//                    resultList.add(fileAbsolutePath);
//                }
//            }
//            return resultList;
//        }
//        catch(Exception e) {
//            throw ZKExceptionsUtils.unchecked(e);
//        }
//    }
//
//    public String transferFile(String path, String fileName, boolean isNewName, boolean isOverride,
//            MultipartFile mFile) {
//
//        File file = null;
//        try {
//            if (ZKStringUtils.isEmpty(fileName)) {
//                fileName = mFile.getOriginalFilename();
//            }
//            if (isNewName) {
//                fileName = ZKFileUtils.newFileName(null, fileName);
//            }
//            if (ZKStringUtils.isEmpty(fileName)) {
//                log.error("[>_<20191217-1919-001] 文件上传失败失败，无 OriginalFilename ");
//                throw ZKCodeException.as("zk.000008", "文件名不能为空。");
//            }
//
//            this.checkFileSize(mFile.getSize());
//            if (ZKStringUtils.isEmpty(fileName)) {
//                log.error("[>_<20191217-1919-001] 文件上传失败失败，无文件名！");
//                return null;
//            }
//            file = ZKFileUtils.createFile(path, fileName, isOverride);
//            mFile.transferTo(file);
//            this.checkFileType(ZKFileUtils.getFileType(file));
//
//            return file.getAbsolutePath();
//        }
//        catch(Exception e) {
//            if (file != null) {
//                file.delete();
//            }
//            throw ZKExceptionsUtils.unchecked(e);
//        }
//    }

    // ====================================================================
//    @Override
//    public String transferFile(InputStream inputStream, String path, String fileName, boolean isOverride,
//            boolean appendFlag) {
//        return this.transferFile(inputStream, path, fileName, isOverride, appendFlag, null, null);
//    }

    @Override
    public String transferFile(InputStream inputStream, String path, String fileName, boolean isOverride,
            boolean appendFlag, long[] outFileSize, ZKFileType[] outFileType) {
        File file = ZKFileUtils.createFile(path, fileName, isOverride);
        if (file != null) {
            try {
                /* 1、写文件 */
                long fileSize = ZKFileUtils.writeFile(this.getMaxSize(), inputStream, file, appendFlag);
                /* 2、校验文件大小 */
                this.checkFileSize(fileSize);
                /* 3、校验文件格式 */
                ZKFileType fileType = ZKFileUtils.getFileType(file);
                this.checkFileType(fileType);

                if (outFileSize != null) {
                    outFileSize[0] = fileSize;
                }
                if (outFileType != null) {
                    outFileType[0] = fileType;
                }
            }
            catch(ZKStreamMaxLengthException e) {
                log.error("[>_<：20240229-2322-002] 文件上传失败; 长度超出了限制{}。", e.getMaxLength());
                throw ZKBusinessException.as("zk.000007", null, e.getMaxLength());
            }
            catch(Exception e) {
                log.error("[>_<20191217-1006-001] 文件大小校验失败，删除文件");
                file.delete();
                throw ZKExceptionsUtils.unchecked(e);
            }
        }
        return file.getAbsolutePath();
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
        log.info("[^_^:20250120-1816-001] getFile: {}", fileId);
        File file = new File(fileId);
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    /**
     * (not Javadoc)
     * 
     * @param fileId
     * @return
     * @see com.zk.core.commons.ZKFileTransfer#del(java.lang.String)
     */
    @Override
    public boolean del(String fileId) {
        File file = new File(fileId);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    @Override
    public long getFile(String fileId, OutputStream os) {
        File f = getFile(fileId);
        if (f == null) {
            return -1;
        }

        try {
            return ZKFileUtils.readFile(f, os);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        catch(IOException e) {
            e.printStackTrace();
            return -1;
        }

    }

}
