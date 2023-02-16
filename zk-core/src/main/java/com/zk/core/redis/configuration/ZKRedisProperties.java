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
* @Title: ZKRedisProperties.java 
* @author Vinson 
* @Package com.zk.core.redis.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 4:04:22 PM 
* @version V1.0 
*/
package com.zk.core.redis.configuration;

import redis.clients.jedis.JedisPoolConfig;

/** 
* @ClassName: ZKRedisProperties 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRedisProperties {

    String host;

    int port;

    int database;

    String password;

    int timeout;

    JedisPoolConfig jedisPool;

    /**
     * @return host sa
     */
    public String getHost() {
        return host;
    }

    /**
     * @return port sa
     */
    public int getPort() {
        return port;
    }

    /**
     * @return database sa
     */
    public int getDatabase() {
        return database;
    }

    /**
     * @return password sa
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return timeout sa
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @return jedisPool sa
     */
    public JedisPoolConfig getJedisPool() {
        return jedisPool;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param database
     *            the database to set
     */
    public void setDatabase(int database) {
        this.database = database;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @param jedisPool
     *            the jedisPool to set
     */
    public void setJedisPool(JedisPoolConfig jedisPool) {
        this.jedisPool = jedisPool;
    }


}
