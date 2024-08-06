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
* @Title: ZKDBProperties.java 
* @author Vinson 
* @Package com.zk.db.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 2:36:56 PM 
* @version V1.0 
*/
package com.zk.db.configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @ClassName: ZKDBProperties
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKDBProperties {

    // 写数据 的 连接地址
    private String writeUrl;

    // 可读可写的用户名称
    private String writeUserName;

    // 可读可写的用户密码
    private String writePwd;

    // 读数据 的 连接地址
    private String readUrl;

    // 只读用户
    private String readUserName;

    // 只读用户密码
    private String readPwd;

    DruidDataSource publicDruidPool;

    /**
     * @return writeUrl sa
     */
    public String getWriteUrl() {
        return writeUrl;
    }

    /**
     * @return writeUserName sa
     */
    public String getWriteUserName() {
        return writeUserName;
    }

    /**
     * @return writePwd sa
     */
    public String getWritePwd() {
        return writePwd;
    }

    /**
     * @return readUrl sa
     */
    public String getReadUrl() {
        return readUrl;
    }

    /**
     * @return readUserName sa
     */
    public String getReadUserName() {
        return readUserName;
    }

    /**
     * @return readPwd sa
     */
    public String getReadPwd() {
        return readPwd;
    }

    /**
     * @param writeUrl
     *            the writeUrl to set
     */
    public void setWriteUrl(String writeUrl) {
        this.writeUrl = writeUrl;
    }

    /**
     * @param writeUserName
     *            the writeUserName to set
     */
    public void setWriteUserName(String writeUserName) {
        this.writeUserName = writeUserName;
    }

    /**
     * @param writePwd
     *            the writePwd to set
     */
    public void setWritePwd(String writePwd) {
        this.writePwd = writePwd;
    }

    /**
     * @param readUrl
     *            the readUrl to set
     */
    public void setReadUrl(String readUrl) {
        this.readUrl = readUrl;
    }

    /**
     * @param readUserName
     *            the readUserName to set
     */
    public void setReadUserName(String readUserName) {
        this.readUserName = readUserName;
    }

    /**
     * @param readPwd
     *            the readPwd to set
     */
    public void setReadPwd(String readPwd) {
        this.readPwd = readPwd;
    }

    /**
     * @return publicDruidPool sa
     */
    public DruidDataSource getPublicDruidPool() {
        return publicDruidPool;
    }

    /**
     * @param publicDruidPool
     *            the publicDruidPool to set
     */
    public void setPublicDruidPool(DruidDataSource publicDruidPool) {
        this.publicDruidPool = publicDruidPool;
    }

}
