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
 * @Title: BadmintonTestHelper.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 9:12:20 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

import java.util.List;

/** 
* @ClassName: BadmintonTestHelper 
* @Description: TODO(simple description this class what to do.) 
* @author 熊镇 Vinson 
* @version 1.0 
*/
public class BadmintonTestHelper {

    public static int count = 16;

    static Member[] ms = null;

    public static Member[] getMembers() {
        if (ms == null) {
            ms = new Member[BadmintonTestHelper.count];
            for (int i = 1; i <= BadmintonTestHelper.count; ++i) {
                int index = i - 1;
                ms[index] = new Member();
                ms[index].setNumber(String.format("num-%02d", i));
                ms[index].setName(ms[index].getNumber());
            }
        }
        return ms;
    }

    // 打印双打队伍
    public static void printDoubleMember(DoubleMember dm) {
        System.out.println(String.format("[%s~%s]", dm.getMemberN1().getNumber(), dm.getMemberN2().getNumber()));
    }

    // 打印双打队伍
    public static void printDoubleMember(List<DoubleMember> dms) {
        System.out.println("[^_^:20190621-0123-001] 队伍数量:" + dms.size());
        for (DoubleMember dm : dms) {
            printDoubleMember(dm);
        }
    }

    // 打印场次中的队伍
    public static void printDoubleSession(List<DoubleSession> doubleSessions) {
        System.out.println("[^_^:20190620-144-001] 场次:");
        int i = 1;
        for (DoubleSession ds : doubleSessions) {
            System.out.println("场次:" + i);
            printDoubleMember(ds.getDms());
            ++i;
        }
    }

}
