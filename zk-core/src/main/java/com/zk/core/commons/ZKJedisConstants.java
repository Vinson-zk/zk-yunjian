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
* @Title: ZKJedisConstants.java 
* @author Vinson 
* @Package com.zk.core.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 11, 2021 10:11:33 AM 
* @version V1.0 
*/
package com.zk.core.commons;

/** 
* @ClassName: ZKJedisConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKJedisConstants {

    /**
     * 操作 KEY 的模式
     * 
     * @ClassName: OpsMode
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface OpsMode {

        /**
         * 仅在key不存时创建
         */
        public final static String NOT_EXIST = "NX";

        /**
         * 仅在key存在时修改值
         */
        public final static String EXIST = "XX";
    }

    public static interface TimeUnit {
        /**
         * 秒 seconds
         */
        public final static String SECONDS = "EX";

        /**
         * 毫秒 milliseconds
         */
        public final static String MILLISECONDS = "PX";

    }

}
