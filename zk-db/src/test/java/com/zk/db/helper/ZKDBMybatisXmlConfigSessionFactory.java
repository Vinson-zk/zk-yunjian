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
 * @Title: ZKDBMybatisXmlConfigSessionFactory.java 
 * @author Vinson 
 * @Package com.zk.db.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:47:26 AM 
 * @version V1.0   
*/
package com.zk.db.helper;

import java.io.InputStream;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.db.ZKMybatisSessionFactory;

/** 
* @ClassName: ZKDBMybatisXmlConfigSessionFactory 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBMybatisXmlConfigSessionFactory extends ZKMybatisSessionFactory {

    protected static Logger logger = LogManager.getLogger(ZKDBMybatisXmlConfigSessionFactory.class);

    private static ZKDBMybatisXmlConfigSessionFactory myBatisSessionFactory = null;

    /**
     * 单例模式
     */
    private ZKDBMybatisXmlConfigSessionFactory() {

    }

    private static String xml_file_path;

    public static ZKDBMybatisXmlConfigSessionFactory creatBean(String configPath) {
        if (myBatisSessionFactory == null) {
            myBatisSessionFactory = new ZKDBMybatisXmlConfigSessionFactory();
            xml_file_path = configPath;
            myBatisSessionFactory.builder();
        }
        return myBatisSessionFactory;
    }

    /**
     * 重建立 SqlSessionFactory 对象
     */
    public void rebuildSqlSessionFactory() {
        try {
            // 设置读配置文件输入流
            InputStream inputStream = Resources.getResourceAsStream(xml_file_path);

            // 创建 myBatis SqlSessionFactory 对象
//            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            XMLConfigBuilder configBuilder = new XMLConfigBuilder(inputStream, null, null);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configBuilder.parse());

            logger.info("[^_^:20180309-1114-001] 创建 myBatis SqlSessionFactory 对象成功！ ");
        }
        catch(Exception e) {
            sqlSessionFactory = null;
            logger.error("[>_<:20180309-1114-002] 创建 myBatis SqlSessionFactory 对象失败！ ");
            e.printStackTrace();
        }
    }

    public SqlSessionFactory builder() {
        rebuildSqlSessionFactory();
        return sqlSessionFactory;
    }

}
