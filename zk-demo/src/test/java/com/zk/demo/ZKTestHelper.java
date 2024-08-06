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
* @Title: ZKTestHelper.java 
* @author Vinson 
* @Package com.zk.test 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 24, 2022 2:49:58 PM 
* @version V1.0 
*/
package com.zk.demo;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.zk.core.utils.ZKDateUtils;

/** 
* @ClassName: ZKTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTestHelper {

    public static class ZK {

        public static void sleep(String funName, double second) {
            sleep(funName, second, false);
        }

        public static void sleep(String funName, double second, boolean isPrint) {
            try {
                if (isPrint)
                    System.out.println(String.format("[^_^:20240327-2349-001] [%s]方法: 休息%s秒！", funName, second));
                Thread.sleep((long) (second * 1000l));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static void p(String flag, String into) {
            String printFromt = "[%s]:[%s].%s: %s";
            System.out
                    .println(String.format(printFromt, flag, ZKDateUtils.formatDate(ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss),
                            Thread.currentThread().getName(), into));
        }
    }

    // ==============================================

    class Node {
        public int data;
        public Node next;
        public Node(int d, Node next) {
            this.data = d;
            this.next = next;
        }
    }

    @Test
    public void test() {
        String timeStr = "1657537721";
        Date d = ZKDateUtils.parseDate(Long.valueOf(timeStr) * 1000);
        System.out.println("[^_^:20220522-2016-001] 1653218795 -> date : " + d.getTime());
        System.out.println("[^_^:20220522-2016-001] 1653218795 -> date : " + ZKDateUtils.formatDate(d));

        d = ZKDateUtils.parseDate(Integer.valueOf(timeStr) * 1000);
        System.out.println("[^_^:20220522-2016-002] 1653218795 -> date : " + d.getTime());
        System.out.println("[^_^:20220522-2016-002] 1653218795 -> date : " + ZKDateUtils.formatDate(d));

    }

    /*
    21365 -> 56312
     */
    @Test
    public void testNode() {
//        String timeStr = "1653218795";
//        Date d = ZKDateUtils.parseDate(Long.valueOf(timeStr) * 1000);
//
//        System.out.println("[^_^:20220522-2016-001] 1653218795 -> date : " + d.getTime());
//        System.out.println("[^_^:20220522-2016-001] 1653218795 -> date : " + ZKDateUtils.formatDate(d));

        // 21365
        Node head = new Node(5, null);

        head = new Node(6, head);
        head = new Node(3, head);
        head = new Node(1, head);
        head = new Node(2, head);
        s(head);
    }

    public static Node s(Node head) {
        printNode(head);
        System.out.println("=====================================================================");
        Node s = null;
//        s = head.next;
//        head.next = null;
//        s = s1(head, s);
        s = s2(head);
        printNode(s);

        return null;
    }

    public static void printNode(Node n){
        System.out.println("print node begin ----------------------------------- ");
        while(n.next != null) {
            System.out.print(n.data + "->");
            n = n.next;
        }
        System.out.println(n.data + "->null");
        System.out.println("print node end ----------------------------------- ");
    }

    public static Node s2(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        else {
            Node next = head.next;
            Node t = s2(next);
            head.next = next.next;
            next.next = head;

            return t;
        }
    }

    public static Node s1(Node head, Node next) {
        if (next == null) {
            return head;
        }
        else {
            if (next.next != null) {
                Node cNext = next.next;
//                head.next = next.next;
                next.next = head;
                return s1(next, cNext);
            }
            else {
                next.next = head;
                return next;
            }
        }
    }


    @Test
    public void genPwd() {
        String sourceStr = "1234567890qwertyuiopasdfghjklzxcvbnm!@#$%*()_+?.][QWERTYUIOPASDFGHJKLZXCVBNM";

        int length = 6;
        int maxLen = sourceStr.length();

        String pwd = "";

        Random r = new Random(ZKDateUtils.getToday().getTime());
        for (int i = 0; i < length; i++) {
            int index = r.nextInt(maxLen);
            pwd += sourceStr.toCharArray()[index];
        }
        System.out.println("[^_^:20220124-1452-001] 生成的 password : " + pwd);

    }

}
