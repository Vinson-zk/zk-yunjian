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
 * @Title: DoubleSession.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 9:39:42 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 
* @ClassName: DoubleSession 
* @Description: TODO(simple description this class what to do.) 
* @author 熊镇 Vinson 
* @version 1.0 
*/
public class DoubleSession {

    List<DoubleMember> dms;

    public DoubleSession() {

    }

    public DoubleSession(DoubleMember[] dms) {
        this.dms = Arrays.asList(dms);
    }

    public DoubleSession(List<DoubleMember> dms) {
        this.getDms().addAll(dms);
    }

    /**
     * @return dms
     */
    public List<DoubleMember> getDms() {
        if (dms == null) {
            this.dms = new ArrayList<DoubleMember>();
        }
        return dms;
    }

    /**
     * @param dms
     *            the dms to set
     */
    public void setDms(List<DoubleMember> dms) {
        this.dms = dms;
    }

    /**
     * @param dms
     *            the dms to set
     */
    public void addDoubleMember(DoubleMember dm) {
        this.dms.add(dm);
    }

    /**
     * 判断双打队伍中，是否已有选手在本场次中。
     *
     * @Title: isInside
     * @Description: TODO(simple description this method what to do.)
     * @author 熊镇 Vinson
     * @date Jun 20, 2019 10:10:08 AM
     * @param ds
     * @param dm
     * @return
     * @return boolean
     */
    public boolean isInside(DoubleMember dm) {

        for (DoubleMember alreadyDm : this.getDms()) {
            if (alreadyDm.haveSameMember(dm)) {
                return true;
            }
        }
        return false;
    }

}
