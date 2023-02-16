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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @ClassName: ZKStreamUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKStreamUtils {

    protected static Logger log = LoggerFactory.getLogger(ZKStreamUtils.class);

    /**
     * 从输入流写到输出流，不会关闭两个流，两个由提供者自行处理；
     *
     * @Title: readAndWrite
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 18, 2019 12:18:05 PM
     * @param is
     * @param os
     * @return long
     * @throws IOException
     */
    public static long readAndWrite(InputStream is, OutputStream os) throws IOException {
        int len = -1;
        long fileSize = 0;
        byte[] result = new byte[8192];
        while ((len = is.read(result)) != -1) {
            fileSize += len;
            os.write(result, 0, len);
        }
        os.flush();
        return fileSize;
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
