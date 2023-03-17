/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKAbstractFileTransfer.java 
* @author Vinson 
* @Package com.zk.core.commons.support 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 10, 2023 10:55:00 AM 
* @version V1.0 
*/
package com.zk.core.commons.support;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.ZKFileType;
import com.zk.core.utils.ZKJsonUtils;

/** 
* @ClassName: ZKAbstractFileTransfer 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKAbstractFileTransfer implements ZKFileTransfer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final long DEFUALT_MAX_SIZE = 10 * 1024 * 1024;// 10M

    protected static final long DEFUALT_MIN_SIZE = -1; //

    public static interface FileTypeCheckMode {

        public static final int disabled = -1; // 不检测

        public static final int allow = 0; // 白名单方式检测；即 指定可以上传的文件类型；

        public static final int ignore = 1; // 黑名单方式检测；即 指定不能上传的文件类型；
    }

    /**
     * 上传文件最小限制；-1，不限制
     */
    private long minSize;

    /**
     * 上传文件最大限制；-1，不限制
     */
    private long maxSize;

    /**
     * 文件类型，检查模式；0-白名单检测；1-黑名单检测；-1-不检测;
     */
    private int checkMode;

    /**
     * 文件类型检测
     */
    private ZKFileType[] fileTypes = null; // 文件类型；

    public ZKAbstractFileTransfer() {
        this.minSize = DEFUALT_MIN_SIZE;
        this.maxSize = DEFUALT_MAX_SIZE;
        this.checkMode = FileTypeCheckMode.disabled;
        this.fileTypes = new ZKFileType[] {};
    }

    /**
     * @param minSize
     * @param maxSize
     *            -1 不限制；0 默认 10M 限制；
     * @param checkMode
     * @param extTypes
     */
    public ZKAbstractFileTransfer(long minSize, long maxSize, int checkMode, String[] extTypes) {
        this.minSize = minSize;
        this.maxSize = maxSize == 0 ? DEFUALT_MAX_SIZE : maxSize;
        this.checkMode = checkMode;
        if (fileTypes != null) {
            this.fileTypes = ZKFileType.getFileTypeByString(extTypes);
        }
        else {
            this.fileTypes = new ZKFileType[] {};
        }
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
