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
* @Title: ZKSysNavControllerTest.java 
* @author Vinson 
* @Package com.zk.sys.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 21, 2020 10:08:09 AM 
* @version V1.0 
*/
package com.zk.sys.res.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKPage.Param_Name;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.sys.helper.ZKSysBaseControllerTest;
import com.zk.sys.res.entity.ZKSysNav;
import com.zk.sys.res.service.ZKSysNavService;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysNavControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SuppressWarnings("deprecation")
public class ZKSysNavControllerTest extends ZKSysBaseControllerTest {


    @Autowired
    private ZKSysNavService zkSysNavService;

    // 统一创建导航栏实体
    private ZKSysNav createZKSysNav() {
        ZKSysNav e = new ZKSysNav();

        e.putName("test-c-key-1", "test-c-name-1");
        e.setCode("test-c-nav-code");
        e.setFuncModuleCode("test-c-funcModuleCode");
        e.setFuncName("test-c-funcName");
        e.setPath("test-c-path");
        e.setIsShow(1);
        e.setIsIndex(0);
        e.setSort(1);

        return e;
    }

    // 在数据库创建指定个数的 导航栏
    private List<ZKSysNav> insertZKSysNav(int count) {
        List<ZKSysNav> resList = new ArrayList<>();
        ZKSysNav zkSysNav = null;
        for (int i = 0; i < count; ++i) {
            zkSysNav = this.createZKSysNav();
            zkSysNav.setCode(zkSysNav.getCode() + "-" + i);
            this.zkSysNavService.save(zkSysNav);
            resList.add(zkSysNav);
        }
        return resList;
    }

    @Override
    public String getBaseUrl() {
        return super.getBaseUrl() + "/res/nav";
    }

    /** 测试 新增/修改 */
    @Test
    public void testEditSysNav() {
        List<ZKSysNav> dels = new ArrayList<>();
        try {
            HttpEntity<?> requestEntity;
            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            ZKSysNav zkSysNav = null, getZkSysNav = null;

            // 设置请求数据格式 通过请求头 headers 设置
            HttpHeaders requestHeaders = new HttpHeaders();
            // 设置请求数据格式为 "application/json;charset=UTF-8";
            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            requestHeaders.add("locale", "zh_CN");

            /*** "zk.000002" 数据验证失败, [] ***/
//            Map<String, Object> paramMap = new HashMap<String, Object>();

            zkSysNav = new ZKSysNav();
            zkSysNav.putName("test-key-name", "name-1");
            requestEntity = new HttpEntity<>(zkSysNav, requestHeaders);
            response = this.getTestRestTemplate().postForEntity(this.getBaseUrl() + "/sysNav", requestEntity,
                    String.class);
            TestCase.assertEquals(200, response.getStatusCodeValue());
            System.out.println("[^_^:20200821-1027-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.000002", zkMsgRes.getCode());
            TestCase.assertEquals("数据验证失败", zkMsgRes.getMsg());

            /*** 新增 ***/
            zkSysNav = this.createZKSysNav();
            requestEntity = new HttpEntity<>(zkSysNav, requestHeaders);
            response = this.getTestRestTemplate().postForEntity(this.getBaseUrl() + "/sysNav", requestEntity,
                    String.class);
            System.out.println("[^_^:20200821-1027-002] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            zkSysNav = zkMsgRes.getDataByClass(ZKSysNav.class);
            dels.add(zkSysNav);
            getZkSysNav = this.zkSysNavService.get(zkSysNav);
            TestCase.assertNotNull(getZkSysNav);
            TestCase.assertEquals("test-c-name-1", getZkSysNav.getName().get("test-c-key-1"));

            /*** 修改 ***/
            zkSysNav.putName("test-c-key-1", "test-c-name-1-update");
            zkSysNav.putName("test-c-key-2", "test-c-name-2");
            requestEntity = new HttpEntity<>(zkSysNav, requestHeaders);
            response = this.getTestRestTemplate().postForEntity(this.getBaseUrl() + "/sysNav", requestEntity,
                    String.class);
            System.out.println("[^_^:20200821-1027-002] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            getZkSysNav = this.zkSysNavService.get(zkSysNav);
            TestCase.assertNotNull(getZkSysNav);
            TestCase.assertEquals("test-c-name-1-update", getZkSysNav.getName().get("test-c-key-1"));
            TestCase.assertEquals("test-c-name-2", getZkSysNav.getName().get("test-c-key-2"));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                this.zkSysNavService.diskDel(item);
            });
        }
    }

