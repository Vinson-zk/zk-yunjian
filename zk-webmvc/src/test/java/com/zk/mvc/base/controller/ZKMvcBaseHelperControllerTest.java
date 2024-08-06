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
 * @Title: ZKMvcBaseHelperControllerTest.java 
 * @author Vinson 
 * @Package com.zk.mvc.base.controller
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 4:01:28 PM 
 * @version V1.0   
*/
package com.zk.mvc.base.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.base.helper.dto.ZKBaseHelperEntityStringDto;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.mvc.base.helper.ZKMvcBaseHelperSpringBootMain;

import junit.framework.TestCase;

/**
 * @ClassName: ZKMvcBaseHelperControllerTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = ZKMvcBaseHelperSpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZKMvcBaseHelperControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = "http://127.0.0.1:" + port + "/mvc/base/helper";
    }

    public static interface Param_Name {
        public static String nString = "nString";

        public static String nInt = "nInt";

        public static String nList = "nList";

        public static String nArray = "nArray";

        public static String nZKJson = "nJson";
    }

    @Test
    public void testBaseHelperData() {
        try {

            List<String> listString = new ArrayList<String>();
            listString.add("aaa");
            int[] zkrayInt = new int[] { 1, 2 };

            ZKJson zkJsonTemp = new ZKJson();
            zkJsonTemp.put(Param_Name.nString, "v_" + Param_Name.nString);
            zkJsonTemp.put(Param_Name.nInt, 2);
            zkJsonTemp.put(Param_Name.nList, listString);
            zkJsonTemp.put(Param_Name.nArray, zkrayInt);

            ZKJson zkJson = new ZKJson();
            zkJson.put(Param_Name.nString, "v_" + Param_Name.nString);
            zkJson.put(Param_Name.nInt, 2);
            zkJson.put(Param_Name.nList, listString);
            zkJson.put(Param_Name.nArray, zkrayInt);
            zkJson.put(Param_Name.nZKJson, zkJsonTemp);

            ZKBaseHelperEntityStringDto zkBhe = new ZKBaseHelperEntityStringDto();
            zkBhe.setSpareJson(zkJson);
            zkBhe.setCreateDate(new Date());
            zkBhe.setSpare2("spare2");

            ResponseEntity<String> response = null;
//            response = testRestTemplate.postForEntity(this.baseUrl + "/data", zkBhe, String.class);

            // {"nInt":2,"nArray":[1,2],"nString":"v_nString","nZKJson":{"nInt":2,"nArray":[1,2],"nString":"v_nString","nList":["aaa"]},"nList":["aaa"]}
            // %7B%22nInt%22%3A2%2C%22nArray%22%3A%5B1%2C2%5D%2C%22nString%22%3A%22v_nString%22%2C%22nZKJson%22%3A%7B%22nInt%22%3A2%2C%22nArray%22%3A%5B1%2C2%5D%2C%22nString%22%3A%22v_nString%22%2C%22nList%22%3A%5B%22aaa%22%5D%7D%2C%22nList%22%3A%5B%22aaa%22%5D%7D
            response = testRestTemplate.getForEntity(
                    this.baseUrl + "/data?spareJson={1}&createDate={2}&spare2={3}", String.class,
                    ZKJsonUtils.toJsonStr(zkJson), "2019-08-15 14:58:46", "v_spare2");
            System.out.println("[^_^:20190815-1358-001]: " + response.getBody());
            TestCase.assertEquals(200, response.getStatusCode().value());
            String expectStr = null;
//            expectStr = "{\"createDate\":\"2019-08-15 14:58:46\",\"spare2\":\"v_spare2\",\"spareJson\":{\"nInt\":2,\"nArray\":[1,2],\"nString\":\"v_nString\",\"nZKJson\":{\"nInt\":2,\"nArray\":[1,2],\"nString\":\"v_nString\",\"nList\":[\"aaa\"]},\"nList\":[\"aaa\"]},\"delFlag\":0,\"version\":0}";
            zkBhe.setCreateDate(ZKDateUtils.parseDate("2019-08-15 14:58:46"));
            zkBhe.setSpare2("v_spare2");
            expectStr = ZKJsonUtils.toJsonStr(zkBhe);
            TestCase.assertEquals(expectStr, response.getBody());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
