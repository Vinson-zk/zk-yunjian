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
 * @Title: DoubleArrangeUtilsTest.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 9:56:58 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: DoubleArrangeUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author 熊镇 Vinson 
* @version 1.0 
*/
public class DoubleArrangeUtilsTest {

    @Test
    public void test() {
        try {
            List<Member> ms = new ArrayList<Member>();
            ms.add(new Member("M1", "M1", Member.FLAG_SEX.Male));
            ms.add(new Member("M2", "M2", Member.FLAG_SEX.Male));
            ms.add(new Member("M3", "M3", Member.FLAG_SEX.Male));
            ms.add(new Member("F1", "F1", Member.FLAG_SEX.Female));
            ms.add(new Member("F2", "F2", Member.FLAG_SEX.Female));
            ms.add(new Member("F3", "F3", Member.FLAG_SEX.Female));

            List<DoubleMember> dms = DoubleArrangeUtils.arrangeMember(ms);
            BadmintonTestHelper.printDoubleMember(dms);
            TestCase.assertEquals(dms.size(), MathUtils.combination(ms.size(), 2).intValue());

//            List<DoubleSession> dss = DoubleArrangeUtils.dispatchSession(dms);
//            BadmintonTestHelper.printDoubleSession(dss);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testArrangeMember() {
        try {
            Member[] ms = BadmintonTestHelper.getMembers();
            List<DoubleMember> dmList = DoubleArrangeUtils.arrangeMember(ms);
            TestCase.assertEquals(dmList.size(), MathUtils.combination(ms.length, 2).intValue());

            BadmintonTestHelper.printDoubleMember(dmList);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDispatchSession() {
        try {
            Member[] ms = BadmintonTestHelper.getMembers();

            List<DoubleMember> dms = DoubleArrangeUtils.arrangeMember(ms);
            TestCase.assertEquals(dms.size(), MathUtils.combination(ms.length, 2).intValue());

            List<DoubleSession> dss = DoubleArrangeUtils.dispatchSession(dms);
            BadmintonTestHelper.printDoubleSession(dss);
            TestCase.assertEquals(dss.size(), BadmintonTestHelper.count - 1);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
