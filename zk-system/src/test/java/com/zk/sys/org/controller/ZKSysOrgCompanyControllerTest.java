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

import java.io.IOException;
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

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.sys.ZKSysSpringBootMain;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgCompanyOptService.TInfoKey;
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

    @Autowired
    ZKSecTicketManager ticketManager;

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

    String groupCode = "junit-test-rc-group-code";
    String companyCode = "junit-test-rc-company-code";

//    String groupCode = "testRcWaitAudit";
//    String companyCode = "testRcWaitAudit";

    // 测试公司注册的全流程
    @SuppressWarnings("unchecked")
    @Test
    public void testRegisterOverallProcess() {
        try {
            String ownerComapnyCode = ZKSysUtils.getOwnerCompanyCode();
            TestCase.assertFalse(ZKStringUtils.isEmpty(ownerComapnyCode));
            ZKSysOrgCompany parentCompany = sysOrgCompanyService.getByCode(ownerComapnyCode);
            TestCase.assertNotNull(parentCompany);

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
            String pwd = "admin";
            /** 1 - 集团代码已存在 *****************************************************/
            url = this.baseUrl + "/n/sendVerifyCode";
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
            company.setGroupCode(groupCode);
            company.setCode(companyCode);
            this.testDoingRegisterCompany(company, pwd, parentCompany, ZKSysSecConstants.KeyAuth.adminAccount, "admin");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            ZKSysOrgCompany company = sysOrgCompanyService.getByCode(companyCode);
            if (company != null) {
                sysOrgCompanyService.diskDel(company);
            }
        }
    }

    // 执行测试：公司注册发送验证码
    public String testDoingSendVerifyCode(ZKSysOrgCompany company) {

        try {
            String url;
            HttpHeaders requestHeaders;
            HttpEntity<Object> requestEntity;
            String resStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;
            String tkId;

            url = this.baseUrl + "/n/sendVerifyCode";
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            // body
            requestEntity = new HttpEntity<Object>(company, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2357-001] testDoingSendVerifyCode resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
            TestCase.assertNotNull(tkId);
            return tkId;
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
        return null;
    }

    // 执行测试：提交公司验证码
    public void testDoingSubmitVerifyCode(String tkId, ZKSysOrgCompany company, String pwd,
            ZKSysOrgCompany parentCompany) {
        try {
            String url;
            HttpHeaders requestHeaders;
            HttpEntity<Object> requestEntity;
            String resStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;

            ZKSysOrgUser user = null;
            ZKSysOrgRole role = null;
            ZKSysAuthCompany ac = null;
            List<ZKSysAuthCompany> haveAcs = null, getAcs = null;

            url = this.baseUrl + "/n/submitVerifyCode";
            // headers
            requestHeaders = new HttpHeaders();
            // requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);
            // body
            requestEntity = new HttpEntity<Object>(null, requestHeaders);
            response = testRestTemplate.postForEntity(url + "?mailVerifyCode=9527&phoneVerifyCode=9527&pwd=" + pwd,
                    requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2357-001] testDoingSubmitVerifyCode resStr: " + resStr);
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
            ac.setCompanyId(parentCompany.getPkId());
            ac.setOwnerType(ZKSysAuthCompany.KeyOwnerType.all);
            ac.setDefaultToChild(ZKSysAuthCompany.KeyDefaultToChild.transfer);
            haveAcs = sysAuthCompanyService.findList(ac);
            ac = new ZKSysAuthCompany();
            ac.setCompanyId(company.getPkId());
            getAcs = sysAuthCompanyService.findList(ac);
            System.out.println("[^_^:20240725-2357-001] testDoingSubmitVerifyCode haveAcs.size(): " + haveAcs.size());
            TestCase.assertEquals(haveAcs.size(), getAcs.size());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 执行测试：提交公司审核信息
    public void testDoingSubmitAuditInfo(String tkId, ZKSysOrgCompany company) {
        try {
            ZKSysResDict legalCertType = sysResDictService.getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCertType(),
                    "id.card");
            TestCase.assertNotNull(legalCertType);
            ZKSysResDict companyCertType = sysResDictService
                    .getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCompanyCertType(), "business.license");
            TestCase.assertNotNull(companyCertType);

            String url;
            HttpHeaders requestHeaders;
            HttpEntity<Object> requestEntity;
            String resStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;

            // 提交审核信息
            url = this.baseUrl + "/n/submitAuditInfo";
            String boundary = "7678sadfasdfaf9876ad7f8a";
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.set("Content-Type",
                    ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr() + "; boundary=" + boundary);
            requestHeaders.add(ZKSecConstants.PARAM_NAME.TicketId, tkId);

            // ---------------------------------
            // 公司信息参数: company
            company.setLegalPerson("test-法人");
            company.setLegalCertType(legalCertType.getPkId());
            company.setLegalCertNum("test-123321");
            company.setRegisterDate(ZKDateUtils.getToday());
            company.setCompanyCertType(companyCertType.getPkId());
            company.setCompanyCertNum("test-company-cert-123321");
            // company.setLogo("test-logo");
            // company.setLegalCertPhoto("test-legal-cert");
            // company.setCompanyCertPhoto("test-company-photo");

            StringBuffer strBuf = new StringBuffer();

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;").append("name=\"company\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JSON_UTF8.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append(ZKJsonUtils.toJsonStr(company));
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;").append("name=\"_p_logo\"; filename=\"_p_logo.png\"")
                    .append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("公司logo");
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append("name=\"_p_legalCertPhotoFront\"; filename=\"_p_legalCertPhotoFront.png\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("身份证正面(国徽面)");
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append("name=\"_p_legalCertPhotoBack\"; filename=\"_p_legalCertPhotoBack.png\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("身份证背面(人像面)");
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append("name=\"_p_companyCertPhoto\"; filename=\"_p_companyCertPhoto.png\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("公司证件照片");
            strBuf.append("\r\n");

            // 报文 结束
            strBuf.append("--").append(boundary).append("--").append("\r\n");

            requestEntity = new HttpEntity<Object>(strBuf.toString(), requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2357-001] testDoingSubmitAuditInfo resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 执行测试：审核公司
    public void testDoingAuditCompany(ZKSysOrgCompany company, ZKSysOrgCompany parentCompany, String parentAccount,
            String parentPwd) {
        try {
            ZKSysResDict legalCertType = sysResDictService.getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCertType(),
                    "id.card");
            TestCase.assertNotNull(legalCertType);
            ZKSysResDict companyCertType = sysResDictService
                    .getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCompanyCertType(), "business.license");
            TestCase.assertNotNull(companyCertType);

            String url;
            String resStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;
            String tkId;

            // 父公司先登录
            url = baseLoginUrl + "?" //
                    + ZKSecConstants.PARAM_NAME.CompanyCode + "=" + parentCompany.getCode() + "&" //
                    + ZKSecConstants.PARAM_NAME.Username + "=" + parentAccount + "&" //
                    + ZKSecConstants.PARAM_NAME.Pwd + "=" + parentPwd;
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2357-001] testDoingAuditCompany.01 resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
            TestCase.assertNotNull(tkId);
            // 审核
            url = this.baseUrl + String.format("/auditCompany/%s/%s", company.getPkId(), 0); // /auditCompany/{companyId}/{flag}
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2357-001] testDoingAuditCompany.02 resStr: " + resStr);
            TestCase.assertTrue(res.isOk());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 执行测试：正常注册
    protected void testDoingRegisterCompany(ZKSysOrgCompany company, String pwd, ZKSysOrgCompany parentCompany,
            String parentAccount, String parentPwd) throws IOException {
        try {
            String tkId;
            company.setCode(companyCode);
            tkId = this.testDoingSendVerifyCode(company);
            this.testDoingSubmitVerifyCode(tkId, company, pwd, parentCompany);

            // 使用登录，公司状态不正常来测试审核信息的提交；
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;

            /* zk.sec.000002 密码错误 */
            response = ZKSysSecIndexControllerTest.secLogin(testRestTemplate, this.baseLoginUrl, company.getCode(),
                    ZKSysSecConstants.KeyAuth.adminAccount, pwd);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2357-001] testDoingSubmitAuditInfo resStr: " + resStr);
            expectStr = "zk.sys.020005";
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(expectStr, res.getCode());
            tkId = response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
            company = res.getDataByClass(ZKSysOrgCompany.class);
            TestCase.assertEquals(ZKSysOrgCompany.KeyStatus.waitSubmit, company.getStatus().intValue());

            this.testDoingSubmitAuditInfo(tkId, company);
            this.testDoingAuditCompany(company, parentCompany, parentAccount, "admin");
            company = sysOrgCompanyService.getByCode(companyCode);
            TestCase.assertNotNull(company);
            TestCase.assertEquals(ZKSysOrgCompany.KeyStatus.auditPlatformIng, company.getStatus().intValue());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 单步测试公司注册，提交审核信息
    @Test
    public void testSubmitAuditInfo() {

        try {
            int status = -1;
            status = ZKSysOrgCompany.KeyStatus.auditPlatformIng; // 设置上级公司时，提交完审核资料后是待公司审核状态；不设置上级公司时，为待平台审核状态；
            int result;

            String mail = "binary_space@126.com";
            String phoneNum = "13825659082";

            /** 1 - 集团代码已存在 *****************************************************/
            // body
            ZKSysOrgCompany company = new ZKSysOrgCompany();
//            company.setParentId(parentCompany.getPkId());
            company.setGroupCode(companyCode);
            company.setCode(companyCode);
            company.setMail(mail);
            company.setPhoneNum(phoneNum);
            company.setName(ZKJson.parse("{\"zh-CN\":\"junit-测试公司\", \"en-US\":\"junit-test-comapny-register\"}"));
            company.setShortDesc(ZKJson
                    .parse("{\"zh-CN\":\"junit-测试公司-简介\", \"en-US\":\"junit-test-comapny-register-short desc\"}"));
            company.setStatus(ZKSysOrgCompany.KeyStatus.waitSubmit);
            result = sysOrgCompanyService.save(company);
            TestCase.assertEquals(1, result);

            ZKSecTicket tk = ticketManager.createSecTicket();
            tk.put(TInfoKey.tempCompany, company);
            this.testDoingSubmitAuditInfo(tk.getTkId().toString(), company);
            company = sysOrgCompanyService.getByCode(companyCode);
            TestCase.assertNotNull(company);
            TestCase.assertEquals(status, company.getStatus().intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            ZKSysOrgCompany company = sysOrgCompanyService.getByCode(companyCode);
            if (company != null) {
                sysOrgCompanyService.diskDel(company);
            }
        }
    }

    // 仅测试参数提交，报 zk.sec.000022 令牌过期
    @Test
    public void testSubmitAuditInfoOnlyParams() {
        try {
            ZKSysResDict legalCertType = sysResDictService.getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCertType(),
                    "id.card");
            TestCase.assertNotNull(legalCertType);
            ZKSysResDict companyCertType = sysResDictService
                    .getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCompanyCertType(), "business.license");
            TestCase.assertNotNull(companyCertType);

            String url = "";
            String resStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;
            HttpHeaders requestHeaders = null;
            HttpEntity<Object> requestEntity = null;

            ZKSysOrgCompany company;

            // 提交审核信息
            url = this.baseUrl + "/n/submitAuditInfo";
            String boundary = "7678sadfasdfaf9876ad7f8a";
            // headers
            requestHeaders = new HttpHeaders();
            requestHeaders.set("Content-Type",
                    ZKContentType.MULTIPART_FORM_DATA_UTF8.toContentTypeStr() + "; boundary=" + boundary);

            // body
            company = new ZKSysOrgCompany();
            company.setLegalPerson("test-法人");
            company.setLegalCertType(legalCertType.getPkId());
            company.setLegalCertNum("test-123321");
            company.setRegisterDate(ZKDateUtils.getToday());
            company.setCompanyCertType(companyCertType.getPkId());
            company.setCompanyCertNum("test-company-cert-123321");
            // company.setLogo("test-logo");
            // company.setLegalCertPhoto("test-legal-cert");
            // company.setCompanyCertPhoto("test-company-photo");

            // ---------------------------------
            StringBuffer strBuf = new StringBuffer();

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;").append("name=\"company\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JSON_UTF8.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append(ZKJsonUtils.toJsonStr(company));
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;").append("name=\"_p_logo\"; filename=\"_p_logo.png\"")
                    .append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("公司logo");
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append("name=\"_p_legalCertPhotoFront\"; filename=\"_p_legalCertPhotoFront.png\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("身份证正面(国徽面)");
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append("name=\"_p_legalCertPhotoBack\"; filename=\"_p_legalCertPhotoBack.png\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("身份证背面(人像面)");
            strBuf.append("\r\n");

            // 开始 boundary
            strBuf.append("--").append(boundary).append("\r\n");
            // 请求头
            strBuf.append("Content-Disposition: form-data;")
                    .append("name=\"_p_companyCertPhoto\"; filename=\"_p_companyCertPhoto.png\"").append("\r\n");
            strBuf.append("Content-Type:" + ZKContentType.JPEG.toContentTypeStr() + "\r\n");
            // 请求头结束
            strBuf.append("\r\n");
            // 参数值
            strBuf.append("公司证件照片");
            strBuf.append("\r\n");

            // 报文 结束
            strBuf.append("--").append(boundary).append("--").append("\r\n");

            requestEntity = new HttpEntity<Object>(strBuf.toString(), requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20240725-2357-001.05] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.sec.000022", res.getCode());
//            TestCase.assertTrue(res.isOk());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
