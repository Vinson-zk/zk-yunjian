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
 * @Title: ZKObjectUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:10:35 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKObjectUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKObjectUtilsTest {

    @Test
    public void testMapFillEntity() {
        @SuppressWarnings("unused")
        class Entity {
            private int intAttribute;

            private String strAttribute;

            private Date dateAttribute;

            private float floatAttribute;

            public int getIntAttribute() {
                return intAttribute;
            }

            public void setIntAttribute(int intAttribute) {
                this.intAttribute = intAttribute;
            }

            public String getStrAttribute() {
                return strAttribute;
            }

            public void setStrAttribute(String strAttribute) {
                this.strAttribute = strAttribute;
            }

            public Date getDateAttribute() {
                return dateAttribute;
            }

            public void setDateAttribute(Date dateAttribute) {
                this.dateAttribute = dateAttribute;
            }

            public float getFloatAttribute() {
                return floatAttribute;
            }

            public void setFloatAttribute(float floatAttribute) {
                this.floatAttribute = floatAttribute;
            }

        }
        Entity entity;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Date date = new Date();
            // private int intAttribute;
            // private String strAttribute;
            // private Date dateAttribute;
            // private float floatAttribute;
            map.put("intAttribute", "2");
            map.put("strAttribute", "strAttribute");
            map.put("dateAttribute", date);
            map.put("floatAttribute", "1.2");

            entity = new Entity();
            ZKObjectUtils.mapFillEntity(entity, map);
            TestCase.assertEquals(2, entity.getIntAttribute());
            TestCase.assertEquals("strAttribute", entity.getStrAttribute());
            TestCase.assertEquals(date, entity.getDateAttribute());

            map.put("intAttribute", 3);
            map.put("dateAttribute", date.getTime());
            map.put("floatAttribute", 1.3);
            entity = new Entity();
            ZKObjectUtils.mapFillEntity(entity, map);
            TestCase.assertEquals(3, entity.getIntAttribute());
            TestCase.assertEquals("strAttribute", entity.getStrAttribute());
            TestCase.assertEquals(date, entity.getDateAttribute());

            map.remove("intAttribute");
            map.remove("strAttribute");
            map.put("dateAttribute", "2015-12-12 12:12:12[yyyy-MM-dd HH:mm:ss]");
            entity = new Entity();
            ZKObjectUtils.mapFillEntity(entity, map);
            TestCase.assertEquals(0, entity.getIntAttribute());
            TestCase.assertEquals(null, entity.getStrAttribute());
            TestCase.assertEquals(ZKDateUtils.parseDate("2015-12-12 12:12:12", "yyyy-MM-dd HH:mm:ss"),
                    entity.getDateAttribute());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsSerialize() {
        try {
            Map<String, String> map = null;
            String str = "";
            byte[] sByte = null;

            map = new HashMap<>();
            map.put("key", "value");
            sByte = ZKObjectUtils.serialize(map);
            TestCase.assertTrue(ZKObjectUtils.isSerialize(sByte));

            str = new String(sByte);
            TestCase.assertFalse(ZKObjectUtils.isSerialize(str.getBytes()));

            sByte = ZKObjectUtils.serialize(str);
            TestCase.assertTrue(ZKObjectUtils.isSerialize(sByte));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeAndUnserialize() {
        try {
            Map<String, String> map = new HashMap<>();

            map.put("key", "value");
            byte[] sByte = ZKObjectUtils.serialize(map);

            map = (Map<String, String>) ZKObjectUtils.unserialize(sByte);

            TestCase.assertEquals("value", map.get("key"));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
