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
* @Title: ZKSysResFuncApiReqChannelServiceTest.java 
* @author Vinson 
* @Package com.zk.sys.res.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 30, 2021 4:24:56 PM 
* @version V1.0 
*/
package com.zk.sys.res.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.res.entity.ZKSysResFuncApi;
import com.zk.sys.res.entity.ZKSysResFuncApiReqChannel;
import com.zk.sys.res.entity.ZKSysResRequestChannel;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysResFuncApiReqChannelServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysResFuncApiReqChannelServiceTest {

    @Test
    public void testDml() {
        ZKSysResFuncApiReqChannelService s = ZKSysTestHelper.getMainCtx()
                .getBean(ZKSysResFuncApiReqChannelService.class);
        List<ZKSysResFuncApiReqChannel> dels = new ArrayList<>();
        try {
            ZKSysResFuncApiReqChannel e = null;
            int result = 0;

            /*** 保存 ***/
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId("1");
            e.setFuncApiId("1");
            e.setChannelCode("t-channelCode");
            e.setSystemCode("t-systemCode");
            e.setFuncApiCode("t-funcApiCode");
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** 修改 ***/
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);

            /*** 查询 ***/
            e = s.get(e);
            TestCase.assertNotNull(e);

            /*** 删除 ***/
            result = 0;
            result = s.del(e);
            TestCase.assertEquals(1, result);
            e = s.get(e);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, e.getDelFlag().intValue());

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

    // 测试根据渠道删除
    @Test
    public void testDelByChannel() {
        ZKSysResFuncApiReqChannelService s = ZKSysTestHelper.getMainCtx()
                .getBean(ZKSysResFuncApiReqChannelService.class);
        List<ZKSysResFuncApiReqChannel> dels = new ArrayList<>();
        try {
            ZKSysResFuncApiReqChannel e = null;
            int result = 0;
            List<ZKSysResFuncApiReqChannel> resList = null;
            String channelId = "1";

            /*** 保存 ***/
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channelId);
            e.setFuncApiId("1");
            e.setChannelCode("t-channelCode");
            e.setSystemCode("t-systemCode");
            e.setFuncApiCode("t-funcApiCode");
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channelId);
            e.setFuncApiId("2");
            e.setChannelCode("t-channelCode");
            e.setSystemCode("t-systemCode");
            e.setFuncApiCode("t-funcApiCode-0");
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            // 查询
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channelId);
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());

