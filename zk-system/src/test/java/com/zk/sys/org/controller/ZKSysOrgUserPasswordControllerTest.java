/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysOrgUserPasswordControllerTest.java 
* @author Vinson 
* @Package com.zk.sys.org.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 25, 2024 2:43:54 PM 
* @version V1.0 
*/
package com.zk.sys.org.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.sys.ZKSysSpringBootMain;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserOptLog.ZKUserOptTypeFlag;
import com.zk.sys.org.service.ZKSysOrgCompanyService;
import com.zk.sys.org.service.ZKSysOrgRoleService;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.utils.ZKSysUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysOrgUserPasswordControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKSysSpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.sys,zk.sys.env" })
public class ZKSysOrgUserPasswordControllerTest {

    @LocalServerPort
    private int port;

    @Value("${zk.path.admin}")
    private String adminPath;

    @Value("${zk.path.sys}")
    private String modulePath;

    @Value("${zk.sys.version}")
    private String version;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    protected ZKSysOrgUserService sysOrgUserService;

    @Autowired
    protected ZKSysOrgRoleService sysOrgRoleService;

    @Autowired
    protected ZKSysAuthCompanyService sysAuthCompanyService;

    String baseUrl;

//    String baseLoginUrl;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/%s/org/upwd", port, adminPath, modulePath,
                version);
//        this.baseLoginUrl = String.format("http://127.0.0.1:%s/%s/%s/%s/sec/login", port, adminPath, modulePath,
//                version);
    }

    /**
     * @return baseUrl sa
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    @Test
    public void test() {
        try {
            TestCase.assertNotNull(this.testRestTemplate);
            TestCase.assertNotNull(this.sysOrgCompanyService);
            TestCase.assertNotNull(this.sysOrgUserService);
            TestCase.assertNotNull(this.sysOrgRoleService);
            TestCase.assertNotNull(this.sysAuthCompanyService);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /********************************************************/
    /** 测试 找回密码，手机，邮箱 ****/
    /********************************************************/
    @Test
    public void testForgotPwd() {
        String companyCode = ZKSysUtils.getOwnerCompanyCode();
        TestCase.assertFalse(ZKStringUtils.isEmpty(companyCode));
        ZKSysOrgCompany company = sysOrgCompanyService.getByCode(companyCode);
        TestCase.assertNotNull(company);

        String mail = "testUser@126.com";
        String phoneNum = "13825658080";
        String newPassword = "test";

        ZKSysOrgUser user;

        try {
            // 先修改掉 用户的密码
            user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, mail);
            TestCase.assertNotNull(user);
            sysOrgUserService.updatePwd(user.getPkId(), "not-pwd", ZKUserOptTypeFlag.Mail.company, null);

            // 邮箱找回
            this.testForgotPwdByMail(company, mail, newPassword);
            user = sysOrgUserService.get(user.getPkId());
            sysOrgUserService.checkUserPassword(user, newPassword);

            // 手机找回
            this.testForgotPwdByPhoneNum(company, phoneNum, newPassword);
            user = sysOrgUserService.get(user.getPkId());
            sysOrgUserService.checkUserPassword(user, newPassword);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
    
    protected void testForgotPwdByMail(ZKSysOrgCompany company, String account, String newPassword) {
        String url = "";
        ResponseEntity<String> response = null;

        String resStr = "";
        ZKMsgRes res = null;
        String tkId;
        /** 1 - 注册用户，发送验证码 *****************************************************/
        url = this.baseUrl
                + String.format("/n/fp/sendMailVerifyCode?companyCode=%s&mail=%s", company.getCode(), account);
        // body
        response = testRestTemplate.postForEntity(url, null, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2330-001.01] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        TestCase.assertNotNull(tkId);

        /** 2 - 提交验证码并完成注册 *****************************************************/
        this.testForgotPwdSubmitVerifyCode(tkId, "9527", newPassword);
    }

    protected void testForgotPwdByPhoneNum(ZKSysOrgCompany company, String phoneNum, String newPassword) {
        String url = "";
        ResponseEntity<String> response = null;

        String resStr = "";
        ZKMsgRes res = null;
        String tkId;
        /** 1 - 注册用户，发送验证码 *****************************************************/
        url = this.baseUrl
                + String.format("/n/fp/sendPhoneVerifyCode?companyCode=%s&phoneNum=%s", company.getCode(), phoneNum);
        // body
        response = testRestTemplate.postForEntity(url, null, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2331-001.01] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        TestCase.assertNotNull(tkId);

        /** 2 - 提交验证码并完成注册 *****************************************************/
        this.testForgotPwdSubmitVerifyCode(tkId, "9527", newPassword);
    }

    // 提交验证码
    protected void testForgotPwdSubmitVerifyCode(String tkId, String verifyCode, String newPassword) {
        String url = "";
        ResponseEntity<String> response = null;
        HttpHeaders requestHeaders = null;
        HttpEntity<Object> requestEntity = null;

        String resStr = "";
        ZKMsgRes res = null;

        /** 2 - 提交验证码并完成修改 *****************************************************/
        url = this.baseUrl + "/n/fp/submitVerifyCode?verifyCode=" + verifyCode + "&newPassword=" + newPassword;
        // headers
        requestHeaders = new HttpHeaders();
        requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
        // body
        requestEntity = new HttpEntity<Object>(null, requestHeaders);
        response = testRestTemplate.postForEntity(url, requestEntity, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2333-001.01] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
    }

}
