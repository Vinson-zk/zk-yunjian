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
* @Title: ZKGetTableInfoUtils.java 
* @author Vinson 
* @Package com.zk.code.generate.tableInfo 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 25, 2021 1:48:04 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.tableInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.devleopment.tool.gen.action.ZKCodeGenConstant;
import com.zk.devleopment.tool.gen.action.connect.myBatis.ZKGetTableInfoMyBatisUtils;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/** 
* @ClassName: ZKGetTableInfoUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKGetTableInfoUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKGetTableInfoUtils.class);

    /**
     * 做一个表信息对象；注意：这里未转换为 java 属性信息
     *
     * @Title: makeTableInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 31, 2021 9:10:42 AM
     * @param dbTableInfo
     *            数据库表信息
     * @param pkList
     *            主键名列表
     * @return
     * @return ZKTableInfo
     */
    public static ZKTableInfo makeTableInfo(Map<String, Object> dbTableInfo, List<String> pkList) {
        ZKTableInfo tableInfo = new ZKTableInfo();
        makeTableInfo(dbTableInfo, pkList, tableInfo);
        return tableInfo;
    }

    public static void makeTableInfo(Map<String, Object> dbTableInfo, List<String> pkList, ZKTableInfo outTableInfo) {
        outTableInfo.setTableName((String) dbTableInfo.get(ZKCodeGenConstant.KeyTable.tableName));
        outTableInfo.setTableComments((String) dbTableInfo.get(ZKCodeGenConstant.KeyTable.tableComments));
        if (pkList != null && !pkList.isEmpty()) {
            outTableInfo.setPkColName(pkList.get(0));
        }
    }

    /**
     * 做一个表字段信息对象；注意：这里未转换为 java 属性信息
     *
     * @Title: makeColInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 2:25:34 PM
     * @param dbColInfo
     *            数据库字段信息
     * @param pkList
     *            主键名列表
     * @return
     * @return ZKColInfo
     */
    public static ZKColInfo makeColInfo(Map<String, Object> dbColInfo, List<String> pkList) {
        ZKColInfo col = new ZKColInfo();
        return makeColInfo(dbColInfo, pkList, col);
    }

    public static ZKColInfo makeColInfo(Map<String, Object> dbColInfo, List<String> pkList, ZKColInfo outColInfo) {
        outColInfo.setColName((String) dbColInfo.get(ZKCodeGenConstant.KeyCol.columnName));
        outColInfo.setColJdbcType((String) dbColInfo.get(ZKCodeGenConstant.KeyCol.columnJdbcType));
        outColInfo.setColComments((String) dbColInfo.get(ZKCodeGenConstant.KeyCol.columnComments));
        outColInfo.setColIsNull(!"0".equals(dbColInfo.get(ZKCodeGenConstant.KeyCol.columnIsNull)));
        outColInfo.setColSort(dbColInfo.get(ZKCodeGenConstant.KeyCol.columnSort) == null ? 999999
            : ((BigInteger) dbColInfo.get(ZKCodeGenConstant.KeyCol.columnSort)).intValue());
        // 判断是不是主键
        for (String n : pkList) {
            if (n.equals(outColInfo.getColName())) {
                // 是主键
                outColInfo.setColIsPK(true);
                break;
            }
        }
        return outColInfo;
    }

    // ---------------------------------------------------------------------

    public static List<String> getPkSourceList(ZKModule zkModule, String tableName) {
        return ZKGetTableInfoMyBatisUtils.getPkSourceList(zkModule, tableName);
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
        return ZKGetTableInfoMyBatisUtils.getDbTableInfos(zkModule, tableName);
    }

    public static Map<String, Object> getDbTableInfo(ZKModule zkModule, String tableName) {
        return ZKGetTableInfoMyBatisUtils.getDbTableInfo(zkModule, tableName);
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
        return ZKGetTableInfoMyBatisUtils.getDbColInfos(zkModule, tableName);
    }

}
