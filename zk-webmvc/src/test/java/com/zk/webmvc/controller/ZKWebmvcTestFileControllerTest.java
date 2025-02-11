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
import java.util.IdentityHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.test.file.ZKFileUploadTest;
import com.zk.webmvc.helper.ZKWebmvcTestHelperMvcSpringBootMain;

import junit.framework.TestCase;

/**
 * @ClassName: ZKWebmvcTestFileControllerTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ZKWebmvcTestHelperMvcSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZKWebmvcTestFileControllerTest {

    static {
        try {
            ZKWebmvcTestHelperMvcSpringBootMain.exit();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @Value("${zk.webmvc.file.upload.maxFileSize}")
    long maxFileSize;

    @Value("${zk.webmvc.file.upload.maxRequestSize}")
    long maxRequestSize;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/file", port,
                ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
                ZKEnvironmentUtils.getString("zk.path.webmvc", "c"));
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
        ZKWebmvcTestFileControllerTest.deleteTestData();
        String url = baseUrl + "/uploadStream";
        ZKFileUploadTest.testUploadStream(url, testRestTemplate);
    }

    @Test
    public void testUploadMultipart() {
        // 删除测试数据，防止影响测试结果
        ZKWebmvcTestFileControllerTest.deleteTestData();
        String url = baseUrl + "/uploadMultipart";
        ZKFileUploadTest.testUploadMultipart(url, testRestTemplate);
    }

    @Test
    public void testGet() {
        // 删除测试数据，防止影响测试结果
        ZKWebmvcTestFileControllerTest.deleteTestData();
        String urlUpload = baseUrl + "/uploadMultipart";
        String urlGet = baseUrl + "/getFile";
        ZKFileUploadTest.testGet(urlUpload, urlGet, testRestTemplate);
    }

    // 测试 ZKHttpApiUtils，同步测试 上传取文件类型和 contentType
    @Test
    public void testZKHttpApiUtils() {
        // 删除测试数据，防止影响测试结果
        ZKWebmvcTestFileControllerTest.deleteTestData();
        String url = baseUrl + "/uploadMultipart";
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

        ZKFileUploadTest.testZKHttpApiUtils(url, files);
    }

    // 测试 文件过大
    @Test
    public void testMaxFileSize() {

        final String boundary = "7678sadfasdfaf9876ad7f8a";

        try {

            HttpEntity<String> requestEntity;
            ResponseEntity<String> response;
            HttpHeaders headers = null;
            int resStatusCode;
            String resStr;
            ZKMsgRes msg = null;

            String contentType = null;
//            String fileContent = null;

            String f1Name = null;
            String fileName = null;

            StringBuffer strBuf = null;

            String url = baseUrl + "/uploadMultipart";

            contentType = ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr();

            headers = new HttpHeaders();
            headers.set("Content-Type", contentType + "; boundary=" + boundary);

            /* 1、单个文件过大 ****************************************/
            f1Name = "f1";
            fileName = "testMaxSize文件.txt";
            strBuf = new StringBuffer();
            // 上传开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append(String.format("name=\"%s\"; filename=\"%s\"", f1Name, fileName)).append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.TEXT_PLAIN_UTF8.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 请求内容
            for (long i = 0; i < this.maxFileSize; ++i) {
                strBuf.append('a');
            }
            strBuf.append('a');
            strBuf.append("\r\n");
            // 上传文件 结尾
            strBuf.append("--").append(boundary).append("--").append("\r\n");

            requestEntity = new HttpEntity<>(strBuf.toString(), headers);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            System.out.println("[^_^:20240821-2212-001] resStatusCode: " + resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240821-2212-002] response: " + resStr);
            msg = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.1", msg.getCode());

            /* 2、文件整体过大 ****************************************/
            f1Name = "f1";
            fileName = "testMaxSize文件.txt";
            strBuf = new StringBuffer();
            
            // 上传开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append(String.format("name=\"%s\"; filename=\"%s\"", f1Name, fileName)).append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.TEXT_PLAIN_UTF8.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 请求内容
            long i = 0;
            for (; i < this.maxFileSize; ++i) {
                strBuf.append('a');
            }
            strBuf.append("\r\n");
            System.out.println("[^_^:20240821-2215-001]----------------- i: " + i);
            f1Name = "fs";
            fileName = "testMaxSize文件";
            long s = this.maxFileSize + 332;
            for (; s < this.maxRequestSize; s = s + this.maxFileSize) {
                // 上传开始 boundary
                strBuf.append("--").append(boundary).append("\r\n");
                // 请求头
                strBuf.append("Content-Disposition: form-data;")
                        .append(String.format("name=\"%s\"; filename=\"%s\"", f1Name, fileName + s + ".txt"))
                        .append("\r\n");
                strBuf.append("Content-Type:" + ZKContentType.TEXT_PLAIN_UTF8.toContentTypeStr() + "\r\n");
                // 请求头结束
                strBuf.append("\r\n");
                // 请求内容
                for (i = 0; i < this.maxFileSize; ++i) {
                    strBuf.append('a');
                }
                strBuf.append("\r\n");
            }
            System.out.println("[^_^:20240821-2215-002]----------------- s: " + s);

            // 上传开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append(String.format("name=\"%s\"; filename=\"%s\"", f1Name, fileName + "last.txt"))
                    .append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.TEXT_PLAIN_UTF8.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 请求内容
            strBuf.append('a');
            strBuf.append("\r\n");

            // 上传文件 结尾
            strBuf.append("--").append(boundary).append("--").append("\r\n");

            requestEntity = new HttpEntity<>(strBuf.toString(), headers);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            System.out.println("[^_^:20240821-2212-001.1] resStatusCode: " + resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240821-2212-001.2] response: " + resStr);
            msg = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.1", msg.getCode());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

