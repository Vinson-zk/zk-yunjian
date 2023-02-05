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
 * @Title: ZKCoreFileController.java 
 * @author Vinson 
 * @Package com.zk.core.helper.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:30:45 PM 
 * @version V1.0   
*/
package com.zk.core.helper.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.controller.ZKCoreFileControllerTest;

/** 
* @ClassName: ZKCoreFileController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin:zkTest}/${zk.path.core:cTest}/file")
public class ZKCoreFileController {

    @Autowired
    ZKFileTransfer zkFileTransfer;

    String targetPath = ZKCoreFileControllerTest.filePath + File.separator + ZKCoreFileControllerTest.filePathTarget;

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
            String fileName = hReq.getHeader("fileName");
            if (!ZKStringUtils.isEmpty(fileName)) {
                // 防止中文乱码 "ISO-8859-1"
                fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
            }
            is = hReq.getInputStream();
            File file = zkFileTransfer.transferFile(is, targetPath, fileName, true, false);
            return file.getAbsolutePath();
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
            @RequestParam(value = "fs") Collection<MultipartFile> mfs, HttpServletRequest hReq)
            throws FileUploadException, IOException {

        List<String> fs = new ArrayList<String>();
        List<File> fileList = this.zkFileTransfer.transferFile(Arrays.asList(mf1), this.targetPath, false, true);
        for (File f : fileList) {
            fs.add(f.getAbsolutePath());
        }
        fileList = this.zkFileTransfer.transferFile(mfs, this.targetPath, false, true);
        for (File f : fileList) {
            fs.add(f.getAbsolutePath());
        }
        return fs;
    }

    /**
     * 自行解析 Multipart 请求；不能配置 MultipartResolver 适配器
     *
     * @Title: uploadMultipartNoResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 17, 2019 3:41:15 PM
     * @param req
     * @return
     * @throws FileUploadException
     * @throws IOException
     * @return List<String>
     */
    @RequestMapping(path = "uploadMultipartNoResolver")
    public List<String> uploadMultipartNoResolver(HttpServletRequest hReq) throws FileUploadException, IOException {

        List<String> fs = new ArrayList<String>();
        // 判断提交上来的数据是否是上传表单的数据
        if (ServletFileUpload.isMultipartContent(hReq)) {
            InputStream is = null;

            // 1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");

            // 3、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> fileitems = upload.parseRequest(hReq);

            for (FileItem fileItem : fileitems) {
                is = null;
                try {
                    String fileName = fileItem.getName();
                    if (ZKStringUtils.isEmpty(fileName)) {
                        fileName = UUID.randomUUID().toString();
                    }
                    is = fileItem.getInputStream();
                    File file = ZKFileUtils.createFile(targetPath, fileName, true);
                    ZKFileUtils.writeFile(is, file, false);
                    fs.add(file.getAbsolutePath());
                } finally {
                    if (is != null) {
                        ZKStreamUtils.closeStream(is);
                    }
                }
            }
        }
        return fs;
    }

}
