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
 * @Title: DoubleArrangeUtils.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 9:56:35 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 双打队伍组合计算
 * 
 * @ClassName: DoubleArrangeUtils
 * @Description: TODO(simple description this class what to do.)
 * @author 熊镇 Vinson
 * @version 1.0
 */
public class DoubleArrangeUtils {

    // 取所有双打组合
    public static List<DoubleMember> arrangeMember(Collection<Member> members) {
        return arrangeMember(members.toArray(new Member[members.size()]));
    }

    // 取所有双打组合
    public static List<DoubleMember> arrangeMember(Member... members) {

        List<DoubleMember> dmList = new ArrayList<DoubleMember>();
        for (int i = 1; i < members.length; ++i) {
            for (int j = i; j < members.length; ++j) {
                dmList.add(new DoubleMember(members[i - 1], members[j]));
            }
        }

        return dmList;
    }

    // 判断某个双打队伍是否在需要过虑
    public static boolean isFilter(List<DoubleMember> filterDmList, DoubleMember targetDm) {
//        if (filterDmList != null && filterDmList.size() > 0) {
            for (DoubleMember dm : filterDmList) {
                if (dm.isSame(targetDm)) {
                    return true;
                }
            }
//        }
        return false;
    }

    public static DoubleMember dispatchSession(DoubleSession ds, List<DoubleMember> dmList,
            List<DoubleMember> filterDmList) {

        for (DoubleMember targetDm : dmList) {
            // 需要过虑的队伍
            if (!isFilter(filterDmList, targetDm)) {
                // 如果没有在本场次中，加入本场次;
                if (!ds.isInside(targetDm)) {
                    // 符合加入本场次要求的队伍
                    return targetDm;
                }
            }
        }

        return null;
    }

    public static DoubleSession dispatchSession(List<DoubleMember> confirmDmList, List<DoubleMember> dmList,
            List<DoubleMember> filterDmList, int sessionDmCount) {
        DoubleSession ds = new DoubleSession();

        if (confirmDmList != null) {
            ds.getDms().addAll(confirmDmList);
        }

        do {
            
            DoubleMember dm = dispatchSession(ds, dmList, filterDmList);
            if (dm != null) {
                ds.addDoubleMember(dm);
            }
            else {
                if (sessionDmCount == 0 || ds.getDms().size() == sessionDmCount) {
                    return ds;
                }

                for (int i = 0; i < ds.getDms().size(); ++i) {
                    DoubleMember filterDM = ds.getDms().get(0);
                    // 将本场次中最后一队，踢除，并在重新组成场次排除这一队
                    filterDmList.add(filterDM);
                    ds.getDms().remove(filterDM);
                    
                    List<DoubleMember> tFilterList = new ArrayList<>();
                    tFilterList.addAll(filterDmList);
                    
                    DoubleSession tds = dispatchSession(ds.getDms(), dmList, tFilterList, sessionDmCount);

                    if (tds != null) {
                        return tds;
                    }
                    else {
                        filterDmList.remove(filterDM);
                        ds.getDms().add(filterDM);
                    }
                }
                
                for (int i = 0; i < ds.getDms().size(); ++i) {

                    DoubleMember filterDM = ds.getDms().get(0);
                    // 将本场次中最后一队，踢除，并在重新组成场次排除这一队
                    filterDmList.add(filterDM);
                    ds.getDms().remove(filterDM);

                    List<DoubleMember> tFilterList = new ArrayList<>();
                    tFilterList.addAll(filterDmList);

                    DoubleSession tds = dispatchSession(ds.getDms(), dmList, tFilterList, sessionDmCount);

                    if (tds != null) {
                        return tds;
                    }
                }


                return null;
            }
        } while (ds.getDms().size() > 0);

        return null;
    }

    public static List<DoubleSession> dispatchSession(List<DoubleMember> dmList, int sessionDmCount) {

        List<DoubleSession> dmsList = new ArrayList<>();

        DoubleSession ds = dispatchSession(null, dmList, new ArrayList<>(), sessionDmCount);
        while (ds != null) {
            if (ds != null) {
                // 成功组成场次
                dmsList.add(ds);
                for (DoubleMember targetDm : ds.getDms()) {
                    dmList.remove(targetDm);
                }
                sessionDmCount = ds.getDms().size();
            }
            ds = dispatchSession(null, dmList, new ArrayList<>(), sessionDmCount);
        }

        // 未成功组成场次的队伍，理论上选手总数是双数时，不会出现些情况
        if (dmList.size() > 0) {
            ds = new DoubleSession(dmList);
            dmsList.add(ds);
        }

        return dmsList;
    }

    // 所有组合排场次，一个人只在一个场次中出现一次；
    public static List<DoubleSession> dispatchSession(List<DoubleMember> doubleMembers) {
        List<DoubleSession> dmsList = dispatchSession(doubleMembers, 0);

        return dmsList;
    }

}
