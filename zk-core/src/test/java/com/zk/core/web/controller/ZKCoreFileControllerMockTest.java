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
 * @Title: ZKCoreFileControllerMockTest.java 
 * @author Vinson 
 * @Package com.zk.core.web.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:32:14 PM 
 * @version V1.0   
*/
package com.zk.core.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.zk.core.commons.ZKContentType;
import com.zk.core.helper.ZKCoreTestHelperMvcSpringBootMain;
import com.zk.core.helper.configuration.ZKCoreChildConfiguration;
import com.zk.core.helper.configuration.ZKCoreParentConfiguration;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKCoreFileControllerMockTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZKCoreParentConfiguration.class,
        ZKCoreChildConfiguration.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ZKCoreFileControllerMockTest {

    static {
        try {
            ZKCoreTestHelperMvcSpringBootMain.exit();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @Autowired
    private MockMvc mvc;

    private static String baseUrl = "/zk/c/file";

//    @Before
//    public void step() {
//        baseUrl = String.format("/%s/%s/file", ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
//                ZKEnvironmentUtils.getString("zk.path.core", "c"));
//    }

    final String boundary = "7678sadfasdfaf9876ad7f8a";

    private final static String filePath = ZKCoreFileControllerTest.filePath;

    private final static String filePathSource = ZKCoreFileControllerTest.filePathSource;

    private final static String filePathTarget = ZKCoreFileControllerTest.filePathTarget;

    @Test
    public void testUploadStream() {
        // 删除测试数据，防止影响测试结果
        ZKCoreFileControllerTest.deleteTestData();

        try {
            MockHttpServletRequestBuilder mockReqBuilder = null;
            MvcResult mvcRes = null;
            MockHttpServletResponse mockRes = null;
            String resStr = null;
            StringBuffer strBuf = null;
            String fileContent = null;
            String contentType = null;
            String fileName = null;
            File file = null;

            String url = "";
            /*** 二进制流上传文件测试 ***/
            contentType = ZKContentType.OCTET_STREAM.toString();
            url = baseUrl + "/uploadStream";
            mockReqBuilder = MockMvcRequestBuilders.post(url);

            fileName = "mock mvc uploadStream 文件.txt";
            mockReqBuilder.contentType(contentType);
//            mockReqBuilder.characterEncoding("UTF-8"); // 不会如 Multipart 一样，不会生效
            // 请求头中是以 ISO-8859-1 编码的，不能改变，在后台接收后，再自行转换为 UTF-8
            mockReqBuilder.header("fileName", ZKStringUtils.encodedString(fileName, "ISO-8859-1"));
            strBuf = new StringBuffer();

//            file = new File(filePath + File.separator + filePathSource + File.separator + fileName);
            fileContent = "池中濯足水，门外打头风。";
//            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);

            strBuf.append(fileContent);
            mockReqBuilder.content(strBuf.toString());
            mvcRes = mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            mockRes = mvcRes.getResponse();
            resStr = mockRes.getContentAsString();
            System.out.println("[^_^:20191217-2146-001] resStr: " + resStr);
            file = new File(filePath + File.separator + filePathTarget + File.separator + fileName);
            TestCase.assertEquals(file.getAbsolutePath(), resStr);
            resStr = new String(ZKFileUtils.readFile(file));
            TestCase.assertEquals(fileContent, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testUploadMultipart() {

        // 删除测试数据，防止影响测试结果
        ZKCoreFileControllerTest.deleteTestData();

        try {
            MockHttpServletRequestBuilder mockReqBuilder = null;
            MvcResult mvcRes = null;
            MockHttpServletResponse mockRes = null;
            String resStr = null;
            StringBuffer strBuf = null;
            String fileContent = null;
            List<String> fs = new ArrayList<>();
            String contentType = null;
            File file = null;
            String fileName = "";

            String f1Name = "f1";
            String fsName = "fs";

            String url = "";
            /*** 二进制流上传测试 multipart ***/
            url = baseUrl + "/uploadMultipart";
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.contentType(ZKContentType.MULTIPART_FORM_DATA_UTF8 + "; boundary=" + boundary);
            strBuf = new StringBuffer();
            contentType = ZKContentType.X_FORM.toString();
            /**
             * Content-Disposition: 说明
             * 
             * @name 参数名
             * @fileName 文件名
             */
            // 文件 f1；
            fileContent = "孤犊触乳，骄子骂母";
            fileName = "mock mvc stream uploadMultipart 文件1.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("\r\n").append("--").append(boundary);
            strBuf.append("\r\n").append(
                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", f1Name, fileName));
            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
            strBuf.append("\r\n").append(fileContent);
            // 文件 fs
            fileContent = "两鬓风霜，途次早行之客。";
            fileName = "mock mvc stream uploadMultipart 文件2.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("\r\n").append("--").append(boundary);
            strBuf.append("\r\n").append(
                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", fsName, fileName));
            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
            strBuf.append("\r\n").append(fileContent);
            // 文件 fs
            fileContent = "一蓑烟雨，溪边晚钓之翁。";
            fileName = "mock mvc stream uploadMultipart 文件3.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("\r\n").append("--").append(boundary);
            strBuf.append("\r\n").append(
                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", fsName, fileName));
            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
            strBuf.append("\r\n").append(fileContent);
            // 上传文件 结尾
            strBuf.append("\r\n").append("--").append(boundary).append("--");

            mockReqBuilder.content(strBuf.toString());
            mvcRes = mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            mockRes = mvcRes.getResponse();
            resStr = mockRes.getContentAsString();
            System.out.println("[^_^:20191217-1549-001] resStr: " + resStr);
            fs = ZKJsonUtils.jsonStrToList(resStr);
            TestCase.assertEquals(3, fs.size());

            for (String fStr : fs) {
                file = new File(fStr);
                resStr = new String(ZKFileUtils.readFile(file));
                file = new File(fStr.replace(filePathTarget, filePathSource));
                fileContent = new String(ZKFileUtils.readFile(file));
                TestCase.assertEquals(fileContent, resStr);
            }

            /*** MultipartFile 上传测试 multipart ***/
            MockMultipartHttpServletRequestBuilder mockMultipartBuilder = null;
            MockMultipartFile mFile = null;
            FileInputStream fIs = null;

            mockMultipartBuilder = MockMvcRequestBuilders.multipart(url);
            mockMultipartBuilder.characterEncoding("UTF-8");

            fileContent = "沿对革，异对同，白叟对黄童。";
            fileName = "mock mvc multiport uploadMultipart 文件1.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            fIs = new FileInputStream(file);
            mFile = new MockMultipartFile(f1Name, fileName, null, fIs);
            mockMultipartBuilder.file(mFile);

            fileContent = "江风对海雾，牧子对渔翁。";
            fileName = "mock mvc multiport uploadMultipart 文件2.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            fIs = new FileInputStream(file);
            mFile = new MockMultipartFile(fsName, fileName, null, fIs);
            mockMultipartBuilder.file(mFile);

            fileContent = "彦巷陋，阮途穷，冀北对辽东。";
            fileName = "mock mvc multiport uploadMultipart 文件3.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            fIs = new FileInputStream(file);
            mFile = new MockMultipartFile(fsName, fileName, null, fIs);
            mockMultipartBuilder.file(mFile);

            mvcRes = mvc.perform(mockMultipartBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            mockRes = mvcRes.getResponse();
            resStr = mockRes.getContentAsString();
            System.out.println("[^_^:20191217-1549-002] resStr: " + resStr);
            fs = ZKJsonUtils.jsonStrToList(resStr);
            TestCase.assertEquals(3, fs.size());

            for (String fStr : fs) {
                file = new File(fStr);
                resStr = new String(ZKFileUtils.readFile(file));
                file = new File(fStr.replace(filePathTarget, filePathSource));
                fileContent = new String(ZKFileUtils.readFile(file));
                TestCase.assertEquals(fileContent, resStr);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 自行解析 Multipart 请求；不能配置 MultipartResolver 适配器
     */
//    @Test
    public void testUploadMultipartNoResolver() {

        // 删除测试数据，防止影响测试结果
        ZKCoreFileControllerTest.deleteTestData();

        try {
            MockHttpServletRequestBuilder mockReqBuilder = null;
            MvcResult mvcRes = null;
            MockHttpServletResponse mockRes = null;
            String resStr = null;
            StringBuffer strBuf = null;
            String fileContent = null;
            List<String> fs = new ArrayList<>();
            String contentType = null;
            String f1Name = "f1";
            String fsName = "fs";
            File file = null;
            String fileName = "";

            String url = "";
            /*** 二进制流上传测试 multipart ***/
            url = baseUrl + "/uploadMultipartNoResolver";
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.contentType(ZKContentType.MULTIPART_FORM_DATA + "; boundary=" + boundary);
            strBuf = new StringBuffer();
            contentType = ZKContentType.OCTET_STREAM.toString();
            /**
             * Content-Disposition: 说明
             * 
             * @name 参数名
             * @fileName 文件名
             */
            // 文件 f1；
            fileContent = "孤犊触乳，骄子骂母";
            fileName = "stream uploadMultipart 文件1.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("\r\n").append("--").append(boundary);
            strBuf.append("\r\n").append(
                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", f1Name, fileName));
            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
            fileContent = new String(ZKFileUtils.readFile(file));
            strBuf.append("\r\n").append(fileContent);
            // 文件 fs
            fileContent = "两鬓风霜，途次早行之客。";
            fileName = "stream uploadMultipart 文件2.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("\r\n").append("--").append(boundary);
            strBuf.append("\r\n").append(
                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", fsName, fileName));
            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
            fileContent = new String(ZKFileUtils.readFile(file));
            strBuf.append("\r\n").append(fileContent);
            // 文件 fs
            fileContent = "一蓑烟雨，溪边晚钓之翁。";
            fileName = "stream uploadMultipart 文件3.txt";
            file = ZKFileUtils.createFile(filePath + File.separator + filePathSource, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("\r\n").append("--").append(boundary);
            strBuf.append("\r\n").append(
                    String.format("Content-Disposition: form-data;name=\"%s\";fileName=\"%s\"", fsName, fileName));
            strBuf.append("\r\n").append("Content-Type:" + contentType + "\r\n");
            fileContent = new String(ZKFileUtils.readFile(file));
            strBuf.append("\r\n").append(fileContent);
            // 上传文件 结尾
            strBuf.append("\r\n").append("--").append(boundary).append("--");

            mockReqBuilder.content(strBuf.toString());
            mvcRes = mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            mockRes = mvcRes.getResponse();
            resStr = mockRes.getContentAsString();
            System.out.println("[^_^:20191217-1549-001] resStr: " + resStr);
            fs = ZKJsonUtils.jsonStrToList(resStr);
            TestCase.assertEquals(3, fs.size());
            for (String fStr : fs) {
                file = new File(fStr);
                resStr = new String(ZKFileUtils.readFile(file));
                file = new File(fStr.replace(filePathTarget, filePathSource));
                fileContent = new String(ZKFileUtils.readFile(file));
                TestCase.assertEquals(fileContent, resStr);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
