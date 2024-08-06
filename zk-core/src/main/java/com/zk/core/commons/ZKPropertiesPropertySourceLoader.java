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
 * @Title: ZKPropertiesPropertySourceLoader.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:28:06 PM 
 * @version V1.0   
*/
package com.zk.core.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

/**
 * 重写 spring boot 配置文件加载，解决中文乱码问题；
 * 
 * 需要在 resources下新建 META-INF 文件夹,新建一个 spring.factories 文件； 在 spring.factories
 * 文件中添加
 * org.springframework.boot.env.PropertySourceLoader=com.zk.core.commons.ZKPropertiesPropertySourceLoader
 * 
 * @ClassName: ZKPropertiesPropertySourceLoader
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKPropertiesPropertySourceLoader extends PropertiesPropertySourceLoader {

//      public ZKPropertiesPropertySourceLoader() {
//          
//      }

      private static final String XML_FILE_EXTENSION = ".xml";

      @Override
      public String[] getFileExtensions() {
          return new String[] { "properties", "xml" };
      }

      @Override
      public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
          Map<String, ?> properties = loadProperties(resource);
          if (properties.isEmpty()) {
              return Collections.emptyList();
          }
          return Collections.singletonList(new OriginTrackedMapPropertySource(name, properties));
      }

      @SuppressWarnings({ "unchecked", "rawtypes" })
      private Map<String, ?> loadProperties(Resource resource) throws IOException {
          String filename = resource.getFilename();
          if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
              Properties props = new Properties();
              fillProperties(props, resource);
              return (Map) props;
          }
        return new ZKOriginTrackedPropertiesLoader(resource).load();
      }

      /**
       * Fill the given properties from the given resource (in ISO-8859-1
       * encoding).
       * 
       * @param props
       *            the Properties instance to fill
       * @param resource
       *            the resource to load from
       * @throws IOException
       *             if loading failed
       */
      public static void fillProperties(Properties props, Resource resource) throws IOException {
          InputStream is = resource.getInputStream();
          try {
              String filename = resource.getFilename();
              if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
                  props.loadFromXML(is);
              }
              else {
                  props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
              }
          } finally {
              is.close();
          }
      }
}
