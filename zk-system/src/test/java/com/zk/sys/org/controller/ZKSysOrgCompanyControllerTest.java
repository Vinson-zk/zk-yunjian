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
* @Title: ZKSysOrgCompanyControllerTest.java 
* @author Vinson 
* @Package com.zk.sys.org.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 25, 2024 2:43:26 PM 
* @version V1.0 
*/
package com.zk.sys.org.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.sys.ZKSysSpringBootMain;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgCompanyService;
import com.zk.sys.org.service.ZKSysOrgRoleService;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.res.entity.ZKSysResDict;
import com.zk.sys.res.service.ZKSysResDictService;
import com.zk.sys.sec.common.ZKSysSecConstants;
import com.zk.sys.sec.controller.ZKSysSecIndexControllerTest;
import com.zk.sys.utils.ZKSysUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysOrgCompanyControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKSysSpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.sys,zk.sys.env" })
public class ZKSysOrgCompanyControllerTest {

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

    @Autowired
    ZKSysResDictService sysResDictService;

    String baseUrl;

    String baseLoginUrl;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/%s/org/sysOrgCompany", port, adminPath, modulePath,
                version);
        this.baseLoginUrl = String.format("http://127.0.0.1:%s/%s/%s/%s/sec/login", port, adminPath, modulePath,
                version);
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

    @SuppressWarnings("unchecked")
    @Test
    public void testRegister() {
        String comapnyCode = "testCode";
        try {
            String ownerComapnyCode = ZKSysUtils.getOwnerCompanyCode();
            TestCase.assertFalse(ZKStringUtils.isEmpty(ownerComapnyCode));
            ZKSysOrgCompany ownerCompany = sysOrgCompanyService.getByCode(ownerComapnyCode);
            TestCase.assertNotNull(ownerCompany);

            String url = "";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;
            HttpHeaders requestHeaders = null;
//            Map<String, Object> params;
            HttpEntity<Object> requestEntity = null;

            ZKSysOrgCompany company = null;

            Map<String, String> resMap;
            String mail = "binary_space@126.com";
            String phoneNum = "13825659082";
            String pwd = "123456";
            /** 1 - 集团代码已存在 *****************************************************/
            url = this.baseUrl + "/n/sendVerifiyCode";
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            // body
            company = new ZKSysOrgCompany();
            company.setGroupCode(ownerComapnyCode);
            company.setCode(ownerComapnyCode);
            company.setMail(mail);
            company.setPhoneNum(phoneNum);
            company.setName(ZKJson.parse("{\"zh-CN\":\"测试公司\", \"en-US\":\"test-comapny\"}"));
            company.setShortDesc(ZKJson.parse("{\"zh-CN\":\"测试公司-简介\", \"en-US\":\"test-comapny-short desc\"}"));
            requestEntity = new HttpEntity<Object>(company, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2336-001.01] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            expectStr = "zk.000002";
            TestCase.assertEquals(expectStr, res.getCode());
            resMap = res.getDataByClass(Map.class);
            TestCase.assertEquals("groupCode", resMap.entrySet().iterator().next().getKey());

            /** 2 - 公司代码已存在 *****************************************************/
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            // body
            company.setGroupCode("test-groupCode");
            requestEntity = new HttpEntity<Object>(company, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2336-001.02] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            expectStr = "zk.000002";
            TestCase.assertEquals(expectStr, res.getCode());
            resMap = res.getDataByClass(Map.class);
            TestCase.assertEquals("code", resMap.entrySet().iterator().next().getKey());

            /** 3 - 正常注册并完成初步审核 *****************************************************/
            company.setCode(comapnyCode);
            this.testRegisterCompany(company, pwd, ownerComapnyCode, ZKSysSecConstants.KeyAuth.adminAccount, "admin");
            company = sysOrgCompanyService.getByCode(comapnyCode);
            TestCase.assertNotNull(company);
            TestCase.assertEquals(ZKSysOrgCompany.KeyStatus.auditPlatformIng, company.getStatus().intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            ZKSysOrgCompany company = sysOrgCompanyService.getByCode(comapnyCode);
            if (company != null) {
                sysOrgCompanyService.diskDel(company);
            }
        }
    }

    protected void testRegisterCompany(ZKSysOrgCompany company, String pwd, String parentCompanyCode,
            String parentAccount, String parentPwd) {

        ZKSysOrgCompany ownerCompany = sysOrgCompanyService.getByCode(parentCompanyCode);
        TestCase.assertNotNull(ownerCompany);

        ZKSysResDict legalCertType = sysResDictService.getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCertType(),
                "id.card");
        TestCase.assertNotNull(legalCertType);
        ZKSysResDict companyCertType = sysResDictService
                .getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCompanyCertType(), "business.license");
        TestCase.assertNotNull(companyCertType);

        String url = "";
        String resStr = "", expectStr = "";
        ResponseEntity<String> response = null;
        ZKMsgRes res = null;
        HttpHeaders requestHeaders = null;
        HttpEntity<Object> requestEntity = null;

        ZKSysOrgUser user = null;
        ZKSysOrgRole role = null;
        ZKSysAuthCompany ac = null;
        List<ZKSysAuthCompany> haveAcs = null, getAcs = null;

        String tkId;

        /** 1 - 平台公司，成功注册，发送验证码 *****************************************************/
        url = this.baseUrl + "/n/sendVerifiyCode";
        // headers
        requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        // body
        requestEntity = new HttpEntity<Object>(company, requestHeaders);
        response = testRestTemplate.postForEntity(url, requestEntity, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2357-001.01] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        TestCase.assertNotNull(tkId);

        /** 2 - 提交验证码 *****************************************************/
        url = this.baseUrl + "/n/submitVerifiyCode";
        // headers
        requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
        // body
        requestEntity = new HttpEntity<Object>(null, requestHeaders);
        response = testRestTemplate.postForEntity(url + "?mailVerifiyCode=9527&phoneVerifiyCode=9527&pwd=" + pwd,
                requestEntity, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2357-001.02] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        TestCase.assertNotNull(tkId);
        company = res.getDataByClass(ZKSysOrgCompany.class);
        // 检查 admin 用户是否初始化
        user = sysOrgUserService.getByAccount(company.getPkId(), ZKSysSecConstants.KeyAuth.adminAccount);
        TestCase.assertNotNull(user);
        // 检查 superAdmin 角色是否初始化
        role = sysOrgRoleService.getByCode(company.getPkId(), ZKSysSecConstants.KeyAuth.adminRoleCode);
        TestCase.assertNotNull(role);
        // 检查权限是否默认分配
        ac = new ZKSysAuthCompany();
        ac.setCompanyId(ownerCompany.getPkId());
        ac.setOwnerType(ZKSysAuthCompany.KeyOwnerType.all);
        ac.setDefaultToChild(ZKSysAuthCompany.KeyDefaultToChild.transfer);
        haveAcs = sysAuthCompanyService.findList(ac);
        ac = new ZKSysAuthCompany();
        ac.setCompanyId(company.getPkId());
        getAcs = sysAuthCompanyService.findList(ac);
        System.out.println("[^_^:20240725-2357-001.03] haveAcs.size(): " + haveAcs.size());
        TestCase.assertEquals(haveAcs.size(), getAcs.size());

        /** 3 - 提交审核信息 *****************************************************/
        // 使用登录，公司状态不正常来测试审核信息的提交；
        /* zk.sec.000002 密码错误 */
        response = ZKSysSecIndexControllerTest.secLogin(testRestTemplate, this.baseLoginUrl, company.getCode(),
                ZKSysSecConstants.KeyAuth.adminAccount, pwd);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2357-001.04] resStr: " + resStr);
        expectStr = "zk.sys.020005";
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertEquals(expectStr, res.getCode());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        company = res.getDataByClass(ZKSysOrgCompany.class);
        TestCase.assertEquals(ZKSysOrgCompany.KeyStatus.waitSubmit, company.getStatus().intValue());
        // 提交审核信息
        url = this.baseUrl + "/n/submitAuditInfo";
        // headers
        requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
        // body
        company.setLogo("test-logo");
        company.setLegalPerson("test-法人");
        company.setLegalCertType(legalCertType.getPkId());
        company.setLegalCertNum("test-123321");
        company.setLegalCertPhoto("test-legal-cert");
        company.setRegisterDate(ZKDateUtils.getToday());
        company.setCompanyCertType(companyCertType.getPkId());
        company.setCompanyCertNum("test-company-cert-123321");
        company.setCompanyCertPhoto("test-company-photo");
        requestEntity = new HttpEntity<Object>(company, requestHeaders);
        response = testRestTemplate.postForEntity(url, requestEntity, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2357-001.05] resStr: " + resStr);
        res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertTrue(res.isOk());

        /** 4 - 审核公司 *****************************************************/
        url = this.baseUrl + "/n/submitAuditInfo";
        // 父公司先登录
        url = baseLoginUrl + "?" //
                + ZKSecConstants.PARAM_NAME.CompanyCode + "=" + parentCompanyCode + "&" //
                + ZKSecConstants.PARAM_NAME.Username + "=" + parentAccount + "&" //
                + ZKSecConstants.PARAM_NAME.Pwd + "=" + parentPwd;
        response = testRestTemplate.postForEntity(url, null, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2357-001.06] resStr: " + resStr);
        TestCase.assertTrue(res.isOk());
        tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
        TestCase.assertNotNull(tkId);
        // 审核
        url = this.baseUrl + String.format("/auditCompany/%s/%s", company.getPkId(), 0); // /auditCompany/{companyId}/{flag}
        response = testRestTemplate.postForEntity(url, null, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20240725-2357-001.07] resStr: " + resStr);
        TestCase.assertTrue(res.isOk());
    }

}
