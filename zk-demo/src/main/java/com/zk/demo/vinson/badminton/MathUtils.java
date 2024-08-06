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
 * @Title: MathUtils.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 8:14:30 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

import java.math.BigInteger;

/** 
* @ClassName: MathUtils 
* @Description: TODO(simple description this class what to do.) 
* @author 熊镇 Vinson 
* @version 1.0 
*/
public class MathUtils {
    /**
     * 阶乘，源数据最好不要太大，小心溢出； 注：0 的阶乘是 1
     *
     * @Title: factorial
     * @Description: TODO(simple description this method what to do.)
     * @author 熊镇 Vinson
     * @date Jun 20, 2019 8:22:15 AM
     * @param source
     * @return
     * @return BigInteger
     */
    public static BigInteger factorial(int source) {

        BigInteger result = BigInteger.valueOf(1);
        for (int i = 1; i < source; ++i) {
            result = result.multiply(BigInteger.valueOf(i + 1));
        }
        return result;
    }

    /**
     * 组合
     *
     * @Title: combination
     * @Description: TODO(simple description this method what to do.)
     * @author 熊镇 Vinson
     * @date Jun 20, 2019 8:37:52 AM
     * @param n
     * @param m
     *            m 小于等于 n
     * @return
     * @return BigInteger
     */
    public static BigInteger combination(int n, int m) {
        return factorial(n).divide((factorial(m).multiply(factorial(n - m))));
    }

    /**
     * 排列
     *
     * @Title: arrange
     * @Description: TODO(simple description this method what to do.)
     * @author 熊镇 Vinson
     * @date Jun 20, 2019 8:40:03 AM
     * @param n
     * @param m
     *            m 小于等于 n
     * @return
     * @return BigInteger
     */
    public static BigInteger arrange(int n, int m) {
        return factorial(n).divide(factorial(n - m));
    }

}
