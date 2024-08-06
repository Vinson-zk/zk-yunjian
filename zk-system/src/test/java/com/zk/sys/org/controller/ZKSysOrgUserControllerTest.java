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
* @Title: ZKSysOrgUserControllerTest.java 
* @author Vinson 
* @Package com.zk.sys.org.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 25, 2024 2:43:41 PM 
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
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog.ZKUserEditFlag;
import com.zk.sys.org.service.ZKSysOrgCompanyService;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.sec.controller.ZKSysSecIndexControllerTest;
import com.zk.sys.utils.ZKSysUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysOrgUserControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKSysSpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.sys,zk.sys.env" })
public class ZKSysOrgUserControllerTest {

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

    String baseUrl;

    String baseLoginUrl;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/%s/org/sysOrgUser", port, adminPath, modulePath,
                version);
        this.baseLoginUrl = String.format("http://127.0.0.1:%s/%s/%s/%s/sec/login", port, adminPath, modulePath,
                version);
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

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /********************************************************/
    /** 测试注册，手机、邮箱注册 ****/
    /********************************************************/
    @Test
    public void testRegisterPersonalUser() {
        String companyCode = ZKSysUtils.getOwnerCompanyCode();
        TestCase.assertFalse(ZKStringUtils.isEmpty(companyCode));
        ZKSysOrgCompany company = sysOrgCompanyService.getByCode(companyCode);
        TestCase.assertNotNull(company);

        String mail = "testmail@126.com";
        String phoneNum = "13825659080";
        String pwd = "test";

        ZKSysOrgUser user;

        try {
            ResponseEntity<String> response = null;

            String resStr = "";
            ZKMsgRes res = null;
            
            // 邮箱注册
            user = this.testRegisterPersonalUserByMail(company, mail, pwd);
            TestCase.assertNotNull(user);
            response = ZKSysSecIndexControllerTest.secLogin(testRestTemplate, this.baseLoginUrl, companyCode, mail,
                    pwd);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2059-001.01] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());

            // 手机注册
            user = this.testRegisterPersonalUserByPhoneNum(company, phoneNum, pwd);
            TestCase.assertNotNull(user);
            response = ZKSysSecIndexControllerTest.secLogin(testRestTemplate, this.baseLoginUrl, companyCode, phoneNum,
                    pwd);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2059-001.02] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, mail);
            if (user != null) {
                sysOrgUserService.diskDel(user);
            }
            user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, phoneNum);
            if (user != null) {
                sysOrgUserService.diskDel(user);
            }
        }
    }

    protected ZKSysOrgUser testRegisterPersonalUserByMail(ZKSysOrgCompany company, String mail, String pwd) {
        String url = "";
        ResponseEntity<String> response = null;
        HttpHeaders requestHeaders = null;
        HttpEntity<Object> requestEntity = null;

        String resStr = "";
        ZKMsgRes res = null;
        String tkId;
        ZKSysOrgUser user = null;
        /** 1 - 注册用户，发送验证码 *****************************************************/
        url = this.baseUrl + String.format("/n/sendRegisterMail/%s", company.getCode()); // n/sendRegisterMail/{companyCode}
        // headers
        requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        // body
        user = new ZKSysOrgUser();
        user.setMail(mail);
        user.setPassword(pwd);
        requestEntity = new HttpEntity<Object>(user, requestHeaders);
        response = testRestTemplate.postForEntity(url, requestEntity, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2359-001.01] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        TestCase.assertNotNull(tkId);
        
        /** 2 - 提交验证码并完成注册 *****************************************************/
        user = this.testRegisterSubmitVerifiyCode(tkId, "9527");
        return user;
    }

    protected ZKSysOrgUser testRegisterPersonalUserByPhoneNum(ZKSysOrgCompany company, String phoneNum, String pwd) {
        String url = "";
        ResponseEntity<String> response = null;
        HttpHeaders requestHeaders = null;
        HttpEntity<Object> requestEntity = null;

        String resStr = "";
        ZKMsgRes res = null;
        String tkId;
        ZKSysOrgUser user = null;
        /** 1 - 注册用户，发送验证码 *****************************************************/
        url = this.baseUrl + String.format("/n/sendRegisterPhone/%s", company.getCode()); // n/sendRegisterPhone/{companyCode}
        // headers
        requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        // body
        user = new ZKSysOrgUser();
        user.setPhoneNum(phoneNum);
        user.setPassword(pwd);
        requestEntity = new HttpEntity<Object>(user, requestHeaders);
        response = testRestTemplate.postForEntity(url, requestEntity, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2358-001.01] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        TestCase.assertNotNull(tkId);

        /** 2 - 提交验证码并完成注册 *****************************************************/
        user = this.testRegisterSubmitVerifiyCode(tkId, "9527");
        return user;
    }

    // 提交验证码
    protected ZKSysOrgUser testRegisterSubmitVerifiyCode(String tkId, String verifiyCode) {
        String url = "";
        ResponseEntity<String> response = null;
        HttpHeaders requestHeaders = null;
        HttpEntity<Object> requestEntity = null;

        String resStr = "";
        ZKMsgRes res = null;
        ZKSysOrgUser user = null;
        
        /** 2 - 提交验证码并完成注册 *****************************************************/
        url = this.baseUrl + "/n/submitVerifiyCode?verifiyCode=" + verifiyCode;
        // headers
        requestHeaders = new HttpHeaders();
        requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
        // body
        requestEntity = new HttpEntity<Object>(null, requestHeaders);
        response = testRestTemplate.postForEntity(url, requestEntity, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2159-001.01] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
        
        user = res.getDataByClass(ZKSysOrgUser.class);
        TestCase.assertNotNull(user);
        return user;
    }

    /********************************************************/
    /** 测试修改 邮箱；需要提前在 拥有者公司下创建 test 用户 ****/
    /********************************************************/
    @Test
    public void testChangeMail() {

        String companyCode = ZKSysUtils.getOwnerCompanyCode();
        TestCase.assertFalse(ZKStringUtils.isEmpty(companyCode));
        ZKSysOrgCompany company = sysOrgCompanyService.getByCode(companyCode);
        TestCase.assertNotNull(company);

        String account = "test";
        String pwd = "test";

        String mail = "testMail@126.com";
        ZKSysOrgUser user;

        try {

            // 先修改掉 用户的邮箱
            user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, account);
            TestCase.assertNotNull(user);
            sysOrgUserService.updateMail(user, "not-Mail", ZKUserEditFlag.Mail.company);

            String url = "";
            ResponseEntity<String> response = null;
            HttpHeaders requestHeaders = null;
            HttpEntity<Object> requestEntity = null;

            String tkId = "";
            String resStr = "";
            ZKMsgRes res = null;

            // 登录用户
            response = ZKSysSecIndexControllerTest.secLogin(testRestTemplate, this.baseLoginUrl, companyCode, account,
                    pwd);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2059-001.01] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);

            /** 1 - 发送验证邮件验证码 *****************************************************/
            url = this.baseUrl + "/cm/sendMailVerifiyCode?newMail=" + mail;
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
            // body
            requestEntity = new HttpEntity<Object>(null, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240729-2257-001.01] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());

            /** 2 - 提交验证码，并修改邮箱 *****************************************************/
            url = this.baseUrl + "/cm/submitVerifiyCode?verifiyCode=9527";
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
            // body
            requestEntity = new HttpEntity<Object>(null, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240729-2257-001.02] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());

            user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, account);
            TestCase.assertNotNull(user);
            TestCase.assertEquals(mail, user.getMail());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /********************************************************/
    /** 测试修改 手机; 需要提前在 拥有者公司下创建 test 用户 ****/
    /********************************************************/
    @Test
    public void testChangePhoneNum() {

        String companyCode = ZKSysUtils.getOwnerCompanyCode();
        TestCase.assertFalse(ZKStringUtils.isEmpty(companyCode));
        ZKSysOrgCompany company = sysOrgCompanyService.getByCode(companyCode);
        TestCase.assertNotNull(company);

        String account = "test";
        String pwd = "test";

        String phoneNum = "13825659081";

        ZKSysOrgUser user;

        try {
            // 先修改掉 用户的邮箱
            user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, account);
            TestCase.assertNotNull(user);
            sysOrgUserService.updatePhoneNum(user, "123", ZKUserEditFlag.Mail.company);

//            sysOrgUserService.updatePwd(user.getPkId(), pwd, ZKUserEditFlag.Pwd.company);

            String url = "";
            ResponseEntity<String> response = null;
            HttpHeaders requestHeaders = null;
            HttpEntity<Object> requestEntity = null;

            String tkId = "";
            String resStr = "";
            ZKMsgRes res = null;

            // 登录用户
            response = ZKSysSecIndexControllerTest.secLogin(testRestTemplate, this.baseLoginUrl, companyCode, account,
                    pwd);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2059-001.01] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);

            /** 1 - 发送验证邮件验证码 *****************************************************/
            url = this.baseUrl + "/cp/sendPhoneVerifiyCode?newPhoneNum=" + phoneNum;
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
            // body
            requestEntity = new HttpEntity<Object>(null, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240729-2257-001.01] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());

            /** 2 - 提交验证码，并修改邮箱 *****************************************************/
            url = this.baseUrl + "/cp/submitVerifiyCode?verifiyCode=9527";
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
            // body
            requestEntity = new HttpEntity<Object>(null, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240729-2257-001.02] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());


            user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, account);
            TestCase.assertNotNull(user);
            TestCase.assertEquals(phoneNum, user.getPhoneNum());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }


}
