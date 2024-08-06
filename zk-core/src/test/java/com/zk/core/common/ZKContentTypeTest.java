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
* @Title: ZKContentTypeTest.java 
* @author Vinson 
* @Package com.zk.core.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 29, 2021 4:24:00 PM 
* @version V1.0 
*/
package com.zk.core.common;

import java.nio.charset.Charset;

import org.junit.Test;

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKCoreConstants;

/** 
* @ClassName: ZKContentTypeTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKContentTypeTest {

    @Test
    public void testZKContentType() {
        ZKContentType ct = null;

        String ctStr = null;
        ct = ZKContentType.parseByFileExt(ctStr);
        System.out.println("[^_^:20211129-1625-001-1] ByFileExt.null: " + ct);
        ct = ZKContentType.parseByType(ctStr);
        System.out.println("[^_^:20211129-1625-001-2] ByType.null: " + ct);

        ctStr = "doc";
        ct = ZKContentType.parseByFileExt(ctStr);
        System.out.println("[^_^:20211129-1625-002-1] ByFileExt.doc: " + ct);
        System.out.println("[^_^:20211129-1625-002-1] ByFileExt.doc.name: " + ct.name());

        ctStr = "application/x-www-form-urlencoded";
        ct = ZKContentType.parseByType(ctStr);
        System.out.println("[^_^:20211129-1625-003-1] ByFileExt.application/x-www-form-urlencoded: " + ct);
        ct = ZKContentType.parseByType(ctStr, null);
        System.out.println("[^_^:20211129-1625-003-2] ByFileExt.application/x-www-form-urlencoded: " + ct);
        ct = ZKContentType.parseByType(ctStr, Charset.forName("UTF-8"));
        System.out.println("[^_^:20211129-1625-003-3] ByFileExt.application/x-www-form-urlencoded: " + ct);
        ct = ZKContentType.parseByType(ctStr, ZKCoreConstants.Consts.UTF_8);
        System.out.println("[^_^:20211129-1625-003-4] ByFileExt.application/x-www-form-urlencoded: " + ct.name());

    }

}
