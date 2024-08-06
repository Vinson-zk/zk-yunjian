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
 * @Title: ZKWebmvcTestFileController.java 
 * @author Vinson 
 * @Package com.zk.core.helper.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:30:45 PM 
 * @version V1.0   
*/
package com.zk.webmvc.helper.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKHtmlUtils;
import com.zk.test.file.ZKFileUploadTest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @ClassName: ZKWebmvcTestFileController
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin:zk}/${zk.path.webmvc:c}/file")
public class ZKWebmvcTestFileController {

    @Autowired
    ZKFileTransfer zkFileTransfer;

    String targetPath = ZKFileUploadTest.uploadFileRootPath + File.separator + ZKFileUploadTest.upload;

    /**
     * 二进制流上传，不需要 MultipartResolver 适配器
     *
     * @Title: uploadStream
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 17, 2019 3:41:03 PM
     * @param hReq
     * @throws IOException
     * @return void
     */
    @RequestMapping(value = "uploadStream")
    public String uploadStream(HttpServletRequest hReq) throws IOException {
        InputStream is = null;
        try {
            String fileName = hReq.getHeader("filename");
            if (!ZKStringUtils.isEmpty(fileName)) {
                fileName = ZKHtmlUtils.urlDecode(fileName);
                // 防止中文乱码 "ISO-8859-1"; 因为 okhttp3 不支持请求头添加中文，统一使用 url 编码方式解决中文编码问题
//                fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
            }
            is = hReq.getInputStream();
            String fileAbsolutePath = zkFileTransfer.transferFile(is, targetPath, fileName, false, false, null, null);
            System.out.println("[^_^:20240329-1959-001] ZKWebmvcTestFileController.uploadStream: " + fileAbsolutePath);
            return fileAbsolutePath;
        } finally {
            if (is != null) {
                ZKStreamUtils.closeStream(is);
            }
        }
    }

    /**
     * multipart File 单个与多个同时上传，需要 MultipartResolver 适配器
     *
     * @Title: uploadMultipart
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 17, 2019 3:40:47 PM
     * @param mf1
     * @param mfs
     * @return
     * @throws FileUploadException
     * @throws IOException
     * @return List<String>
     */
    @RequestMapping(path = "uploadMultipart")
    public List<String> uploadMultipart(@RequestParam(value = "f1") MultipartFile mf1,
            @RequestParam(value = "fs", required = false) List<MultipartFile> mfs, HttpServletRequest hReq)
            throws FileUploadException, IOException {

        System.out.println("[^_^:20230526-1733-001] ZKWebmvcTestFileController ======================");
        System.out.println("[^_^:20230526-1733-001] getOriginalFilename: " + mf1.getOriginalFilename());
        System.out.println("[^_^:20230526-1733-001] getName: " + mf1.getName());
        System.out.println("[^_^:20230526-1733-001] getContentType: " + mf1.getContentType());
        System.out.println("[^_^:20230526-1733-001] getResource: " + mf1.getResource());

        List<String> fs = new ArrayList<String>();
        String fPath = this.zkFileTransfer.transferFile(mf1.getInputStream(), this.targetPath,
                mf1.getOriginalFilename(), false,
                false, null, null);
        fs.add(fPath);

//        for (String fileAbsolutePath : fileList) {
//            fs.add(fileAbsolutePath);
//        }

        System.out.println("[^_^:20230526-1733-001] ===============================================");
        if (mfs != null) {
            for (MultipartFile item : mfs) {
                System.out.println("[^_^:20230526-1733-001] getOriginalFilename: " + item.getOriginalFilename()
                        + "-----------------");
                System.out.println("[^_^:20230526-1733-001] getContentType: " + item.getContentType());
                System.out.println("[^_^:20230526-1733-001] getName: " + item.getName());
                System.out.println("[^_^:20230526-1733-001] getResource: " + item.getResource());
            }
            for (MultipartFile item : mfs) {
                fPath = this.zkFileTransfer.transferFile(item.getInputStream(), this.targetPath,
                        item.getOriginalFilename(),
                        false, false, null, null);
                fs.add(fPath);
            }
        }

//        for (String fileAbsolutePath : fileList) {
//            fs.add(fileAbsolutePath);
//        }
        return fs;
    }

    /**
     * 取文件
     *
     * @Title: getFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 1, 2024 9:08:20 AM
     * @param fName
     * @param hRes
     * @throws FileUploadException
     * @throws IOException
     * @return void
     */
    @RequestMapping(path = "getFile")
    public void getFile(@RequestParam(value = "fName") String fName, HttpServletResponse hRes)
            throws FileUploadException, IOException {
        
        String fileId = this.targetPath + File.separator + fName;

        // 下载文件，浏览器自动转为下载时，需要添加下面这一句。
//        hRes.setHeader("Content-Disposition", "attachment;fileName=" + fName);
        OutputStream os = null;
        try {
            os = hRes.getOutputStream();
            this.zkFileTransfer.getFile(fileId, os);
        } finally {
            ZKStreamUtils.closeStream(os);
        }
    }

//    /**
//     * 自行解析 Multipart 请求；不能配置 MultipartResolver 适配器
//     *
//     * @Title: uploadMultipartNoResolver
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Dec 17, 2019 3:41:15 PM
//     * @param req
//     * @return
//     * @return List<String>
//     * @throws Exception
//     */
//    @RequestMapping(path = "uploadMultipartNoResolver")
//    public List<String> uploadMultipartNoResolver(HttpServletRequest hReq) throws Exception {
//
//        StandardServletMultipartResolver ssm = new StandardServletMultipartResolver();
//        List<String> fs = new ArrayList<String>();
//        // 判断提交上来的数据是否是上传表单的数据
//        if (ssm.isMultipart(hReq)) {
//            InputStream is = null;
//
//            // 1、创建一个DiskFileItemFactory工厂
//            DiskFileItemFactory factory = new DiskFileItemFactory();
//            // 2、创建一个文件上传解析器
//            ServletFileUpload upload = new ServletFileUpload(factory);
//            // 解决上传文件名的中文乱码
//            upload.setHeaderEncoding("UTF-8");
//
//            // 3、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
//            List<FileItem> fileitems = upload.parseRequest(hReq);
//
//            for (FileItem fileItem : fileitems) {
//                is = null;
//                try {
//                    String fileName = fileItem.getName();
//                    if (ZKStringUtils.isEmpty(fileName)) {
//                        fileName = UUID.randomUUID().toString();
//                    }
//                    is = fileItem.getInputStream();
//                    File file = ZKFileUtils.createFile(targetPath, fileName, true);
//                    ZKFileUtils.writeFile(is, file, false);
//                    fs.add(file.getAbsolutePath());
//                } finally {
//                    if (is != null) {
//                        ZKStreamUtils.closeStream(is);
//                    }
//                }
//            }
//        }
//        return fs;
//    }

}
