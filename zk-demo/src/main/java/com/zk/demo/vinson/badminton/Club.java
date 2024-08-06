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
 * @Title: Club.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 7:39:35 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

/**
 * 球会
 * 
 * @ClassName: Club
 * @Description: TODO(simple description this class what to do.)
 * @author 熊镇 Vinson
 * @version 1.0
 */
public class Club {

    /**
     * 球会名称
     */
    private String name;

    /**
     * 球会胜率
     */
    private float winRate;

    /**
     * 具体成员在球会中的个人胜率，计入选手的个人胜率，数据不存在玩会表中，存在选手与球会的关系表中
     */
    private float memberWinRate;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return winRate
     */
    public float getWinRate() {
        return winRate;
    }

    /**
     * @param winRate
     *            the winRate to set
     */
    public void setWinRate(float winRate) {
        this.winRate = winRate;
    }

    /**
     * @return memberWinRate
     */
    public float getMemberWinRate() {
        return memberWinRate;
    }

    /**
     * @param memberWinRate
     *            the memberWinRate to set
     */
    public void setMemberWinRate(float memberWinRate) {
        this.memberWinRate = memberWinRate;
    }

}
