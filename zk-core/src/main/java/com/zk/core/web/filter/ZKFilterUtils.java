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
* @Title: ZKFilterUtils.java 
* @author Vinson 
* @Package com.zk.core.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 14, 2023 7:19:17 PM 
* @version V1.0 
*/
package com.zk.core.web.filter;

/** 
* @ClassName: ZKFilterUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFilterUtils {

    public static interface ZKFilterLevel {

        static final int ZoneSpacing = 30000;

        // 异常处理拦截级别
        public static interface Exception {

            public static final int HIGHEST = Integer.MIN_VALUE + ZoneSpacing;

            public static final int LOWEST = HIGHEST + ZoneSpacing;

        }

        // 日志的拦截级别最高
        public static interface Log {

            public static final int HIGHEST = Exception.LOWEST + ZoneSpacing;

            public static final int LOWEST = HIGHEST + ZoneSpacing;

        }

        // 权限的拦截级别，仅次于日志
        public static interface Security {

            public static final int HIGHEST = Log.LOWEST + ZoneSpacing;

            public static final int LOWEST = HIGHEST + ZoneSpacing;

        }

        // 一般拦截器
        public static interface Normal {

            public static final int HIGHEST = Security.LOWEST + ZoneSpacing;

            public static final int LOWEST = HIGHEST + ZoneSpacing;

        }

    }

}
