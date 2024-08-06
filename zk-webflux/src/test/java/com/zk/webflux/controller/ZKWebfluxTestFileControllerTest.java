/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKWebfluxTestFileControllerTest.java 
* @author Vinson 
* @Package com.zk.webflux.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2024 10:48:18 PM 
* @version V1.0 
*/
package com.zk.webflux.controller;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.test.file.ZKFileUploadTest;
import com.zk.webflux.helper.ZKWebFluxTestHelperSpringBootMain;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWebfluxTestFileControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ZKWebFluxTestHelperSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZKWebfluxTestFileControllerTest {

    public static void main(String[] args) {
        try {

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            ZKWebFluxTestHelperSpringBootMain.exit();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/file", port,
                ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
                ZKEnvironmentUtils.getString("zk.path.webmvc", "x"));
        System.out.println("[^_^:20191218-1555-001] base url:" + this.baseUrl);
    }

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
            TestCase.assertTrue(deleteFile(
                    new File(ZKFileUploadTest.uploadFileRootPath + File.separator + ZKFileUploadTest.upload)));
            TestCase.assertTrue(deleteFile(
                    new File(ZKFileUploadTest.uploadFileRootPath + File.separator + ZKFileUploadTest.source)));
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

    @Test
    public void testUploadStream() {
        // 删除测试数据，防止影响测试结果
        ZKWebfluxTestFileControllerTest.deleteTestData();
        String url = baseUrl + "/uploadStream";
        ZKFileUploadTest.testUploadStream(url, testRestTemplate);
    }

    @Test
    public void testUploadMultipart() {
        // 删除测试数据，防止影响测试结果
        ZKWebfluxTestFileControllerTest.deleteTestData();
        String url = baseUrl + "/uploadFilePart";
        ZKFileUploadTest.testUploadMultipart(url, testRestTemplate);
    }

    @Test
    public void testGet() {
        // 删除测试数据，防止影响测试结果
        ZKWebfluxTestFileControllerTest.deleteTestData();
        String urlUpload = baseUrl + "/uploadFilePart";
        String urlGet = baseUrl + "/getFile";
        ZKFileUploadTest.testGet(urlUpload, urlGet, testRestTemplate);
    }

    // 测试 ZKHttpApiUtils，同步测试 上传取文件类型和 contentType
    @Test
    public void testZKHttpApiUtils() {
        // 删除测试数据，防止影响测试结果
        ZKWebfluxTestFileControllerTest.deleteTestData();
        String url = baseUrl + "/uploadFilePart";
        Map<File, String> files = new IdentityHashMap<>();
        String fileName;
        File file;
        fileName = "testFile-图片-1.jpg";
        file = new File(ZKFileUploadTest.sourceFileRootPath + File.separator + fileName);
        files.put(file, "f1");

        fileName = "testFile-md-1.md";
        file = new File(ZKFileUploadTest.sourceFileRootPath + File.separator + fileName);
        files.put(file, "fs");
        
        fileName = "testFile-图片-1.jpg";
        file = new File(ZKFileUploadTest.sourceFileRootPath + File.separator + fileName);
        files.put(file, "fs");
        
        // fileName = "jdk-21.0.2_linux-x64_bin.tar.gz";
//        file = new File(ZKFileUploadTest.sourceFileRootPath + File.separator + fileName);
//        files.put(file, "fs");

        ZKFileUploadTest.testZKHttpApiUtils(url, files);
    }

}
