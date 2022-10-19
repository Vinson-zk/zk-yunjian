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
 * @Title: ZKBaseHelperEntityTest.java 
 * @author Vinson 
 * @Package com.zk.base.entity 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 2:57:15 PM 
 * @version V1.0   
*/
package com.zk.base.entity;

import java.util.Date;

import org.junit.Test;

import com.zk.base.helper.entity.ZKBaseHelperEntityLong;
import com.zk.base.helper.entity.ZKBaseHelperEntityString;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKBaseHelperEntityTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBaseHelperEntityTest {

    @Test
    public void test() {
        try {
            ZKBaseEntity<?, ?> helperEntity = new ZKBaseHelperEntityString();
            ZKBaseEntity<?, ?> helperEntityLong = new ZKBaseHelperEntityLong();

            System.out.println("[^_^:20190808-1543-001] pkID class name: " + helperEntity.getPkIDClass().getName());
            System.out.println("[^_^:20190808-1543-002] pkID class name: " + helperEntityLong.getPkIDClass().getName());

            TestCase.assertNull(helperEntity.getPkId());
            TestCase.assertNull(helperEntityLong.getPkId());

            TestCase.assertTrue(helperEntity.getPkIDClass() == String.class);
            TestCase.assertTrue(helperEntityLong.getPkIDClass() == Long.class);

            TestCase.assertFalse(helperEntity.getPkIDClass() == Long.class);
            TestCase.assertFalse(helperEntityLong.getPkIDClass() == String.class);

            // 期望值
            String expected = "";
            // 实际值
            String actualValue = "";

            expected = "{\"delFlag\":0,\"version\":0,\"isNewRecord\":true}";
            actualValue = ZKJsonUtils.writeObjectJson(helperEntity);
            System.out.println("[^_^:20190808-1543-003] string: " + actualValue);
            TestCase.assertEquals(expected, actualValue);

            helperEntity.setRemarks("remarks");
            ZKJson zkJson = new ZKJson();
            zkJson.put("zkJson1", "zkJson");
            zkJson.put("zkJson2", 1);
            helperEntity.setSpareJson(zkJson);
            expected = "{\"remarks\":\"remarks\",\"spareJson\":{\"zkJson2\":1,\"zkJson1\":\"zkJson\"},\"delFlag\":0,\"version\":0,\"isNewRecord\":true}";
            actualValue = ZKJsonUtils.writeObjectJson(helperEntity);
            System.out.println("[^_^:20190808-1543-004] string: " + actualValue);
            TestCase.assertEquals(expected, actualValue);

            helperEntity.setRemarks(null);
            helperEntity.setSpareJson(null);
            expected = "{\"delFlag\":0,\"version\":0,\"isNewRecord\":true}";
            actualValue = ZKJsonUtils.writeObjectJson(helperEntity);
            System.out.println("[^_^:20190808-1543-005] string: " + actualValue);
            TestCase.assertEquals(expected, actualValue);

            helperEntity.setRemarks("");
            helperEntity.setSpareJson(new ZKJson());
            expected = "{\"delFlag\":0,\"version\":0,\"isNewRecord\":true}";
            actualValue = ZKJsonUtils.writeObjectJson(helperEntity);
            System.out.println("[^_^:20190808-1543-006] string: " + actualValue);
            TestCase.assertEquals(expected, actualValue);

            helperEntity.setUpdateDate(new Date());
            expected = "{\"updateDate\":\""
                    + ZKDateUtils.formatDate(helperEntity.getUpdateDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss)
                    + "\",\"delFlag\":0,\"version\":0,\"isNewRecord\":true}";
            actualValue = ZKJsonUtils.writeObjectJson(helperEntity);
            System.out.println("[^_^:20190808-1543-007] string: " + actualValue);
            TestCase.assertEquals(expected, actualValue);

            System.out.println("[^_^:20190808-1543-008-1] string: " + helperEntity.toString());

            helperEntity.preInsert();
            helperEntity.setRemarks("remarks");
            System.out.println("[^_^:20190808-1543-008-2] string: " + ZKJsonUtils.writeObjectJson(helperEntity));

            System.out.println("[^_^:20190808-1543-008-3] string: " + helperEntity.toString());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetPkIDClass() {
        try {
            ZKBaseEntity<?, ?> helperEntityLong = new ZKBaseHelperEntityLong();

            System.out.println("[^_^:20220504-1358-001] pkID class name: " + helperEntityLong.getPkIDClass().getName());
            TestCase.assertNull(helperEntityLong.getPkId());
            TestCase.assertTrue(helperEntityLong.getPkIDClass() == Long.class);
            TestCase.assertFalse(helperEntityLong.getPkIDClass() == String.class);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
