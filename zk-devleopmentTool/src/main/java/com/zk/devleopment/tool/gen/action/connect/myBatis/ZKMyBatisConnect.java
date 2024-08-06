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
* @Title: ZKMyBatisConnect.java 
* @author Vinson 
* @Package com.zk.devleopment.tool.gen.action.connect.myBatis 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 17, 2020 9:07:18 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.connect.myBatis;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.zk.core.exception.ZKBusinessException;
import com.zk.devleopment.tool.gen.action.connect.api.ZKDatabaseConnect;

import jakarta.persistence.PersistenceException;

/** 
* @ClassName: ZKMyBatisConnect 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
class ZKMyBatisConnect implements ZKDatabaseConnect {

    SqlSession sqlSession = null;

    public ZKMyBatisConnect(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * 取数据实例下的表列表；
     *
     * @Title: findTableList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 1, 2021 6:55:10 AM
     * @param dbType
     * @param tableName
     *            不传表名时，为查询所有；传入表名时为查询指定表名；
     * @return
     * @return List<Map<String,Object>>
     */
    public List<Map<String, Object>> findTableList(String dbType, String tableName) {
        try {
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("dbType", dbType);
            paramsMap.put("name", tableName);
            System.out.println("[^_^:20210331-0903-001] schema: " + sqlSession.getConnection().getSchema());
            List<Map<String, Object>> columnSourceList = sqlSession
                    .selectList("com.zk.devleopment.tool.gen.action.dao.TableInfoDao.findTableList", paramsMap);
            return columnSourceList;
        }catch (PersistenceException e){
            e.printStackTrace();
            // 模块数据库连接异常
            throw ZKBusinessException.as("zk.codeGen.000005", e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getColumnSourceList(String dbType, String tableName) {
        try {
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("dbType", dbType);
            paramsMap.put("name", tableName);
            System.out.println("[^_^:20200318-1447-001] schema: " + sqlSession.getConnection().getSchema());
            List<Map<String, Object>> columnSourceList = sqlSession
                    .selectList("com.zk.devleopment.tool.gen.action.dao.TableInfoDao.findTableColumnList", paramsMap);
            return columnSourceList;
        }catch (PersistenceException e){
            e.printStackTrace();
            // 模块数据库连接异常
            throw ZKBusinessException.as("zk.codeGen.000005", e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getPkSourceList(String dbType, String tableName) {
        try {
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("dbType", dbType);
            paramsMap.put("name", tableName);
            List<String> pkSourceList = sqlSession.selectList("com.zk.devleopment.tool.gen.action.dao.TableInfoDao.findTablePK",
                    paramsMap);
            return pkSourceList;
        }catch (PersistenceException e){
            e.printStackTrace();
            // 模块数据库连接异常
            throw ZKBusinessException.as("zk.codeGen.000005", e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    @SuppressWarnings("removal")
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
