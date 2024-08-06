/**   
 * Copyright (c) 2004-2014 i-Sprint Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * i-Sprint Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with i-Sprint. 
 *
 * @Title: MathUtilsTest.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 8:25:20 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: MathUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author 熊镇 Vinson 
* @version 1.0 
*/
public class MathUtilsTest {

    // 阶乘 Factorial
    @Test
    public void testFactorial() {
        try {
            int source = 0;
            int target = 0;

            source = 0;
            target = 1;
            TestCase.assertEquals(target, MathUtils.factorial(source).intValue());

            source = 1;
            target = 1;
            TestCase.assertEquals(target, MathUtils.factorial(source).intValue());

            source = 2;
            target = 2;
            TestCase.assertEquals(target, MathUtils.factorial(source).intValue());

            source = 3;
            target = 6;
            TestCase.assertEquals(target, MathUtils.factorial(source).intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 组合 Combination
    @Test
    public void testCombination() {
        try {
            int n = 0;
            int m = 0;
            int target = 0;

            n = 3;
            m = 1;
            target = 3;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 3;
            m = 2;
            target = 3;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 3;
            m = 3;
            target = 1;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 4;
            m = 1;
            target = 4;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 4;
            m = 2;
            target = 6;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 4;
            m = 3;
            target = 4;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 4;
            m = 4;
            target = 1;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 12;
            m = 2;
            target = 66;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 10;
            m = 2;
            target = 45;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 8;
            m = 2;
            target = 28;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());

            n = 18;
            m = 2;
            target = 153;
            TestCase.assertEquals(target, MathUtils.combination(n, m).intValue());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 排列 Arrange
    @Test
    public void testArrange() {
        try {
            int n = 0;
            int m = 0;
            int target = 0;

            n = 3;
            m = 1;
            target = 3;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

            n = 3;
            m = 2;
            target = 6;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

            n = 3;
            m = 3;
            target = 6;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

            n = 4;
            m = 1;
            target = 4;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

            n = 4;
            m = 2;
            target = 12;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

            n = 4;
            m = 3;
            target = 24;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

            n = 4;
            m = 4;
            target = 24;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

            n = 10;
            m = 2;
            target = 90;
            TestCase.assertEquals(target, MathUtils.arrange(n, m).intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
