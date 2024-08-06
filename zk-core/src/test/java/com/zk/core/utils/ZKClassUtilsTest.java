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
 * @Title: ZKClassUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:06:08 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKClassUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/

interface IParent0<C1,C2> {}
interface IParent1<C1,C2> { }
interface IParent2<C1,C2> { }
interface IParent3<C1,C2> { }
interface IParent01<C1> extends IParent0<C1, Double> {}
interface IParent11<C1> extends IParent1<C1, Double> {}
interface IChild extends IParent01<String>, IParent1<ZKClassUtilsTest, Double> { }

class CParent<CC1, CC2> implements IParent11<CC1> { }
class CChild extends CParent<ZKClassUtilsTest, Double> implements IChild, IParent2<Float, BigDecimal>,
        IParent3<ZKClassUtilsTest, Double> { }

public class ZKClassUtilsTest {

    @Test
    public void testGetSuperclassByName() {

        Class<?> resultClass = null;
        /** java class 泛型 CParent */
        // CParent<CC1, CC2> : CParent<ZKClassUtilsTest, Double> implements IParent11<CC1>
        resultClass = ZKClassUtils.getSuperclassByName(CParent.class, CChild.class, "CC1");
        System.out.println("[^_^:20220504-1354-002]" + resultClass);
        TestCase.assertEquals(ZKClassUtilsTest.class, resultClass);
        resultClass = ZKClassUtils.getSuperclassByName(CParent.class, CChild.class, "CC2");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(Double.class, resultClass);
        // IParent11<C1> : IParent11<C1> extends IParent1<C1, Double>
        resultClass = ZKClassUtils.getSuperclassByName(IParent11.class, CChild.class, "C1");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(ZKClassUtilsTest.class, resultClass);
        // IParent1<C1,C2> : IParent1<C1, Double>
        resultClass = ZKClassUtils.getSuperclassByName(IParent1.class, CChild.class, "C1");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(ZKClassUtilsTest.class, resultClass);
        resultClass = ZKClassUtils.getSuperclassByName(IParent1.class, CChild.class, "C2");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(Double.class, resultClass);

        // IChild : IParent01<String>, IParent1<ZKClassUtilsTest, Double>
        // IParent01<C1> : IParent01<String> extends IParent0<C1, Double>
        resultClass = ZKClassUtils.getSuperclassByName(IParent01.class, CChild.class, "C1");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(String.class, resultClass);
        // IParent0<C1,C2> : IParent0<C1, Double>
        resultClass = ZKClassUtils.getSuperclassByName(IParent0.class, CChild.class, "C1");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(String.class, resultClass);
        resultClass = ZKClassUtils.getSuperclassByName(IParent0.class, CChild.class, "C2");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(Double.class, resultClass);
        // IParent1<C1,C2> : IParent1<ZKClassUtilsTest, Double>
        resultClass = ZKClassUtils.getSuperclassByName(IParent1.class, CChild.class, "C1");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(ZKClassUtilsTest.class, resultClass);
        resultClass = ZKClassUtils.getSuperclassByName(IParent1.class, CChild.class, "C2");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(Double.class, resultClass);

        // IParent2<C1,C2> : IParent2<Float, BigDecimal>,
        resultClass = ZKClassUtils.getSuperclassByName(IParent2.class, CChild.class, "C1");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(Float.class, resultClass);
        resultClass = ZKClassUtils.getSuperclassByName(IParent2.class, CChild.class, "C2");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(BigDecimal.class, resultClass);
        // IParent3<C1,C2> : IParent3<ZKClassUtilsTest, Double>
        resultClass = ZKClassUtils.getSuperclassByName(IParent3.class, CChild.class, "C1");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(ZKClassUtilsTest.class, resultClass);
        resultClass = ZKClassUtils.getSuperclassByName(IParent3.class, CChild.class, "C2");
        System.out.println("[^_^:20220504-1354-001]" + resultClass);
        TestCase.assertEquals(Double.class, resultClass);
    }

    @Test
    public void testGetAllField() {

        @SuppressWarnings("unused")
        class Parent {
            private String parentAttribute;
            public String getParentAttribute() {
                return parentAttribute;
            }
            public void setParentAttribute(String parentAttribute) {
                this.parentAttribute = parentAttribute;
            }
        }

        @SuppressWarnings("unused")
        class Child extends Parent {
            private String childAttribute;
            public String getChildAttribute() {
                return childAttribute;
            }
            public void setChildAttribute(String childAttribute) {
                this.childAttribute = childAttribute;
            }
        }

        List<Field> result = ZKClassUtils.getAllField(Child.class);
        String fieldNameStr = "";
        for (Field f : result) {
            fieldNameStr += f.getName();
        }
        System.out.println(fieldNameStr);
        TestCase.assertTrue(fieldNameStr.matches(".*childAttribute.*"));
        TestCase.assertTrue(fieldNameStr.matches(".*parentAttribute.*"));
    }

    @Test
    public void testGetValueByClass() {
        try {
            Object result = null;

            result = ZKClassUtils.getValueByClass(Date.class, "2015-12-12 12:12:12[yyyy-MM-dd HH:mm:ss]");
            TestCase.assertEquals(Date.class, result.getClass());
            TestCase.assertEquals(ZKDateUtils.parseDate("2015-12-12 12:12:12", "yyyy-MM-dd HH:mm:ss"), result);

            Date date = new Date();
            result = ZKClassUtils.getValueByClass(Date.class, date);
            TestCase.assertEquals(Date.class, result.getClass());
            TestCase.assertEquals(date, result);

            result = ZKClassUtils.getValueByClass(Date.class, date.getTime());
            TestCase.assertEquals(Date.class, result.getClass());
            TestCase.assertEquals(date, result);

            result = ZKClassUtils.getValueByClass(int.class, null);
            TestCase.assertNull(result);

            result = ZKClassUtils.getValueByClass(int.class, "1");
            TestCase.assertEquals(Integer.class, result.getClass());
            TestCase.assertEquals(1, result);

            result = ZKClassUtils.getValueByClass(int.class, 2);
            TestCase.assertEquals(Integer.class, result.getClass());
            TestCase.assertEquals(2, result);

            result = ZKClassUtils.getValueByClass(String.class, "");
            TestCase.assertEquals(String.class, result.getClass());
            TestCase.assertEquals("", result);

            result = ZKClassUtils.getValueByClass(String.class, "String");
            TestCase.assertEquals(String.class, result.getClass());
            TestCase.assertEquals("String", result);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
