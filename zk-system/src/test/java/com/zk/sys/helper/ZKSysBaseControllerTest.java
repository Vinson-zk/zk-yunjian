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
* @Title: ZKSysBaseControllerTest.java 
* @author Vinson 
* @Package com.zk.sys.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 21, 2020 10:17:35 AM 
* @version V1.0 
*/
package com.zk.sys.helper;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/** 
* @ClassName: ZKSysBaseControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKSysTestHelper.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.sys" })
public class ZKSysBaseControllerTest {


    @LocalServerPort
    private int port;

    @Value("${zk.path.admin}")
    private String adminPath;

    @Value("${zk.path.sys}")
    private String modulePath;

    @Value("${zk.sys.version}")
    private String version;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/%s", port, adminPath, modulePath, version);
    }

    /**
     * @return port sa
     */
    public int getPort() {
        return port;
    }

    /**
     * @return testRestTemplate sa
     */
    public TestRestTemplate getTestRestTemplate() {
        return testRestTemplate;
    }

    /**
     * @return adminPath sa
     */
    public String getAdminPath() {
        return adminPath;
    }

    /**
     * @return modulePath sa
     */
    public String getModulePath() {
        return modulePath;
    }

    /**
     * @return baseUrl sa
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @return version sa
     */
    public String getVersion() {
        return version;
    }

}
