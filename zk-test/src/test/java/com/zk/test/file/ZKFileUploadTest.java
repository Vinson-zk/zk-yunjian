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
* @Title: ZKFileUploadTest.java 
* @author Vinson 
* @Package com.zk.core.helper.file 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 5, 2024 2:05:13 PM 
* @version V1.0 
*/
package com.zk.test.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.zk.core.commons.ZKContentType;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKHtmlUtils;
import com.zk.core.web.utils.ZKHttpApiUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKFileUploadTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileUploadTest {

    public final static String boundary = "7678sadfasdfaf9876ad7f8a";

    public final static String sourceFileRootPath = "src/test/resources/testFile";

    public final static String uploadFileRootPath = "target/fileUpload";

    public final static String source = "source";

    public final static String upload = "upload";

    // 二进制流上传文件测试
    public static void testUploadStream(String url, TestRestTemplate testRestTemplate) {
        System.out.println("[^_^:20240204-2238-001] testUploadStream.url: " + url);
        try {
            HttpEntity<String> requestEntity;
            ResponseEntity<String> response;
            HttpHeaders headers = null;
            int resStatusCode;
            String resStr;

            String fileContent = null;
//            String contentType = null;
            String fileName = null;
            File file = null;

//            contentType = ZKContentType.OCTET_STREAM.toContentTypeStr();

            headers = new HttpHeaders();
//            headers.add("Content-type", contentType);
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            fileName = "testUploadStream.uploadStream 文件.txt";
            // 请求头中是以 ISO-8859-1 编码的，不能改变，在后台接收后，再自行转换为 UTF-8
//            headers.add("filename", ZKStringUtils.encodedString(fileName, "ISO-8859-1"));
//            headers.add("filename", fileName);
            // 因为 okhttp3 不支持请求头添加中文，统一使用 url 编码方式解决中文编码问题
            headers.add("filename", ZKHtmlUtils.urlEncode(fileName));

            fileContent = "池中濯足水，门外打头风。";
            requestEntity = new HttpEntity<>(fileContent, headers);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            System.out.println("[^_^:20240204-2238-002] resStatusCode: " + resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240204-2238-003] response: " + resStr);
            TestCase.assertEquals(200, resStatusCode);
            file = new File(uploadFileRootPath + File.separator + upload + File.separator + fileName);
            TestCase.assertEquals(file.getAbsolutePath(), resStr);
            resStr = new String(ZKFileUtils.readFile(file));
            TestCase.assertEquals(fileContent, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // Multipart 上传文件测试
    public static void testUploadMultipart(String url, TestRestTemplate testRestTemplate) {
        System.out.println("[^_^:20240204-2238-011] testUploadMultipart.url: " + url);

        try {

            HttpEntity<String> requestEntity;
            ResponseEntity<String> response;
            HttpHeaders headers = null;
            int resStatusCode;
            String resStr;

            String fileContent = null;
            String fileName = null;
            File file = null;

            StringBuffer strBuf = null;
            List<String> fs = new ArrayList<>();
            String contentType = null;
            String f1Name = "f1";
            String fsName = "fs";

            /* === 上传方式 1 字节流上传 */
            // ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr(): multipart/form-data;charset=UTF-8
            // ZKContentType.MULTIPART_FORM_DATA_UTF8.toString(): multipart/form-data
            // ZKContentType.MULTIPART_FORM_DATA_UTF8.getContentType(): multipart/form-data
            // ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr(): multipart/form-data;charset=UTF-8
            contentType = ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr();

            headers = new HttpHeaders();
            headers.set("Content-Type", contentType + "; boundary=" + boundary);

            strBuf = new StringBuffer();
            /**
             * Content-Disposition: 说明; name=参数名; filename=文件名
             */
            /* 文件 f1 ---------------------------- */
            fileContent = "孤犊触乳，骄子骂母";
            fileName = "testUploadMultipart.stream uploadMultipart 文件1.txt";
            // 写成文件，方便上传测试 对比断言
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + source, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            // 报文开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append(String.format("name=\"%s\"; filename=\"%s\"", f1Name, fileName))
                    .append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.TEXT_PLAIN_UTF8.toContentTypeStr()).append("\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 请求内容
            strBuf.append(fileContent);
            // 本段请求内容结束
            strBuf.append("\r\n");

            /* 文件 fs ---------------------------- */
            // fs1
            fileContent = "两鬓风霜，途次早行之客。";
            fileName = "testUploadMultipart.stream uploadMultipart 文件2.txt";
            // 写成文件，方便上传测试 对比断言
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + source, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("--").append(boundary).append("\r\n");
            strBuf.append("Content-Disposition: form-data;")
                    .append(String.format("name=\"%s\"; filename=\"%s\"", fsName, fileName)).append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.TEXT_PLAIN_UTF8.toContentTypeStr()).append("\r\n");
            strBuf.append("\r\n");
            strBuf.append(fileContent);
            strBuf.append("\r\n");
            // fs2
            fileContent = "一蓑烟雨，溪边晚钓之翁。";
            fileName = "testUploadMultipart.stream uploadMultipart 文件3.txt";
            // 写成文件，方便上传测试 对比断言
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + source, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            strBuf.append("--").append(boundary).append("\r\n");
            strBuf.append("Content-Disposition: form-data;")
                    .append(String.format("name=\"%s\"; filename=\"%s\"", fsName, fileName)).append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.TEXT_PLAIN_UTF8.toContentTypeStr()).append("\r\n");
            strBuf.append("\r\n");
            strBuf.append(fileContent);
            strBuf.append("\r\n");
            // 报文文件 结尾
            strBuf.append("--").append(boundary).append("--").append("\r\n");

            requestEntity = new HttpEntity<>(strBuf.toString(), headers);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            System.out.println("[^_^:20240204-2238-012] resStatusCode: " + resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240204-2238-013] response: " + resStr);
            TestCase.assertEquals(200, resStatusCode);

            fs = ZKJsonUtils.parseList(resStr);
            TestCase.assertEquals(3, fs.size());

            for (String fStr : fs) {
                file = new File(fStr);
                resStr = new String(ZKFileUtils.readFile(file));
                file = new File(fStr.replace(File.separator + upload + File.separator,
                        File.separator + source + File.separator));
                fileContent = new String(ZKFileUtils.readFile(file));
                TestCase.assertEquals(fileContent, resStr);
            }

            /* === 上传方式 2 MultipartFile 上传 */
            HttpEntity<MultiValueMap<String, Object>> requestEntityFile;
            MultiValueMap<String, Object> params;

            params = new LinkedMultiValueMap<String, Object>();

            fileContent = "沿对革，异对同，白叟对黄童。";
            fileName = "testUploadMultipart.multiport uploadMultipart 文件1.txt";
            // 写成文件，方便上传测试 对比断言
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + source, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            params.add(f1Name, new FileSystemResource(file));

            fileContent = "江风对海雾，牧子对渔翁。";
            fileName = "testUploadMultipart.multiport uploadMultipart 文件2.txt";
            // 写成文件，方便上传测试 对比断言
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + source, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            params.add(fsName, new FileSystemResource(file));

            fileContent = "彦巷陋，阮途穷，冀北对辽东。";
            fileName = "testUploadMultipart.multiport uploadMultipart 文件3.txt";
            // 写成文件，方便上传测试 对比断言
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + source, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            params.add(fsName, new FileSystemResource(file));

            requestEntityFile = new HttpEntity<>(params, headers);
            response = testRestTemplate.postForEntity(url, requestEntityFile, String.class);
            resStatusCode = response.getStatusCode().value();
            System.out.println("[^_^:20240204-2238-014] resStatusCode: " + resStatusCode);
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240204-2238-015] response: " + resStr);
            TestCase.assertEquals(200, resStatusCode);
            fs = ZKJsonUtils.parseList(resStr);
            TestCase.assertEquals(3, fs.size());

            for (String fStr : fs) {
                file = new File(fStr);
                resStr = new String(ZKFileUtils.readFile(file));
                file = new File(fStr.replace(File.separator + upload + File.separator,
                        File.separator + source + File.separator));
                fileContent = new String(ZKFileUtils.readFile(file));
                TestCase.assertEquals(fileContent, resStr);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    public static void testGet(String urlUpload, String urlGet, TestRestTemplate testRestTemplate) {
        System.out.println("[^_^:20240204-2238-021] testGet.urlUpload: " + urlUpload);
        System.out.println("[^_^:20240204-2238-021] testGet.urlGet: " + urlGet);

        try {
            HttpEntity<String> requestEntity;
            ResponseEntity<String> response;
            HttpHeaders headers = null;
            int resStatusCode;
            String resStr;

            String fileContent = null;
            String fileName = null;

            String url;
            StringBuffer strBuf = null;
            List<String> fs = new ArrayList<>();
            String contentType = null;
            String f1Name = "f1";

            /*** 字符流上传测试 multipart ***/
            contentType = ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr();

            headers = new HttpHeaders();
            headers.set("Content-Type", contentType + "; boundary=" + boundary);

            strBuf = new StringBuffer();

            // 文件 f1；
            fileContent = "呜呼，胜地不常，盛筵难在，兰亭已矣，梓泽丘墟。" + "临别赠言，幸承恩于伟饯；登高作赋，是所望于群公。" + "敢竭鄙怀，恭疏短引；一言均赋，四韵俱成。"
                    + "请洒潘江，各倾陆海云尔。";
            fileName = "testGet.文件1.txt";

            strBuf.append("\r\n").append("--").append(boundary);
            strBuf.append("\r\n").append(
                    String.format("Content-Disposition: form-data;name=\"%s\";filename=\"%s\"", f1Name, fileName));
            strBuf.append("\r\n").append("Content-Type:" + contentType).append("\r\n");
            strBuf.append("\r\n").append(fileContent);
            // 上传文件 结尾
            strBuf.append("\r\n").append("--").append(boundary).append("--");

            requestEntity = new HttpEntity<>(strBuf.toString(), headers);
            response = testRestTemplate.postForEntity(urlUpload, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            System.out.println("[^_^:20240204-2238-022] resStatusCode: " + resStatusCode);
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240204-2238-023] resStr: " + resStr);
            fs = ZKJsonUtils.parseList(resStr);
            TestCase.assertEquals(1, fs.size());

            // 下载
            ResponseEntity<byte[]> responseBytes;

            url = urlGet + "?fName=" + fileName; // ZKHtmlUtils.urlEncode(fileName)
            responseBytes = testRestTemplate.getForEntity(url, byte[].class);
            resStatusCode = responseBytes.getStatusCode().value();
            System.out.println("[^_^:20240204-2238-024] resStatusCode: " + resStatusCode);
            TestCase.assertEquals(200, resStatusCode);
            resStr = ZKStringUtils.toString(responseBytes.getBody());
//            resStr = new String(resStr.getBytes("ISO-8859-1"));
            System.out.println("[^_^:20240204-2238-025] resStr: " + resStr);
            TestCase.assertEquals(fileContent, resStr);

            url = urlGet + "?fName=" + fileName + "&isDownload=true"; // ZKHtmlUtils.urlEncode(fileName)
            responseBytes = testRestTemplate.getForEntity(url, byte[].class);
            resStatusCode = responseBytes.getStatusCode().value();
            System.out.println("[^_^:20240204-2238-026] resStatusCode: " + resStatusCode);
            TestCase.assertEquals(200, resStatusCode);
            String downloadFileName = responseBytes.getHeaders().get("Content-Disposition").get(0);
            downloadFileName = downloadFileName.substring(downloadFileName.indexOf("filename=") + 9);
            downloadFileName = ZKEncodingUtils.urlDecoder(downloadFileName);
            TestCase.assertEquals(fileName, downloadFileName);
            resStr = ZKStringUtils.toString(responseBytes.getBody());
            System.out.println("[^_^:20240204-2238-027] resStr: " + resStr);
            TestCase.assertEquals(fileContent, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 测试 ZKHttpApiUtils，同步测试 上传取文件类型和 contentType
    public static void testZKHttpApiUtils(String url, Map<File, String> files) {
        System.out.println("[^_^:20240204-2238-031] testZKHttpApiUtils.url: " + url);

        try {
//            Map<String, String> headers;
//            Map<File, String> files;
            File file;
            int resStatusCode;
            ByteArrayOutputStream baos;
            String resStr;
            List<String> fs;

            baos = new ByteArrayOutputStream();
//            headers = new HashMap<>();
//            headers.put("_sign", UUID.randomUUID().toString());
            resStatusCode = ZKHttpApiUtils.uploadFileByMultipart(url, files, null, null, baos, null);
            System.out.println("[^_^:20240204-2238-032] resStatusCode: " + resStatusCode);
            TestCase.assertEquals(200, resStatusCode);
            resStr = baos.toString();
            System.out.println("[^_^:20240204-2238-033] resStr: " + resStr);
            TestCase.assertEquals(200, resStatusCode);

            fs = ZKJsonUtils.parseList(resStr);
            System.out.println("[^_^:20240204-2238-033] fs.size(): " + fs.size());

            System.out.println("[^_^:20240204-2238-034] =======================================");
            for (String fStr : fs) {
                file = new File(fStr);
                System.out.println("[^_^:20240204-2238-035] fStr: " + fStr + " ----------------------------------");
//                fileType = ZKFileUtils.getFileType(file);
//                System.out.println("[^_^:20230527-1101-001] " + fileType.name());
//                contentType = ZKContentType.getContentTypeByFileExt(fileType.name());
//                System.out.println("[^_^:20230527-1101-001] " + contentType.toString());

                file = new File(fStr.replace(upload, "zk"));
                System.out.println("[^_^:20240204-2238-036] " + file.getName());
//                fileType = ZKFileUtils.getFileType(file);
//                System.out.println("[^_^:20230527-1101-001] " + fileType.name());
//                contentType = ZKContentType.getContentTypeByFileExt(fileType.name());
//                System.out.println("[^_^:20230527-1101-001] " + contentType.toString());
            }
            TestCase.assertEquals(files.size(), fs.size());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
