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
* @Title: ZKDatabaseConnect.java 
* @author Vinson 
* @Package com.zk.code.generate.connect.api 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 17, 2020 9:05:40 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.connect.api;

import java.util.List;
import java.util.Map;

/** 
* @ClassName: ZKDatabaseConnect 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKDatabaseConnect {

    /**
     * 取表在数据中字段的信息明细 List
     *
     * @Title: getColumnSourceList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 17, 2020 9:06:19 PM
     * @param dbType
     *            数据库类型，MyBatis 的实现中支持【oracle、mssql、mysql】
     * @param tableName
     *            需要查询的表名
     * @return
     * @return List<Map<String,Object>>
     */
    public List<Map<String, Object>> getColumnSourceList(String dbType, String tableName);

    /**
     * 取表中的主键字段名
     *
     * @Title: getPkSourceList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 17, 2020 9:06:41 PM
     * @param dbType
     *            数据库类型，MyBatis 的实现中支持【oracle、mssql、mysql】
     * @param tableName
     *            需要查询的表名
     * @return
     * @return List<String>
     */
    public List<String> getPkSourceList(String dbType, String tableName);
}
