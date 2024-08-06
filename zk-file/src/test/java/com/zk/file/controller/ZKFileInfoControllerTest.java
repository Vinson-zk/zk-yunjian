/** 
* Copyright (c) 2012-2023 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFileInfoControllerTest.java 
* @author Vinson 
* @Package com.zk.file.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 25, 2023 11:16:24 PM 
* @version V1.0 
*/
package com.zk.file.controller;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.file.helper.ZKFileTestSpringBootMainHelper;
import com.zk.framework.security.realm.ZKDistributedRealm;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;

import junit.framework.TestCase;

/**
 * @ClassName: ZKFileInfoControllerTest
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKFileTestSpringBootMainHelper.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.file" })
public class ZKFileInfoControllerTest {

//    @Value("${server.port}")
    @LocalServerPort
//    @org.springframework.boot.web.server.LocalServerPort
    private int port;

    @Value("${zk.path.admin}")
    private String adminPath;

    @Value("${zk.path.file}")
    private String modulePath;

    @Value("${zk.file.version}")
    private String version;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ZKSecSecurityManager securityManager;

    @Autowired
    private ZKDistributedRealm zkSecRealm;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/%s/fileInfo", port, adminPath, modulePath, version);
    }

    /**
     * @return baseUrl sa
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    public final static String sourceFileRootPath = "src/test/resources/testFile";

    public final static String uploadFileRootPath = "target/fileUpload";

    public final static String source = "source";

    public final static String download = "download";

    @Test
    public void testFileUpload() {
        try {
            String fileContent, fileName, url;
            File file;
            ZKMsgRes msgRes;

            // 请示头
            HttpHeaders headers = new HttpHeaders();
            // 请求体
            HttpEntity<MultiValueMap<String, Object>> requestEntity;
            // 响应体
            ResponseEntity<String> response;
            // 参数
            MultiValueMap<String, Object> params;

            url = this.getBaseUrl() + "/f/upload"; // 没有上级目录
            params = new LinkedMultiValueMap<String, Object>();

            params.set("companyCode", "test-companyCode");
//            params.set("saveGroupCode", "test-saveGroupCode");
//            params.set("parentCode", "test-dirCode");
//            params.set("securityType", "1");
//            params.set("actionScope", "2");

            // 创建一个令牌，满足测试权限校验
            ZKSecTicket tk = securityManager.getTicketManager()
                    .createSecTicket(securityManager.getTicketManager().generateTkId());
            ZKSecPrincipalCollection<String> pc = new ZKSecDefaultPrincipalCollection<String>();
            ZKSecPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>("-1", "test-user", "test-user",
                    ZKSecPrincipal.OS_TYPE.UNKNOWN, null, ZKSecPrincipal.APP_TYPE.web, null, "test-group-code", "-1",
                    "test-company-code");
            pc.add(zkSecRealm.getRealmName(), p);
            tk.setPrincipalCollection(pc);
            headers.add(ZKSecConstants.PARAM_NAME.TicketId, tk.getTkId().toString());
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

//            System.out.println("[^_^:20231226-2314-001]" + System.getProperty(ZKCoreConstants.System.userName));

            // 文件 1；
            fileContent = "遥襟甫畅，逸兴遄飞。爽籁发而清风生，纤歌凝而白云遏。";
            fileName = "uploadMultipart 文件1.txt";
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + source, fileName, true);
            ZKFileUtils.writeFile(fileContent.getBytes(), file, false);
            params.add("mfs", new FileSystemResource(file));

            // 文件 2；
            fileName = "test-file.png";
            file = new File(sourceFileRootPath + File.separator + fileName);
            params.add("mfs", new FileSystemResource(file));
            requestEntity = new HttpEntity<>(params, headers);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            System.out.println("[^_^:20231226-2313-001] response: " + response.getStatusCodeValue());
            System.out.println("[^_^:20231226-2313-001] response: " + response.getBody());
            TestCase.assertEquals(200, response.getStatusCodeValue());
            msgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals(true, msgRes.isOk());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testFileGet() {
        try {
            String filePkId, fileName, url;
            File file;

            // 请示头
            HttpHeaders headers = new HttpHeaders();
            // 请求体
            HttpEntity<MultiValueMap<String, Object>> requestEntity;
            // 响应体
            ResponseEntity<byte[]> response;

            // 创建一个令牌，满足测试权限校验
            ZKSecTicket tk = securityManager.getTicketManager()
                    .createSecTicket(securityManager.getTicketManager().generateTkId());
            ZKSecPrincipalCollection<String> pc = new ZKSecDefaultPrincipalCollection<String>();
            ZKSecPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>("-1", "test-user", "test-user",
                    ZKSecPrincipal.OS_TYPE.UNKNOWN, null, ZKSecPrincipal.APP_TYPE.web, null, "test-group-code", "-1",
                    "test-company-code");
            pc.add(zkSecRealm.getRealmName(), p);
            tk.setPrincipalCollection(pc);
            headers.add(ZKSecConstants.PARAM_NAME.TicketId, tk.getTkId().toString());
            requestEntity = new HttpEntity<>(headers);

            filePkId = "6977061496484266496";
            url = String.format(this.getBaseUrl() + "/f/getFile?pkId=%s&isDownload=%s", filePkId, true);
            response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);
//            response = testRestTemplate.getForEntity(url, byte[].class);
            TestCase.assertEquals(200, response.getStatusCodeValue());
            
            fileName = response.getHeaders().get("Content-Disposition").get(0);
            fileName = fileName.substring(fileName.indexOf(";fileName=") + 10);
            file = ZKFileUtils.createFile(uploadFileRootPath + File.separator + download, fileName, false);
            ZKFileUtils.writeFile(response.getBody(), file, false);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
