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
 * @Title: ZKDBConstants.java 
 * @author Vinson 
 * @Package com.zk.db.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:28:20 AM 
 * @version V1.0   
*/
package com.zk.db.commons;

/**
* @ClassName: ZKDBConstants 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBConstants {

    /**
     * 默认数据库
     */
    public static final String defaultJdbcType = "mysql";

    /**
     * 参数名
     */
    public static interface PARAM_NAME {

        /**
         * 分页数据对象
         */
        public static final String Page = "page";
    }

    public static interface Mybatis_Param_Name {
        public static final String sqlHelper = "_zkSql";
    }

}
