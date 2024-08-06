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
* @Title: ZKJaxbMapperUtils.java 
* @author Vinson 
* @Package com.zk.core.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 5:34:35 PM 
* @version V1.0 
*/
package com.zk.core.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 使用Jaxb2.0实现XML<->Java Object的Mapper.
 * 
 * 在创建时需要设定所有需要序列化的Root对象的Class. 特别支持Root对象是Collection的情形.
 * 
 * @ClassName: ZKJaxbMapperUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKJaxbMapperUtils {

    private static ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>();

    /**
     * Java Object->Xml without encoding.
     * 
     * @throws Exception
     */
    public static String toXml(Object root) throws Exception {
        Class<?> clazz = ZKReflectionUtils.getUserClass(root);
        return toXml(root, clazz, null);
    }

    /**
     * Java Object->Xml with encoding.
     * 
     * @throws Exception
     */
    public static String toXml(Object root, String encoding) throws Exception {
        Class<?> clazz = ZKReflectionUtils.getUserClass(root);
        return toXml(root, clazz, encoding);
    }

    /**
     * Java Object->Xml with encoding.
     * 
     * @throws Exception
     */
    public static String toXml(Object root, Class<?> clazz, String encoding) throws Exception {
        StringWriter writer = new StringWriter();
        createMarshaller(clazz, encoding).marshal(root, writer);
        return writer.toString();
    }

    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
     * 
     * @throws Exception
     */
    public static String toXml(Collection<?> root, String rootName, Class<?> clazz) throws Exception {
        return toXml(root, rootName, clazz, null);
    }

    /**
     * Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形.
     * 
     * @throws Exception
     */
    public static String toXml(Collection<?> root, String rootName, Class<?> clazz, String encoding) throws Exception {
        CollectionWrapper wrapper = new CollectionWrapper();
        wrapper.collection = root;

        JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
                CollectionWrapper.class, wrapper);

        StringWriter writer = new StringWriter();
        createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

        return writer.toString();
    }

    /**
     * Xml->Java Object.
     * 
     * @throws JAXBException
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXml(String xml, Class<T> clazz) throws JAXBException, Exception {
        StringReader reader = new StringReader(xml);
        return (T) createUnmarshaller(clazz).unmarshal(reader);
    }

    /**
     * 创建Marshaller并设定encoding(可为null). 线程不安全，需要每次创建或pooling。
     * 
     * @throws Exception
     */
    public static Marshaller createMarshaller(Class<?> clazz, String encoding) throws Exception {
        JAXBContext jaxbContext = getJaxbContext(clazz);

        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        if (StringUtils.isNotBlank(encoding)) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        }

        return marshaller;
    }

    /**
     * 创建UnMarshaller. 线程不安全，需要每次创建或pooling。
     * 
     * @throws Exception
     */
    public static Unmarshaller createUnmarshaller(Class<?> clazz) throws Exception {
        JAXBContext jaxbContext = getJaxbContext(clazz);
        return jaxbContext.createUnmarshaller();
    }

    protected static JAXBContext getJaxbContext(Class<?> clazz) throws Exception {
        Assert.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            }
            catch(JAXBException ex) {
                throw new Exception("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(),
                        ex);
            }
        }
        return jaxbContext;
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    public static class CollectionWrapper {

        @XmlAnyElement
        protected Collection<?> collection;
    }

}