//    /**
//     * 自行解析 Multipart 请求；不能配置 MultipartResolver 适配器
//     */
////    @Test
//    public void testUploadMultipartNoResolver() {
//
//        // 删除测试数据，防止影响测试结果
//        ZKWebmvcTestFileControllerTest.deleteTestData();
//
//        try {
//
//            HttpEntity<String> requestEntity;
//            ResponseEntity<String> response;
//            HttpHeaders headers = null;
//            int resStatusCode;
//            String resStr;
//
//            StringBuffer strBuf = null;
//            List<String> fs = new ArrayList<>();
//            String contentType = null;
//            String fileContent = "";
//            String f1Name = "f1";
//            String fsName = "fs";
//            File file = null;
//            String fileName = "";
//
//            String url = "";
//            /*** 二进制流上传测试 multipart ***/
//            url = baseUrl + "/uploadMultipartNoResolver";
//            strBuf = new StringBuffer();
//            contentType = ZKContentType.OCTET_STREAM.toContentTypeStr();
//            /**
//             * Content-Disposition: 说明
//             * 
//             * @name 参数名
//             * @fileName 文件名
//             */
//            // 文件 f1；
//            fileContent = "孤犊触乳，骄子骂母";
//            fileName = "stream uploadMultipart 文件1.txt";
//            file = ZKFileUtils.createFile(
//                    ZKFileUploadTest.uploadFileRootPath + File.separator + ZKFileUploadTest.source, fileName, true);
//            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
//            strBuf.append("\r\n").append("--").append(ZKFileUploadTest.boundary);
//            strBuf.append("\r\n").append(
//                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", f1Name, fileName));
//            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
//            fileContent = new String(ZKFileUtils.readFile(file));
//            strBuf.append("\r\n").append(fileContent);
//            // 文件 fs
//            fileContent = "两鬓风霜，途次早行之客。";
//            fileName = "stream uploadMultipart 文件2.txt";
//            file = ZKFileUtils.createFile(
//                    ZKFileUploadTest.uploadFileRootPath + File.separator + ZKFileUploadTest.source, fileName, true);
//            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
//            strBuf.append("\r\n").append("--").append(ZKFileUploadTest.boundary);
//            strBuf.append("\r\n").append(
//                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", fsName, fileName));
//            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
//            fileContent = new String(ZKFileUtils.readFile(file));
//            strBuf.append("\r\n").append(fileContent);
//            // 文件 fs
//            fileContent = "一蓑烟雨，溪边晚钓之翁。";
//            fileName = "stream uploadMultipart 文件3.txt";
//            file = ZKFileUtils.createFile(
//                    ZKFileUploadTest.uploadFileRootPath + File.separator + ZKFileUploadTest.source, fileName, true);
//            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
//            strBuf.append("\r\n").append("--").append(ZKFileUploadTest.boundary);
//            strBuf.append("\r\n").append(
//                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", fsName, fileName));
//            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
//            fileContent = new String(ZKFileUtils.readFile(file));
//            strBuf.append("\r\n").append(fileContent);
//            // 上传文件 结尾
//            strBuf.append("\r\n").append("--").append(ZKFileUploadTest.boundary).append("--");
//
//            requestEntity = new HttpEntity<>(strBuf.toString(), headers);
//            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
//            resStatusCode = response.getStatusCode().value();
//            System.out.println("[^_^:20231226-2318-001] resStatusCode: " + resStatusCode);
//            resStr = response.getBody();
//            System.out.println("[^_^:20231226-2318-001] response: " + resStr);
//            TestCase.assertEquals(200, resStatusCode);
//
//            fs = ZKJsonUtils.parseList(resStr);
//            TestCase.assertEquals(3, fs.size());
//            for (String fStr : fs) {
//                file = new File(fStr);
//                resStr = new String(ZKFileUtils.readFile(file));
//                file = new File(fStr.replace(File.separator + ZKFileUploadTest.upload + File.separator,
//                        File.separator + ZKFileUploadTest.source + File.separator));
//                fileContent = new String(ZKFileUtils.readFile(file));
//                TestCase.assertEquals(fileContent, resStr);
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            TestCase.assertTrue(false);
//        }
//    }

}
