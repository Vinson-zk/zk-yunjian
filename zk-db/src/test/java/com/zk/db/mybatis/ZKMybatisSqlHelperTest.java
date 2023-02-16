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
 * @Title: ZKMybatisSqlHelperTest.java 
 * @author Vinson 
 * @Package com.zk.db.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:20:30 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMybatisSqlHelperTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMybatisSqlHelperTest {

    @Test
    public void testFormatSql() {
        try {
            String str = "aa aa       aa \n \t \r          \n   \t   \r        ";
            String expectedStr = "aa aa aa";
            String resStr = ZKMybatisSqlHelper.formatSql(str);
            TestCase.assertEquals(expectedStr, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
