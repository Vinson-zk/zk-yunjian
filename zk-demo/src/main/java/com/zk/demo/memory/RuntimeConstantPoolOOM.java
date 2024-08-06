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
* @Title: RuntimeConstantPoolOOM.java 
* @author Vinson 
* @Package com.zk.demo.memory 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 29, 2020 2:01:55 PM 
* @version V1.0 
*/
package com.zk.demo.memory;

import java.util.ArrayList;
import java.util.List;

/** 
* @ClassName: RuntimeConstantPoolOOM 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class RuntimeConstantPoolOOM {
    /**
     * 方法区和运行时常量池内存测试
     * 
     * VM Args:-XX:PermSize=10m -XX:MaxPermSize=10m
     */

    // 运行时常量池内存溢出异常
    public static void constantPool() {
        // 使用 List 保持常量池引用，避免 Full GC 回收常量池
        List<String> list = new ArrayList<String>();

        // 10M 的 PermSize 在 integer 范围内足够产生 OOM
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }

    // String.intern() 返回引用测试
    public static void returnReference() {
        /*
         * jdk 1.6 两个输出为 false
         * 
         * jdk 1.7 一个 true 一个 false
         * 
         * 原因：
         * 
         * jdk 1.6 intern() 方法会把首次人遇到的字符串实例复制到永久代上；而 StringBuilder 创建的字符串实例在
         * JAVA 堆上，所以两个是不同的引用，返回 false;
         * 
         * jdk 1.7 intern() 不会再复制实例，只是在常量池中记录首次出现的实例；即："java-第二次出现的字符串"，在
         * StringBuilder.toString() 之前 str1 中出现过，所以，str2.intern() 与第一个 str1
         * 相同；但不与 str2 相同
         * 
         * 所以同一个字符串不会常量池出现两个；
         */
        String str = new StringBuilder("计算机").append("软件").toString();
        System.out.println("[^_^:20200729-1410-001] " + (str.intern() == str));
//        System.out.println("[^_^:20200729-1410-001] str.intern：" + str.intern());

        String str1 = "java-第二次出现的字符串";
        String str2 = new StringBuilder("ja").append("va-第二次出现的字符串").toString();
        System.out.println("[^_^:20200729-1410-002] str1:" + (str2.intern() == str1));
        System.out.println("[^_^:20200729-1410-002] str2:" + (str2.intern() == str2));
//        System.out.println("[^_^:20200729-1410-002] str1.intern：" + str1.intern() + "  str1: " + str1);
//        System.out.println("[^_^:20200729-1410-002] str2.intern：" + str2.intern() + "  str2: " + str2);
    }

    public static void main(String[] args) {
        returnReference();
        constantPool();
    }

}