    /** 测试 删除 */
    @Test
    public void testDeleteSysNav() {
        List<ZKSysNav> dels = new ArrayList<>();
        try {
            dels = this.insertZKSysNav(3);

            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            StringBuffer paramsBuffer = new StringBuffer();

            if(dels.isEmpty()) {
                // 没有测试删除的数据
                TestCase.assertTrue(false);
            }
            
            /*** 删除 1 个 ***/
            paramsBuffer = new StringBuffer();
            paramsBuffer.append("pkId[]=");
            paramsBuffer.append(dels.get(0).getPkId());
            response = this.getTestRestTemplate().exchange(
                    String.format("%s/sysNav?%s", this.getBaseUrl(), paramsBuffer.toString()), HttpMethod.DELETE, null,
                    String.class);
            System.out.println("[^_^:20200821-1347-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals(1, (int) zkMsgRes.getData());

            /*** 删除 2 个 ***/
            paramsBuffer = new StringBuffer();
            paramsBuffer.append("pkId[]=");
            paramsBuffer.append(dels.get(1).getPkId());
            paramsBuffer.append("&pkId[]=");
            paramsBuffer.append(dels.get(2).getPkId());
            response = this.getTestRestTemplate().exchange(
                    String.format("%s/sysNav?%s", this.getBaseUrl(), paramsBuffer.toString()), HttpMethod.DELETE, null,
                    String.class);
            System.out.println("[^_^:20200821-1347-002] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals(2, (int) zkMsgRes.getData());

            for (ZKSysNav obj : dels) {
                obj = this.zkSysNavService.get(obj);
                TestCase.assertNotNull(obj);
                TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, obj.getDelFlag().intValue());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                this.zkSysNavService.diskDel(item);
            });
        }
    }

    /** 测试 相询/详情/分页查询 */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSysNav() {
        List<ZKSysNav> dels = new ArrayList<>();
        try {
            dels = this.insertZKSysNav(3);

            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            StringBuffer paramsBuffer = new StringBuffer();
            ZKSysNav zkSysNav = null;
            ZKPage<ZKSysNav> resPage;

            if (dels.isEmpty()) {
                // 没有测试删除的数据
                TestCase.assertTrue(false);
            }

            /*** 查询详情 ***/
            paramsBuffer = new StringBuffer();
            paramsBuffer.append("pkId=");
            paramsBuffer.append(dels.get(1).getPkId());
            response = this.getTestRestTemplate().getForEntity(String.format("%s/sysNav?%s", this.getBaseUrl(), paramsBuffer.toString()), String.class);
            System.out.println("[^_^:20200821-1517-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            zkSysNav = zkMsgRes.getDataByClass(ZKSysNav.class);
            TestCase.assertEquals(zkSysNav.getPkId(), dels.get(1).getPkId());
            TestCase.assertEquals(zkSysNav.getCode(), dels.get(1).getCode());

            /*** 分页查询 ***/
            paramsBuffer = new StringBuffer();
            paramsBuffer.append(Param_Name.no);
            paramsBuffer.append("=2");
            paramsBuffer.append("&");
            paramsBuffer.append(Param_Name.size);
            paramsBuffer.append("=2");
            paramsBuffer.append("&");
            paramsBuffer.append("name=");
            paramsBuffer.append(ZKEncodingUtils.urlEncode("{\"test-c-key-1\":\"test-c\"}"));
            response = this.getTestRestTemplate().getForEntity(
                    String.format("%s/sysNavs?%s", this.getBaseUrl(), paramsBuffer.toString()),
                    String.class);
            System.out.println("[^_^:20200821-1517-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            resPage = zkMsgRes.getDataByClass(ZKPage.class);
            TestCase.assertEquals(2, resPage.getPageNo());
            TestCase.assertEquals(2, resPage.getPageSize());
            TestCase.assertEquals(3, resPage.getTotalCount());

            /*** 分页查询 根据 ZKJson 中的 key 匹配结果为 0 ***/
            paramsBuffer = new StringBuffer();
            paramsBuffer.append(Param_Name.no);
            paramsBuffer.append("=0");
            paramsBuffer.append("&");
            paramsBuffer.append(Param_Name.size);
            paramsBuffer.append("=22");
            paramsBuffer.append("&");
            paramsBuffer.append("name=");
            paramsBuffer.append(ZKEncodingUtils.urlEncode("{\"test-c-key-1\":\"test-not-c\"}"));
            response = this.getTestRestTemplate().getForEntity(
                    String.format("%s/sysNavs?%s", this.getBaseUrl(), paramsBuffer.toString()), String.class);
            System.out.println("[^_^:20200821-1517-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            resPage = zkMsgRes.getDataByClass(ZKPage.class);
            TestCase.assertEquals(0, resPage.getPageNo());
            TestCase.assertEquals(22, resPage.getPageSize());
            TestCase.assertEquals(0, resPage.getTotalCount());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                this.zkSysNavService.diskDel(item);
            });
        }
    }

}
