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
 * @Title: ZKUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 27, 2019 9:42:15 AM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.File;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.encrypt.utils.ZKEncryptUtils;

/** 
* @ClassName: ZKUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKUtils {

    protected static Logger logger = LogManager.getLogger(ZKUtils.class);

    public static boolean isTrue(String value) {
        return value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t")
                || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("enabled") || value.equalsIgnoreCase("y")
                || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on"));
    }

    /**
     * 生成一个请求标记；防重复提交时，使用；
     *
     * @Title: genRequestSign
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 27, 2019 9:42:50 AM
     * @return
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public static String genRequestSign() throws NoSuchAlgorithmException {
        String source = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";
        byte[] sign = ZKEncryptUtils.encryptDigest(source.getBytes(), null, ZKEncryptUtils.DIGEST_MODE.MD5);
        return ZKEncodingUtils.encodeHex(sign);
    }

    /**
     * 根据文件相对 classpath 路径，取文件绝对路径;
     * 
     * 此方法不会判断文件是否存在
     *
     * @Title: getAbsolutePath
     * @Description: TODO(simple description this method what to do.)
     * @author zhenx
     * @date Sep 7, 2017 5:36:08 PM
     * @param relativePathAndFileName
     *            文件相对 classpath 的路径及文件名
     * @return
     * @return String
     */
    public static String getAbsolutePath(String relativePathAndFileName) {
        URL resultPath = getClassPath(relativePathAndFileName);
        String path = null;
        if (resultPath == null) {
            resultPath = getClassPath();

//          path = resultPath.getFile() + File.separator + relativePathAndFileName;
            if (relativePathAndFileName.startsWith(File.separator)) {
                path = resultPath.getFile() + relativePathAndFileName.substring(1);
            }
            else {
                path = resultPath.getFile() + relativePathAndFileName;
            }
            logger.info("[^_^:201709071756-001] file path: {}", path);
        }
        else {
            path = resultPath.getFile();
            logger.info("[^_^:201709071756-002] file path: {}", path);
        }
        return path;
    }

    public static URL getClassPath() {
        return getClassPath("");
    }

    public static URL getClassPath(String resource) {
        return Thread.currentThread().getContextClassLoader().getResource(resource);
    }

}
