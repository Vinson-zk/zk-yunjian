package com.zk.db.commons;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zk.core.utils.ZKClassUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.helper.entity.ZKDBTestSampleEntity;

import junit.framework.TestCase;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 实体注解映射 测试
 * @ClassName ZKDBMapInfoTest
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-28 09:47:32
 **/
public class ZKDBMapInfoTest {

    @Test
    public void testAnnotation() {
        try {

            ZKDBMapInfo mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);

            for (Map.Entry<PropertyDescriptor, ZKColumn> e : mapInfo.getColumnInfo().entrySet()) {
                System.out.println("20200910-1642-002 column.name: " + e.getKey().getName() + " -> "
                        + e.getValue().name()
                        + " -> " + e.getValue().javaType());
            }

            TestCase.assertEquals("t_zk_db_test", mapInfo.getTable().name());
            TestCase.assertEquals(13, mapInfo.getColumnInfo().size());
            TestCase.assertEquals("c_int", mapInfo.getColumn("mInt").name());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void test() {
        try {

            BeanInfo beanInfo = Introspector.getBeanInfo(ZKDBTestSampleEntity.class);

            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                System.out.println("-------------------- 0 ");
                System.out.println("[^_^:20200917-1114-001] PropertyDescriptor.getName: " + pd.getName());
                System.out.println("[^_^:20200917-1114-001] PropertyDescriptor.getName: " + pd.getReadMethod());
//                System.out.println("[^_^:20200917-1114-001] PropertyDescriptor.getDisplayName: " + pd.getDisplayName());
//                System.out
//                        .println("[^_^:20200917-1114-001] PropertyDescriptor.getPropertyType: " + pd.getPropertyType());
//
//                Enumeration<String> es = pd.attributeNames();
//                while (es.hasMoreElements()) {
//                    System.out
//                            .println("[^_^:20200917-1114-001] PropertyDescriptor.attributeNames: " + es.nextElement());
//                }

            }

            System.out.println("==============================  ");
            System.out.println("==============================  ");
            System.out.println("==============================  ");

            List<Field> fs = ZKClassUtils.getAllField(ZKDBTestSampleEntity.class);
            for (Field f : fs) {
                System.out.println("-------------------- 2 ");
                System.out.println("[^_^:20200917-1114-001] Field.getName: " + f.getName());
            }

            System.out.println("==============================  ");
            System.out.println("==============================  ");
            System.out.println("==============================  ");

            for (MethodDescriptor md : beanInfo.getMethodDescriptors()) {
                System.out.println("-------------------- 1 ");
                System.out.println("[^_^:20200917-1114-001] MethodDescriptor.getName: " + md.getName());
//                System.out.println("[^_^:20200917-1114-001] MethodDescriptor.getDisplayName: " + md.getDisplayName());
//                System.out.println(
//                        "[^_^:20200917-1114-001] MethodDescriptor.getShortDescription: " + md.getShortDescription());
//                System.out.println(
//                        "[^_^:20200917-1114-001] MethodDescriptor.getMethod().getName: " + md.getMethod().getName());
//
//                Enumeration<String> es = md.attributeNames();
//                while (es.hasMoreElements()) {
//                    System.out.println("[^_^:20200917-1114-001] MethodDescriptor.attributeNames: " + es.nextElement());
//                }

            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
