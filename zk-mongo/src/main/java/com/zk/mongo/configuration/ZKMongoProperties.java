/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKMongoProperties.java 
 * @author Vinson 
 * @Package com.zk.mongo.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:59:59 PM 
 * @version V1.0   
*/
package com.zk.mongo.configuration;

/** 
* @ClassName: ZKMongoProperties 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoProperties {

    /**
     * # mongodb 服务器 URL 及端口 port; 集群是使用这个配置
     * 
     * 例：172.16.20.129:10001,172.16.20.129:10002,172.16.20.129:10003
     */
//    @Value("${mongodb.url}")
    private String url;

//    // mongodb 服务器 端口，mongodb 服务默认端口为 27017，集群时端口拼接在URL中，此项将不起作用
////    @Value("${mongodb.port:27017}")
//    private int mongodbPort;

    /**
     * # mongodb 服务器实例名，schema
     */
//    @Value("${mongodb.dbname}")
    private String dbname;

    /**
     * # mongodb 用户名
     */
//    @Value("${mongodb.username}")
    private String username;

    /**
     * # mongodb 用户名密码
     */
//    @Value("${mongodb.password}")
    private String password;

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return dbname
     */
    public String getDbname() {
        return dbname;
    }

    /**
     * @param dbname
     *            the dbname to set
     */
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
