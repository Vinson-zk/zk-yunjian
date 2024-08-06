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
* @Title: ZKWebConstants.java 
* @author Vinson 
* @Package com.zk.core.web 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 3:43:02 PM 
* @version V1.0 
*/
package com.zk.core.web;

/** 
* @ClassName: ZKWebConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebConstants {

    public static interface HeaderKey {

        public static final String contentType = "Content-Type";

        public static final String contentLength = "Content-Length";

        public static final String cacheControl = "Cache-Control";

        public static final String allowOrigin = "Access-Control-Allow-Origin";

        public static final String allowMethods = "Access-Control-Allow-Methods";

        public static final String maxAge = "Access-Control-Max-Age";

        public static final String allowHeaders = "Access-Control-Allow-Headers";

    }

    public static interface HeaderValue {

        public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

        /***
         * header中的Access-Control-Allow-Origin,为空时禁止跨域
         */
        public static String allowOrigin = "*";

        /***
         * header 中的 Access-Control-Allow-Methods
         */
        public static String allowMethods = "POST,GET";

        /***
         * header 中的 Access-Control-Max-Age
         */
        public static String maxAge = "3600";

        /***
         * header 中的 Access-Control-Allow-Headers
         */
        public static String allowHeaders = "__SID,locale,Lang,X-Requested-With";

    }

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
