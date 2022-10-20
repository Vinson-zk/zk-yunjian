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
* @Title: ZKPayGroupServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 4:56:46 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.pay.entity.ZKPayGroup;

import junit.framework.TestCase;

/** 
* @ClassName: ZKPayGroupServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKPayGroupServiceTest {

    @Test
    public void testDmlAndGetByCode() {

        ZKPayGroupService s = ZKWechatTestHelper.getCtx().getBean(ZKPayGroupService.class);
        TestCase.assertNotNull(s);
        List<ZKPayGroup> dels = new ArrayList<>();
        try {

            ZKPayGroup payGroup = null;

            int intRes = 0;
            String code = null, name = null, wxMchid = null, wxAppId = null;
            String pkId = null;

            payGroup = new ZKPayGroup();
            try {
                s.save(payGroup);
                TestCase.assertTrue(false);
            }
            catch(ZKValidatorException e) {

            }

            code = "test-code";
            name = "test-name-名称";
            wxMchid = "test-wx-mchid";
            wxAppId = "test-wx-appid";
            payGroup = new ZKPayGroup();
            payGroup.setWxMchid(wxMchid);
            payGroup.setWxAppId(wxAppId);
            payGroup.setCode(code);
            payGroup.setName(new ZKJson());
            payGroup.getName().put(ZKLocaleUtils.getLocale().toString(), name);
            payGroup.setStatus(ZKPayGroup.Status.disabled);
            intRes = s.save(payGroup);
            dels.add(payGroup);
            pkId = payGroup.getPkId();
            TestCase.assertEquals(1, intRes);

            // get
            payGroup = s.get(payGroup);
            System.out.println("[^_^:20210222-1622-001] payGroup: " + ZKJsonUtils.writeObjectJson(payGroup));
            TestCase.assertEquals(ZKPayGroup.Status.disabled, payGroup.getStatus().intValue());
            TestCase.assertEquals(code, payGroup.getCode());
            TestCase.assertEquals(name, payGroup.getName().get(ZKLocaleUtils.getLocale().toString()));

            // getByCode
            payGroup = s.getByCode(payGroup.getCode());
            TestCase.assertEquals(ZKPayGroup.Status.disabled, payGroup.getStatus().intValue());
            TestCase.assertEquals(code, payGroup.getCode());
            TestCase.assertEquals(name, payGroup.getName().get(ZKLocaleUtils.getLocale().toString()));

            // 相同 code 保存
            payGroup = new ZKPayGroup();
            payGroup.setCode(code);
            try {
                s.save(payGroup);
                TestCase.assertTrue(false);
            }
            catch(ZKCodeException e) {
                TestCase.assertEquals("zk.wechat.000011", e.getCode());
            }

            // 删除
            payGroup = new ZKPayGroup();
            payGroup.setPkId(pkId);
            intRes = s.del(payGroup);
            TestCase.assertEquals(1, intRes);
            payGroup = s.get(payGroup);
            TestCase.assertEquals(ZKPayGroup.Status.disabled, payGroup.getStatus().intValue());
            TestCase.assertEquals(ZKPayGroup.DEL_FLAG.delete, payGroup.getDelFlag().intValue());

            // 物理删除
            payGroup = new ZKPayGroup();
            payGroup.setPkId(pkId);
            intRes = s.diskDel(payGroup);
            TestCase.assertEquals(1, intRes);
            payGroup = new ZKPayGroup();
            payGroup.setPkId(pkId);
            payGroup = s.get(payGroup);
            TestCase.assertNull(payGroup);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                s.diskDel(item);
            });
        }
    }

}
