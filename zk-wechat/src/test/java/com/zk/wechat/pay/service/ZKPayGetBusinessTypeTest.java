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
* @Title: ZKPayGetBusinessTypeTest.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 9:50:39 AM 
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
import com.zk.wechat.pay.entity.ZKPayGetBusinessType;

import junit.framework.TestCase;

/** 
* @ClassName: ZKPayGetBusinessTypeTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKPayGetBusinessTypeTest {

    @Test
    public void testDmlAndGetByCode() {
        ZKPayGetBusinessTypeService s = ZKWechatTestHelper.getCtx().getBean(ZKPayGetBusinessTypeService.class);
        TestCase.assertNotNull(s);
        List<ZKPayGetBusinessType> dels = new ArrayList<>();
        try {

            ZKPayGetBusinessType zkPayGetBusinessType = null;
            
            int intRes = 0;
            String code = null, name = null;
            String pkId = null;

            zkPayGetBusinessType = new ZKPayGetBusinessType();
            try {
                s.save(zkPayGetBusinessType);
                TestCase.assertTrue(false);
            }
            catch(ZKValidatorException e) {

            }
            
            code = "test-code";
            name = "test-name-名称";
            zkPayGetBusinessType = new ZKPayGetBusinessType();
            zkPayGetBusinessType.setCode(code);
            zkPayGetBusinessType.setName(new ZKJson());
            zkPayGetBusinessType.getName().put(ZKLocaleUtils.getLocale().toString(), name);
            zkPayGetBusinessType.setStatus(ZKPayGetBusinessType.Status.disabled);
            intRes = s.save(zkPayGetBusinessType);
            dels.add(zkPayGetBusinessType);
            pkId = zkPayGetBusinessType.getPkId();
            TestCase.assertEquals(1, intRes);

            // get
            zkPayGetBusinessType = s.get(zkPayGetBusinessType);
            System.out.println("[^_^:20210220-1022-001] zkPayGetBusinessType: "
                    + ZKJsonUtils.writeObjectJson(zkPayGetBusinessType));
            TestCase.assertEquals(ZKPayGetBusinessType.Status.disabled, zkPayGetBusinessType.getStatus().intValue());
            TestCase.assertEquals(code, zkPayGetBusinessType.getCode());
            TestCase.assertEquals(name, zkPayGetBusinessType.getName().get(ZKLocaleUtils.getLocale().toString()));

            // getByCode
            zkPayGetBusinessType = s.getByCode(zkPayGetBusinessType.getCode());
            TestCase.assertEquals(ZKPayGetBusinessType.Status.disabled, zkPayGetBusinessType.getStatus().intValue());
            TestCase.assertEquals(code, zkPayGetBusinessType.getCode());
            TestCase.assertEquals(name, zkPayGetBusinessType.getName().get(ZKLocaleUtils.getLocale().toString()));

            // 相同 code 保存
            zkPayGetBusinessType = new ZKPayGetBusinessType();
            zkPayGetBusinessType.setCode(code);
//            zkPayGetBusinessType.setName(new ZKJson());
//            zkPayGetBusinessType.getName().put(ZKLocaleUtils.getLocale().getDisplayName(), name);
//            zkPayGetBusinessType.setStatus(ZKPayGetBusinessType.Status.disabled);
            try {
                s.save(zkPayGetBusinessType);
                TestCase.assertTrue(false);
            }
            catch(ZKCodeException e) {
                TestCase.assertEquals("zk.wechat.000005", e.getCode());
            }

            // 删除
            zkPayGetBusinessType = new ZKPayGetBusinessType();
            zkPayGetBusinessType.setPkId(pkId);
            intRes = s.del(zkPayGetBusinessType);
            TestCase.assertEquals(1, intRes);
            zkPayGetBusinessType = s.get(zkPayGetBusinessType);
            TestCase.assertEquals(ZKPayGetBusinessType.Status.disabled, zkPayGetBusinessType.getStatus().intValue());
            TestCase.assertEquals(ZKPayGetBusinessType.DEL_FLAG.delete, zkPayGetBusinessType.getDelFlag().intValue());

            // 物理删除
            zkPayGetBusinessType = new ZKPayGetBusinessType();
            zkPayGetBusinessType.setPkId(pkId);
            intRes = s.diskDel(zkPayGetBusinessType);
            TestCase.assertEquals(1, intRes);
            zkPayGetBusinessType = new ZKPayGetBusinessType();
            zkPayGetBusinessType.setPkId(pkId);
            zkPayGetBusinessType = s.get(zkPayGetBusinessType);
            TestCase.assertNull(zkPayGetBusinessType);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }finally {
            dels.forEach(item->{
                s.diskDel(item);
            });
        }
    }

}
