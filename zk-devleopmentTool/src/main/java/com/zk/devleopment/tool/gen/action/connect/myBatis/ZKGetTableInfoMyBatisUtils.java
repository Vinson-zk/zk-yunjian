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
* @Title: ZKGetTableInfoMyBatisUtils.java 
* @author Vinson 
* @Package com.zk.devleopment.tool.gen.action.connect.myBatis 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 6, 2023 1:23:31 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.connect.myBatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.exception.ZKBusinessException;
import com.zk.devleopment.tool.gen.action.ZKCodeGenConstant;
import com.zk.devleopment.tool.gen.entity.ZKModule;

/** 
* @ClassName: ZKGetTableInfoMyBatisUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKGetTableInfoMyBatisUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKGetTableInfoMyBatisUtils.class);

    public static List<String> getPkSourceList(ZKModule zkModule, String tableName) {
        ZKMyBatisConnect zkConnect = null;
        try {
            ZKMyBatisSessionFactory.createSessionFactory(ZKMyBatisSessionFactory.createDataSource(zkModule),
                    ZKCodeGenConstant.mappers);
            SqlSession session = ZKMyBatisSessionFactory.openSession();
//            Assert.notNull(session);
            zkConnect = new ZKMyBatisConnect(session);

            return zkConnect.getPkSourceList(zkModule.getDbType(), tableName);

        } finally {
            if (zkConnect != null) {
                zkConnect.close();
            }
            ZKMyBatisSessionFactory.clean();
        }
    }

    /**
     * 查询功能模块下的所有表
     *
     * @Title: getDbTableInfos
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 31, 2021 9:11:38 AM
     * @param zkModule
     * @return
     * @return List<ZKTableInfo>
     */
    public static List<Map<String, Object>> getDbTableInfos(ZKModule zkModule, String tableName) {
        ZKMyBatisConnect zkConnect = null;
        try {
            ZKMyBatisSessionFactory.createSessionFactory(ZKMyBatisSessionFactory.createDataSource(zkModule),
                    ZKCodeGenConstant.mappers);
            SqlSession session = ZKMyBatisSessionFactory.openSession();
//            Assert.notNull(session);
            zkConnect = new ZKMyBatisConnect(session);

            return zkConnect.findTableList(zkModule.getDbType(), tableName);
        } finally {
            if (zkConnect != null) {
                zkConnect.close();
            }
            ZKMyBatisSessionFactory.clean();
        }
    }

    public static Map<String, Object> getDbTableInfo(ZKModule zkModule, String tableName) {
        ZKMyBatisConnect zkConnect = null;
        try {
            ZKMyBatisSessionFactory.createSessionFactory(ZKMyBatisSessionFactory.createDataSource(zkModule),
                    ZKCodeGenConstant.mappers);
            SqlSession session = ZKMyBatisSessionFactory.openSession();
//            Assert.notNull(session);
            zkConnect = new ZKMyBatisConnect(session);

            List<Map<String, Object>> tableInfoList = zkConnect.findTableList(zkModule.getDbType(), tableName);
            if (tableInfoList != null && !tableInfoList.isEmpty()) {
                if (tableInfoList.size() != 1) {
                    log.error("[^_^:20210401-0704-003] 表:{}, 信息不唯一；", tableName);
                    throw ZKBusinessException.as("zk.codeGen.000003", null, tableName);
                }
                return tableInfoList.get(0);
            }
            return null;

        } finally {
            if (zkConnect != null) {
                zkConnect.close();
            }
            ZKMyBatisSessionFactory.clean();
        }
    }

    /**
     * 查询 表字段信息
     *
     * @Title: getDbColInfos
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 1, 2021 12:09:44 AM
     * @param zkModule
     * @param tableName
     * @return
     * @return List<ZKColInfo>
     */
    public static List<Map<String, Object>> getDbColInfos(ZKModule zkModule, String tableName) {
        ZKMyBatisConnect zkConnect = null;
        try {
            ZKMyBatisSessionFactory.createSessionFactory(ZKMyBatisSessionFactory.createDataSource(zkModule),
                    ZKCodeGenConstant.mappers);
            SqlSession session = ZKMyBatisSessionFactory.openSession();
//            Assert.notNull(session);
            zkConnect = new ZKMyBatisConnect(session);

            return zkConnect.getColumnSourceList(zkModule.getDbType(), tableName);

        } finally {
            if (zkConnect != null) {
                zkConnect.close();
            }
            ZKMyBatisSessionFactory.clean();
        }
    }

}
