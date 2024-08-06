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
* @Title: ZKSecApiCodeAnnotationHandler.java 
* @author Vinson 
* @Package com.zk.security.authz.hander 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 1:44:25 PM 
* @version V1.0 
*/
package com.zk.security.authz.hander;

import java.lang.annotation.Annotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.utils.ZKStringUtils;
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.authz.ZKSecAbstractAuthorizingAnnotationHandler;
import com.zk.security.common.ZKSecAnnotationResolver;

/** 
* @ClassName: ZKSecApiCodeAnnotationHandler 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecApiCodeAnnotationHandler extends ZKSecAbstractAuthorizingAnnotationHandler {

    protected Logger logger = LogManager.getLogger(getClass());

    public ZKSecApiCodeAnnotationHandler(ZKSecAnnotationResolver resolver) {
        super(ZKSecApiCode.class, resolver);
    }

    @Override
    public void doAssertAuthorized(Annotation annotation) {
        ZKSecApiCode apiCodeAnnotation = (ZKSecApiCode) annotation;
        String apiCode = apiCodeAnnotation.value();
        if (ZKStringUtils.isEmpty(apiCode)) {
            return;
        }
        // 鉴定是否有接口 API 代码权限
        if (getZKSecSubject().checkApiCode(apiCode)) {
            return;
        }
        else {
//            logger.error("您没API接口调用权限; [{}]", apiCode);
            throw com.zk.core.exception.ZKSecAuthorizationException.as("zk.sec.000021", apiCode);
        }
    }


}
