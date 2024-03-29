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
 * @Title: ZKWebmvcTestFileControllerTest.java 
 * @author Vinson 
 * @Package com.zk.webmvc.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:32:21 PM 
 * @version V1.0   
*/
package com.zk.webmvc.controller;

import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @ClassName: ZKWebmvcTestFileControllerTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKWebmvcTestFileControllerTest {

    public final static String sourceFileRootPath = "src/test/resources/testFile";

    public final static String uploadFileRootPath = "target/fileUpload";

    public final static String source = "source";

    public final static String upload = "upload";

    public static boolean deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    deleteFile(f);
                }
                System.out.println("[^_^:20191218-1049-001] 文件上传目标已存在，删除，防止之前的测试影响现在的测试结果。" + file.getAbsolutePath());
                return file.delete();
            }
            else {
                System.out.println("[^_^:20191218-1049-001] 目标文件已存在，删除，防止之前的测试影响现在的测试结果。" + file.getAbsolutePath());
                return file.delete();
            }
        }
        return true;
    }

    public static void deleteTestData() {
        try {
            TestCase.assertTrue(deleteFile(new File(uploadFileRootPath + File.separator + upload)));
            TestCase.assertTrue(deleteFile(new File(uploadFileRootPath + File.separator + source)));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void test() {
        try {
//            deleteTestData();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
