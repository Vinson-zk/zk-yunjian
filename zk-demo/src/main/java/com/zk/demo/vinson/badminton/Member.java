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
 * @Title: Member.java 
 * @author 熊镇 Vinson 
 * @Package com.bs.test.badminton 
 * @Description: TODO(simple description this file what to do.) 
 * @date Jun 20, 2019 7:35:07 AM 
 * @version V1.0   
*/
package com.zk.demo.vinson.badminton;

/** 
* @ClassName: Member 
* @Description: TODO(simple description this class what to do.) 
* @author 熊镇 Vinson 
* @version 1.0 
*/
public class Member {

    /**
     * 性别标识
     * 
     * @ClassName: SEX
     * @Description: TODO(simple description this class what to do.)
     * @author 熊镇 Vinson
     * @version 1.0
     */
    static interface FLAG_SEX {

        /**
         * 未知
         */
        public static int Unknown = 0;

        /**
         * 女
         */
        public static int Female = 1;

        /**
         * 男
         */
        public static int Male = 2;

    }

    public Member() {

    }

    public Member(String name, String number, int gender) {
        this.name = name;
        this.number = number;
        this.gender = gender;
    }

    /**
     * 
     */
    private int gender = FLAG_SEX.Unknown;

    /**
     * 选手名字
     */
    private String name;

    /**
     * 选手编号，唯一值
     */
    private String number;

    /**
     * 个人胜率
     */
    private float personalWinRate;

    /**
     * 
     */
    private Club[] glubs;

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
     * @return personalWinRate
     */
    public float getPersonalWinRate() {
        return personalWinRate;
    }

    /**
     * @param personalWinRate
     *            the personalWinRate to set
     */
    public void setPersonalWinRate(float personalWinRate) {
        this.personalWinRate = personalWinRate;
    }

    /**
     * @return glubs
     */
    public Club[] getGlubs() {
        return glubs;
    }

    /**
     * @param glubs
     *            the glubs to set
     */
    public void setGlubs(Club[] glubs) {
        this.glubs = glubs;
    }

    /**
     * @return gender
     */
    public int getGender() {
        return gender;
    }

    /**
     * @param gender
     *            the gender to set
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * @return number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number
     *            the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 取个人胜率的值；通球会胜率，选手在球会中的胜率，个人胜率计算得出
     *
     * @Title: getWinRate
     * @Description: TODO(simple description this method what to do.)
     * @author 熊镇 Vinson
     * @date Jun 20, 2019 7:46:13 AM
     * @return void
     */
    public float getWinRate() {
        // 暂直接取个人胜率
        return this.personalWinRate;
    }

}
