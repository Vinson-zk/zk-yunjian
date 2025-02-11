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
 * @Title: ZKXmlUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:20:37 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import jakarta.xml.bind.JAXBException;

/** 
* @ClassName: ZKXmlUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKXmlUtils {

    protected static Logger logger = LogManager.getLogger(ZKXmlUtils.class);

//    /**
//     *
//     * @Title: getRootElemnt
//     * @Description: TODO(simple description this method what to do.)
//     * @author zk
//     * @date 2017年8月21日 下午6:59:38
//     * @param file
//     *            从 classpath 路径下的相对路径
//     * @return
//     * @throws Exception
//     * @return Element
//     */
//    public static Element getRootElemnt(String file) throws Exception {
//        Document document;
//        try {
//            SAXReader reader = new SAXReader();
//            URL url = Thread.currentThread().getContextClassLoader().getResource(file);
//            logger.info("[^_^ 20171124-2302-001] file path->{}", url.getFile());
//            document = reader.read(url);
//            return document.getRootElement();
//        }
//        catch(Exception e) {
//            throw e;
//        }
//    }
    
    /**
     * 根据 xml 字符串内容生成 Document
     *
     * @Title: getDocument
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年9月6日 下午12:48:59
     * @param xmlStr
     *            xml 字符串
     * @return
     * @throws Exception
     * @return Document
     */
    public static Document getDocument(String xmlStr) {
        Document document;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new ByteArrayInputStream(xmlStr.getBytes()));
            return document;
        }
        catch(Exception e) {
            logger.error("[^_^ 20180906-1245-003] 根据 xml 字符串内容 [{}] 生成 Document 失败", xmlStr);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param values
     * @param rootKey
     * @return
     */
    public static Document getDocument(Object values, String rootKey) {
        Document rDom = null;
        if (values != null) {
            rDom = DocumentHelper.createDocument();
            Element root = rDom.addElement(rootKey);
            objToXml(values, root);
        }

        return rDom;
    }

    /**
     * map 转换为 xml
     * 
     * @param map
     * @param e
     */
    private static void mapToXml(Map<String, Object> map, Element e) {
        for (String key : map.keySet()) {
            Element keyE = e.addElement(key);
            objToXml(map.get(key), keyE);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void objToXml(Object obj, Element e) {
        if (obj instanceof String) {
            e.add(DocumentHelper.createCDATA(obj.toString()));
        }
        else {
            if (obj instanceof Map) {
                mapToXml((Map<String, Object>) obj, e);
            }
            else {
                if (obj instanceof List) {
//                  Element eParent = e.getParent();
                    for (Object objv : (List) obj) {
                        objToXml(objv, e);
                    }
                }
                else {
                    e.setText(obj.toString());
                }
            }
        }
    }

    /**
     * 根据模板文件名，取模板内容对象
     *
     * @Title: xmlToBean
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 24, 2021 5:31:23 PM
     * @param <T>
     * @param filePath
     * @param fileName
     * @param classz
     * @return
     * @throws Exception
     * @return T
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String filePath, String fileName, Class<?> classz) throws JAXBException, Exception {

        String pathName = filePath + File.separator + fileName;
        Resource resource = new ClassPathResource(pathName);
        InputStream is = resource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            sb.append(line).append("\r\n");
        }
        if (is != null) {
            is.close();
        }
        if (br != null) {
            br.close();
        }
        return (T) ZKJaxbMapperUtils.fromXml(sb.toString(), classz);

    }

}