//            // 逻辑删除
//            result = s.delByChannelId(channelId);
//            TestCase.assertEquals(2, result);

            // 逻辑删除结果校验
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channelId);
            resList = s.findList(e);
            TestCase.assertEquals(0, resList.size());
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channelId);
            e.setDelFlag(ZKBaseEntity.DEL_FLAG.delete);
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());

            // 物理删除
            result = s.diskDelByChannelId(channelId);
            TestCase.assertEquals(2, result);

            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channelId);
            resList = s.findList(e);
            TestCase.assertEquals(0, resList.size());

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

    // 测试根据接口删除
    @Test
    public void testDelByApi() {
        ZKSysResFuncApiReqChannelService s = ZKSysTestHelper.getMainCtx()
                .getBean(ZKSysResFuncApiReqChannelService.class);
        List<ZKSysResFuncApiReqChannel> dels = new ArrayList<>();
        try {
            ZKSysResFuncApiReqChannel e = null;
            int result = 0;
            List<ZKSysResFuncApiReqChannel> resList = null;
            String apiId = "1";

            /*** 保存 ***/
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId("1");
            e.setFuncApiId(apiId);
            e.setChannelCode("t-channelCode");
            e.setSystemCode("t-systemCode");
            e.setFuncApiCode("t-funcApiCode");
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId("2");
            e.setFuncApiId(apiId);
            e.setChannelCode("t-channelCode");
            e.setSystemCode("t-systemCode");
            e.setFuncApiCode("t-funcApiCode-0");
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            // 查询
            e = new ZKSysResFuncApiReqChannel();
            e.setFuncApiId(apiId);
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());

//            // 逻辑删除
//            result = s.delByApiId(apiId);
//            TestCase.assertEquals(2, result);

            // 逻辑删除结果校验
            e = new ZKSysResFuncApiReqChannel();
            e.setFuncApiId(apiId);
            resList = s.findList(e);
            TestCase.assertEquals(0, resList.size());
            e = new ZKSysResFuncApiReqChannel();
            e.setFuncApiId(apiId);
            e.setDelFlag(ZKBaseEntity.DEL_FLAG.delete);
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());

            // 物理删除
            result = s.diskDelByApiId(apiId);
            TestCase.assertEquals(2, result);

            e = new ZKSysResFuncApiReqChannel();
            e.setFuncApiId(apiId);
            resList = s.findList(e);
            TestCase.assertEquals(0, resList.size());

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

    // 给 请求渠道分配 API 接口
    @Test
    public void testAddByChannel() {
        ZKSysResFuncApiReqChannelService s = ZKSysTestHelper.getMainCtx()
                .getBean(ZKSysResFuncApiReqChannelService.class);
        List<ZKSysResFuncApiReqChannel> dels = new ArrayList<>();
        try {
            ZKSysResFuncApiReqChannel e = null;
            int result = 0;
            List<ZKSysResFuncApiReqChannel> resList = null;
            ZKSysResRequestChannel channel = new ZKSysResRequestChannel();
            ZKSysResFuncApi api = null;
            List<ZKSysResFuncApi> addApis = new ArrayList<>();
            List<ZKSysResFuncApi> delApis = new ArrayList<>();

            channel.setPkId("11");
            channel.setCode("t-code");

            api = new ZKSysResFuncApi();
            api.setPkId("21");
            api.setSystemCode("t-systemCode");
            api.setCode("t-code=1");
            addApis.add(api);
            api = new ZKSysResFuncApi();
            api.setPkId("22");
            api.setSystemCode("t-systemCode");
            api.setCode("t-code=2");
            addApis.add(api);

            // 分配两个差校验
            dels.addAll(s.addByChannel(channel, addApis, delApis));
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channel.getPkId());
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());

            // 删除第二个，重新分配一个
            addApis = new ArrayList<>();
            delApis.add(api);
            api = new ZKSysResFuncApi();
            api.setPkId("23");
            api.setSystemCode("t-systemCode");
            api.setCode("t-code=3");
            addApis.add(api);
            dels.addAll(s.addByChannel(channel, addApis, delApis));
            e = new ZKSysResFuncApiReqChannel();
            e.setChannelId(channel.getPkId());
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());
            result = 0;
            for (ZKSysResFuncApiReqChannel item : resList) {
                TestCase.assertEquals(channel.getPkId(), item.getChannelId());
                if ("21".equals(item.getFuncApiId())) {
                    result = result | 1;
                }
                if ("22".equals(item.getFuncApiId())) {
                    result = result | 2;
                }
                if ("23".equals(item.getFuncApiId())) {
                    result = result | 4;
                }
            }
            TestCase.assertEquals(5, result);

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

    // 给 API 接口 分配 请求渠道
    @Test
    public void testAddByFuncApi() {
        ZKSysResFuncApiReqChannelService s = ZKSysTestHelper.getMainCtx()
                .getBean(ZKSysResFuncApiReqChannelService.class);
        List<ZKSysResFuncApiReqChannel> dels = new ArrayList<>();
        try {
            ZKSysResFuncApiReqChannel e = null;
            int result = 0;
            List<ZKSysResFuncApiReqChannel> resList = null;
            ZKSysResRequestChannel channel = new ZKSysResRequestChannel();
            ZKSysResFuncApi api = new ZKSysResFuncApi();
            List<ZKSysResRequestChannel> addChannels = new ArrayList<>();
            List<ZKSysResRequestChannel> delChannels = new ArrayList<>();

            api.setPkId("21");
            api.setSystemCode("t-systemCode");
            api.setCode("t-code=1");

            channel = new ZKSysResRequestChannel();
            channel.setPkId("21");
            channel.setCode("t-code=1");
            addChannels.add(channel);
            channel = new ZKSysResRequestChannel();
            channel.setPkId("22");
            channel.setCode("t-code=2");
            addChannels.add(channel);

            // 分配两个差校验
            dels.addAll(s.addByFuncApi(api, addChannels, delChannels));
            e = new ZKSysResFuncApiReqChannel();
            e.setFuncApiId(api.getPkId());
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());

            // 删除第二个，重新分配一个
            addChannels = new ArrayList<>();
            delChannels.add(channel);
            channel = new ZKSysResRequestChannel();
            channel.setPkId("23");
            channel.setCode("t-code=3");
            addChannels.add(channel);
            dels.addAll(s.addByFuncApi(api, addChannels, delChannels));

            e = new ZKSysResFuncApiReqChannel();
            e.setFuncApiId(api.getPkId());
            resList = s.findList(e);
            TestCase.assertEquals(2, resList.size());
            result = 0;
            for (ZKSysResFuncApiReqChannel item : resList) {
                TestCase.assertEquals(api.getPkId(), item.getFuncApiId());
                if ("21".equals(item.getChannelId())) {
                    result = result | 1;
                }
                if ("22".equals(item.getChannelId())) {
                    result = result | 2;
                }
                if ("23".equals(item.getChannelId())) {
                    result = result | 4;
                }
            }
            TestCase.assertEquals(5, result);

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
