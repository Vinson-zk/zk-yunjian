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
 * @Title: DoubleMember.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 7:51:49 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

/**
 * 双打队伍
 * 
 * @ClassName: DoubleMember
 * @Description: TODO(simple description this class what to do.)
 * @author 熊镇 Vinson
 * @version 1.0
 */
public class DoubleMember {

    static interface FLAG_DOUBLE_MODE {

        /**
         * 无法判断
         */
        public static int Unknown = 1;

        /**
         * 混双
         */
        public static int Mixed = 1;

        /**
         * 女双
         */
        public static int Female = 2;

        /**
         * 男双
         */
        public static int Male = 0;
    }

    /**
     * 双打队员1
     */
    private Member memberN1;

    /**
     * 双打队员2
     */
    private Member memberN2;

    public DoubleMember(Member m1, Member m2) {
        this.memberN1 = m1;
        this.memberN2 = m2;
    }

    /**
     * @return memberN1
     */
    public Member getMemberN1() {
        return memberN1;
    }

    /**
     * @param memberN1
     *            the memberN1 to set
     */
    public void setMemberN1(Member memberN1) {
        this.memberN1 = memberN1;
    }

    /**
     * @return memberN2
     */
    public Member getMemberN2() {
        return memberN2;
    }

    /**
     * @param memberN2
     *            the memberN2 to set
     */
    public void setMemberN2(Member memberN2) {
        this.memberN2 = memberN2;
    }

    /**
     * 胜率，能过两个选手的胜率计算得出
     */
    public float getWinRate() {
        return 1;
    }

    /**
     * 判断双打队伍是否有相同一个人
     *
     * @Title: haveSameMember
     * @Description: TODO(simple description this method what to do.)
     * @author 熊镇 Vinson
     * @date Jun 20, 2019 10:08:16 AM
     * @param dm
     * @return
     * @return boolean true-有相同的人，false-没有相同的人
     */
    public boolean haveSameMember(DoubleMember dm) {
        if (this.getMemberN1().getNumber().equals(dm.getMemberN1().getNumber())) {
            return true;
        }
        if (this.getMemberN1().getNumber().equals(dm.getMemberN2().getNumber())) {
            return true;
        }
        if (this.getMemberN2().getNumber().equals(dm.getMemberN1().getNumber())) {
            return true;
        }
        if (this.getMemberN2().getNumber().equals(dm.getMemberN2().getNumber())) {
            return true;
        }
        return false;
    }

    /**
     * 是否是同一个双打队伍
     *
     * @Title: isSame
     * @Description: TODO(simple description this method what to do.)
     * @author 熊镇 Vinson
     * @date Jun 20, 2019 12:14:14 PM
     * @param dm
     * @return
     * @return boolean
     */
    public boolean isSame(DoubleMember dm) {
        if (this.getMemberN1().getNumber().equals(dm.getMemberN1().getNumber())
                && this.getMemberN2().getNumber().equals(dm.getMemberN2().getNumber())) {
            return true;
        }
        if (this.getMemberN1().getNumber().equals(dm.getMemberN2().getNumber())
                && this.getMemberN2().getNumber().equals(dm.getMemberN1().getNumber())) {
            return true;
        }

        return false;
    }

}
