/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFeignSpringFormEncoder.java 
* @author Vinson 
* @Package com.zk.framework.common.feign.support
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 9, 2022 1:28:46 AM 
* @version V1.0 
*/
package com.zk.framework.common.feign.support;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;

/**
 * @ClassName: ZKFeignSpringFormEncoder
 * @Description: SpringFormEncoder 不支持 MultipartFile[] 多文件上传，下面配置让其支持上传 数组 和 List
 * @author Vinson
 * @version 1.0
 */
public class ZKFeignSpringFormEncoder extends SpringFormEncoder {

    protected Logger log = LogManager.getLogger(this.getClass());

    public ZKFeignSpringFormEncoder() {
        this(new Default());
    }

    public ZKFeignSpringFormEncoder(Encoder delegate) {
        super(delegate);
        MultipartFormContentProcessor processor = (MultipartFormContentProcessor) this
                .getContentProcessor(ContentType.MULTIPART);
        processor.addWriter(new SpringSingleMultipartFileWriter());
        processor.addWriter(new SpringManyMultipartFilesWriter());
    }

//    public static void main(String[] args) {
//        Type bodyType = null;
//        List<MultipartFile> l = null;
//        // [^_^:20220609-0144-001] bodyType: false-java.util.List<org.springframework.web.multipart.MultipartFile>
//        l = new ArrayList<MultipartFile>();
//        System.out.println("===" + l.getClass());
//        bodyType = sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl.make(List.class,
//                new Class[] { MultipartFile.class }, null);
//        System.out.println("===" + bodyType.getTypeName());
//        System.out.println("---------------");
//        System.out.println("===" + (l instanceof List));
//        System.out
//                .println("===" + (bodyType.getTypeName()
//                        .equals("java.util.List<org.springframework.web.multipart.MultipartFile>")));
//    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
//        log.info("[^_^:20220609-0144-001] bodyType-1: {}", bodyType.equals(List.class));
//        log.info("[^_^:20220609-0144-001] bodyType-2: {}", bodyType.getClass());
//        log.info("[^_^:20220609-0144-001] bodyType-3: {}", bodyType.getTypeName());
        if (bodyType.equals(MultipartFile.class)) {
            MultipartFile file = (MultipartFile) object;
            Map<String, Object> data = Collections.singletonMap(file.getName(), object);
            super.encode(data, MAP_STRING_WILDCARD, template);
            return;
        }
        else if (bodyType.equals(MultipartFile[].class)) {
            MultipartFile[] file = (MultipartFile[]) object;
            if (file != null) {
                Map<String, Object> data = Collections.singletonMap(file.length == 0 ? "" : file[0].getName(), object);
                super.encode(data, MAP_STRING_WILDCARD, template);
                return;
            }
        }
        else if (bodyType.getTypeName().equals("java.util.List<org.springframework.web.multipart.MultipartFile>")) {
            @SuppressWarnings("unchecked")
            List<MultipartFile> files = (List<MultipartFile>) object;
            if (files != null) {
                if (files.isEmpty()) {
                    Map<String, Object> data = Collections.singletonMap("", "");
                    super.encode(data, MAP_STRING_WILDCARD, template);
                }
                else {
                    Map<String, Object> data = Collections.singletonMap(files.isEmpty() ? "" : files.get(0).getName(),
                        object);
                    super.encode(data, MAP_STRING_WILDCARD, template);
                }
                return;
            }
        }
        else {
            super.encode(object, bodyType, template);
        }
    }

}
