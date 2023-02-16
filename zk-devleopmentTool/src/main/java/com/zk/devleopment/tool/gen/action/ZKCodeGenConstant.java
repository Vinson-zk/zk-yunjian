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
* @Title: ZKCodeGenConstant.java 
* @author Vinson 
* @Package com.zk.code.generate.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 25, 2021 12:02:25 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action;
/** 
* @ClassName: ZKCodeGenConstant 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCodeGenConstant {

    /**
     * 配置文件路径
     */
    public static String[] mappers = new String[] { "gen/config/myBatis/mappers/tableInfoMapper.xml" };

    /**
     * 表: 信息 xml
     */
    public static interface KeyTable {
        /**
         * 表：表名
         */
        public final static String tableName = "tableName";

        /**
         * 表：表描述
         */
        public final static String tableComments = "tableComments";
    }

    /**
     * 表: 字段信息 xml 文件中的字段信息节点名称，有多个
     */
    public static interface KeyCol {
//        {"columnComments":"","columnSort":10,"columnJdbcType":"varchar(128)","columnName":"variable","columnIsNull":"0"}

        /**
         * 表：字段名
         */
        public final static String columnName = "columnName";

        /**
         * 表：字段类型
         */
        public final static String columnJdbcType = "columnJdbcType";

        /**
         * 表：描述
         */
        public final static String columnComments = "columnComments";

        /**
         * 表：字段是否可为空；0-不为空；1-可为空；默认 0
         */
        public final static String columnIsNull = "columnIsNull";

        /**
         * 表：字段排序
         */
        public final static String columnSort = "columnSort";

    }

}
