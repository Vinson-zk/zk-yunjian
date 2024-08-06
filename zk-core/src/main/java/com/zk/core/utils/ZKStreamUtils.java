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
 * @Title: ZKStreamUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:15:20 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
* @ClassName: ZKStreamUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKStreamUtils {

    protected static Logger log = LogManager.getLogger(ZKStreamUtils.class);

    public static final int ReadBlockLength = 8192;

    public static long readAndWrite(String content, OutputStream os) throws IOException {
        return ZKStreamUtils.readAndWrite(content, os, -1);
    }

    public static long readAndWrite(String content, OutputStream os, int readLength) throws IOException {
        return ZKStreamUtils.readAndWrite(content.getBytes(), os, readLength);
    }

    public static long readAndWrite(byte[] bs, OutputStream os) throws IOException {
        return ZKStreamUtils.readAndWrite(bs, os, -1);
    }

    public static long readAndWrite(byte[] bs, OutputStream os, long readLength) throws IOException {
        ByteArrayInputStream byteInputStream = null;
        try {
            byteInputStream = new ByteArrayInputStream(bs);
            return ZKStreamUtils.readAndWrite(byteInputStream, os, readLength);
        } finally {
            if (byteInputStream != null) {
                ZKStreamUtils.closeStream(byteInputStream);
            }
        }
    }

    public static long readAndWrite(InputStream is, OutputStream os) throws IOException {
        return ZKStreamUtils.readAndWrite(is, os, -1);
    }

    public static long readAndWrite(InputStream is, OutputStream os, long readLength) throws IOException {
        return readAndWrite(is, os, 0, readLength);
    }

    /**
     * 从输入流中读取指定长度的内容到输出流中，不会关闭两个流，由提供者自行处理；
     *
     * @Title: readAndWrite
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 6, 2024 3:14:02 PM
     * @param is
     *            输入流
     * @param os
     *            输出流
     * @param start
     *            路过的字节长度
     * @param readLength
     *            读取的最大长度；<= 0 不限制
     * @throws IOException
     * @return long
     */
    public static long readAndWrite(InputStream is, OutputStream os, long start, long readLength) throws IOException {
        if (start > 0) {
            is.skip(start);
        }
        if (readLength > 0) {
            return doReadAndWrite(is, os, readLength);
        }
        else {
            return doReadAndWrite(is, os);
        }
    }

    /**
     * 从输入流中读取指定长度的内容
     *
     * @Title: doReadAndWrite
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 6, 2024 3:11:50 PM
     * @param is
     *            输入流
     * @param os
     *            输出流
     * @param readLength
     *            指定读取的长度
     * @throws IOException
     * @return long
     */
    private static long doReadAndWrite(InputStream is, OutputStream os, long readLength) throws IOException {
        // 单次读取的长度
        int len = -1;
        // 读取字节数组
        byte[] readBytes = new byte[ReadBlockLength];
        // 已读取长度
        long alreadyReadSize = 0;
        // 已读取长度 小需要读取的长度；且输入流中还有长度可以读取；
        while (alreadyReadSize < readLength && (len = is.read(readBytes)) != -1) {
            os.write(readBytes, 0, (readLength - alreadyReadSize) > len ? len : (int) (readLength - alreadyReadSize));
//            os.write(readBytes, 0, len);
            alreadyReadSize += len;
        }
        os.flush();
        return alreadyReadSize;
    }

    /**
     * 从输入流中读取内容到输出流中，读完为止
     *
     * @Title: doReadAndWrite
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 6, 2024 3:12:45 PM
     * @param is
     *            输入流
     * @param os
     *            输出流
     * @throws IOException
     * @return long
     */
    private static long doReadAndWrite(InputStream is, OutputStream os) throws IOException {
        // 单次读取的长度
        int len = -1;
        // 读取字节数组
        byte[] readBytes = new byte[ReadBlockLength];
        // 已读取长度
        long alreadyReadSize = 0;
        while ((len = is.read(readBytes)) != -1) {
            os.write(readBytes, 0, len);
            alreadyReadSize += len;
        }
        os.flush();
        return alreadyReadSize;
    }

    /**
     * 关闭流
     *
     * @Title: cloaseStream
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 18, 2019 12:24:20 PM
     * @param is
     * @return void
     */
    public static void closeStream(InputStream is) {
        if (is != null) {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 关闭流
     *
     * @Title: cloaseStream
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 18, 2019 12:24:36 PM
     * @param os
     * @return void
     */
    public static void closeStream(OutputStream os) {
        if (os != null) {
            IOUtils.closeQuietly(os);
        }
    }

}
