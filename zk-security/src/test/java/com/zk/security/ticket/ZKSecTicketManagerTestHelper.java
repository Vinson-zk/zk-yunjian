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
* @Title: ZKSecTicketManagerTestHelper.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 3:04:43 PM 
* @version V1.0 
*/
package com.zk.security.ticket;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.zk.security.principal.ZKSecDefaultDevPrincipal;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.support.redis.ZKSecRedisTicketManager;

import junit.framework.TestCase;

/**
 * @ClassName: ZKSecTicketManagerTestHelper
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecTicketManagerTestHelper {

    private ZKSecTicketManager ticketManager;

    public ZKSecTicketManagerTestHelper(ZKSecTicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    /**
     * 创建权限令牌
     */
    public void testCreateSecTicket() {
        try {
            ZKSecTicket tk = ticketManager.createSecTicket(ticketManager.generateTkId());
            TestCase.assertEquals(ZKSecTicket.KeyType.Security, tk.getType());
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 创建普通令牌
     */
    public void testCreateTicket() {
        try {
            ZKSecTicket tk = ticketManager.createTicket(ticketManager.generateTkId());
            TestCase.assertEquals(ZKSecTicket.KeyType.General, tk.getType());
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 销毁一个令牌
     */
    public void testDropTicket() {
        try {

            Serializable tkId = ticketManager.generateTkId();
            ZKSecTicket tk = ticketManager.createSecTicket(tkId);
            TestCase.assertNotNull(tk);
            ticketManager.dropTicket(tk);
            TestCase.assertFalse(tk.isValid());
            tk = ticketManager.getTicket(tkId);
            TestCase.assertNull(tk);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 销毁指定类型令牌
     */
    public void testDropTicketByType() {
        try {
            Serializable secTkId1 = ticketManager.generateTkId();
            ticketManager.createSecTicket(secTkId1);
            Serializable secTkId2 = ticketManager.generateTkId();
            ticketManager.createSecTicket(secTkId2);
            Serializable tkId = ticketManager.generateTkId();
            ticketManager.createTicket(tkId);
            ticketManager.dropTicketByType(ZKSecTicket.KeyType.Security);
            ZKSecTicket tk = ticketManager.getTicket(secTkId1);
            TestCase.assertNull(tk);
            tk = ticketManager.getTicket(secTkId2);
            TestCase.assertNull(tk);
            tk = ticketManager.getTicket(tkId);
            TestCase.assertNotNull(tk);
            ticketManager.dropTicketByType(ZKSecTicket.KeyType.General);
            tk = ticketManager.getTicket(tkId);
            TestCase.assertNull(tk);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }


    /**
     * 销毁所有令牌
     */
    public void testDropAll() {
        try {
            Serializable secTkId1 = ticketManager.generateTkId();
            ticketManager.createSecTicket(secTkId1);
            Serializable secTkId2 = ticketManager.generateTkId();
            ticketManager.createSecTicket(secTkId2);
            Serializable tkId = ticketManager.generateTkId();
            ticketManager.createTicket(tkId);

            ticketManager.dropAll();

            ZKSecTicket tk = ticketManager.getTicket(secTkId1);
            TestCase.assertNull(tk);
            tk = ticketManager.getTicket(secTkId2);
            TestCase.assertNull(tk);
            tk = ticketManager.getTicket(tkId);
            TestCase.assertNull(tk);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 刷新令牌
     */
    public void testUpdateTicket() {
        try {

            Serializable tkId = ticketManager.generateTkId();
            ZKSecTicket tk = ticketManager.createTicket(tkId, 2000);
            TestCase.assertTrue(tk.isValid());
            Thread.sleep(1500);
            tk.updateLastTime();
            tk.updateLastTime();
            Thread.sleep(1500);
            TestCase.assertTrue(tk.isValid());
            tk.updateLastTime();
            Thread.sleep(1500);
            TestCase.assertTrue(tk.isValid());
            tk.updateLastTime();
            Thread.sleep(1500);
            TestCase.assertTrue(tk.isValid());
            Thread.sleep(1500);
            TestCase.assertFalse(tk.isValid());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 停用令牌 和 启动令牌
     */
    public void testStopAndStart() {
        try {
            Serializable tkId = ticketManager.generateTkId();
            ZKSecTicket tk = ticketManager.createTicket(tkId);
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
            tk.stop();
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Stop, tk.getStatus());
            tk.start();
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
            tk.drop();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 清理过期令牌
     */
    public void testValidateTickets() {
        try {
            long valieSecond = 1, sleepSecond = 2;

            Serializable tkId = ticketManager.generateTkId();
            ZKSecTicket tk = ticketManager.createTicket(tkId, valieSecond * 1000);

            tk = ticketManager.getTicket(tk.getTkId());
            TestCase.assertEquals(tkId, tk.getTkId());

            /* 休眠 2 秒 */
            System.out.println("[^_^:20210817-1133-001] 开始-休眠：" + sleepSecond + " 秒");
            Thread.sleep(sleepSecond * 1000);
            System.out.println("[^_^:20210817-1133-001] 结束-休眠：" + sleepSecond + " 秒");

            tk = ticketManager.getTicket(tk);
            TestCase.assertNull(tk);

            ticketManager.validateTickets();

            // redis 映射测试
            if (ticketManager instanceof ZKSecRedisTicketManager) {
                this.testValidateTickets((ZKSecRedisTicketManager) ticketManager);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    public void testValidateTickets(ZKSecRedisTicketManager zkSecRedisTicketManager) {
        try {
            zkSecRedisTicketManager.dropAll();

            long valieSecond = 1, sleepSecond = 2;
            Serializable tkId = null;
            ZKSecTicket tk = null;
            Map<String, Object> tkMapping = null;

            tkId = ticketManager.generateTkId();
            tk = ticketManager.createTicket(tkId, valieSecond * 1000);
            tk = ticketManager.getTicket(tk.getTkId());
            TestCase.assertEquals(tkId, tk.getTkId());

            tkId = ticketManager.generateTkId();
            tk = ticketManager.createTicket(tkId);
            tk = ticketManager.getTicket(tk.getTkId());
            TestCase.assertEquals(tkId, tk.getTkId());
            tkMapping = zkSecRedisTicketManager.getTicketMapping();
            TestCase.assertEquals(2, tkMapping.size());

            /* 休眠 2 秒 */
            System.out.println("[^_^:20210817-1133-001] 开始-休眠：" + sleepSecond + " 秒");
            Thread.sleep(sleepSecond * 1000);
            System.out.println("[^_^:20210817-1133-001] 结束-休眠：" + sleepSecond + " 秒");
            zkSecRedisTicketManager.validateTickets();
            tkMapping = zkSecRedisTicketManager.getTicketMapping();
            TestCase.assertEquals(1, tkMapping.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 根据身份ID取当前身份所拥有的所有令牌
     */
    public void testFindTickeByPrincipal() {

        ZKSecPrincipal<?> principalPrimary;
        List<ZKSecTicket> tks = null;
        try {
            ZKSecTicket tk;
            ticketManager.dropAll();

            /*** 创建身份 */
            // 创建 userPrincipal 身份
            String pkId = "pk-2018-0713-0814-001";
            String groupCode = "groupCode";
            String username = "username";
            String name = "name";
            ZKSecPrincipal<String> userPrincipal = new ZKSecDefaultUserPrincipal<String>(pkId, username, name, 0, "", 0,
                    groupCode, groupCode, groupCode, groupCode);

            // 创建 appPrincipal 身份
            String devId = "app-2018-0713-0814-001";
            String udid = UUID.randomUUID().toString();
            long osType = ZKSecPrincipal.OS_TYPE.iOS;
            ZKSecPrincipal<String> appPrincipal = new ZKSecDefaultDevPrincipal<String>(pkId, devId, "", osType, udid, 0,
                    groupCode, groupCode, groupCode, groupCode);

            // 创建 userPrincipalOther身份
            String pkIdOther = "pk-2018-0713-0814-001-Other";
            String usernameOther = "usernameOther";
            String nameOther = "nameOther";
            ZKSecPrincipal<String> userPrincipalOther = new ZKSecDefaultUserPrincipal<String>(pkIdOther, usernameOther,
                    nameOther, 0, "", 0, groupCode, groupCode, groupCode, groupCode);

            // 创建 appPrincipalOther 身份
            String appIdOther = "app-2018-0713-0814-001-Other";
            String udidOther = UUID.randomUUID().toString();
            long osTypeOther = ZKSecPrincipal.OS_TYPE.Android;
            ZKSecPrincipal<String> appPrincipalOther = new ZKSecDefaultDevPrincipal<String>(appIdOther, groupCode,
                    udidOther, osTypeOther, "", 0, groupCode, groupCode, groupCode, groupCode);

            /*** 创建令牌 */
            // 创建一个拥有 userPrincipal身份 与 appPrincipal身份 的令牌
            ZKSecPrincipalCollection<String> pcUserAndApp = new ZKSecDefaultPrincipalCollection<String>();
            pcUserAndApp.add("realmNameUser", userPrincipal);
            pcUserAndApp.add("realmNameApp", appPrincipal);
            Serializable tkIdUserAndApp = ticketManager.generateTkId();
            tk = ticketManager.createSecTicket(tkIdUserAndApp);
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
            tk.setPrincipalCollection(pcUserAndApp);
            TestCase.assertEquals(2, tk.getPrincipalCollection().size());

            // 创建一个拥有 userPrincipal身份 的令牌
            ZKSecPrincipalCollection<String> pcUser = new ZKSecDefaultPrincipalCollection<String>();
            pcUser.add("realmNameUser", userPrincipal);
            Serializable tkIdUser = ticketManager.generateTkId();
            tk = ticketManager.createSecTicket(tkIdUser);
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
            tk.setPrincipalCollection(pcUser);
            TestCase.assertEquals(1, tk.getPrincipalCollection().size());

            // 创建一个拥有 appPrincipal身份 的令牌
            ZKSecPrincipalCollection<String> pcApp = new ZKSecDefaultPrincipalCollection<String>();
            pcApp.add("realmNameApp", appPrincipal);
            Serializable tkIdApp = ticketManager.generateTkId();
            tk = ticketManager.createSecTicket(tkIdApp);
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
            tk.setPrincipalCollection(pcApp);
            TestCase.assertEquals(1, tk.getPrincipalCollection().size());

//            // 创建一个拥有 userPrincipalOther身份 与 appPrincipalOther身份 的令牌
//            ZKSecPrincipalCollection pcUserAndAppOther = new ZKSecDefaultPrincipalCollection();
//            pcUserAndAppOther.add("realmNameUser", userPrincipalOther);
//            pcUserAndAppOther.add("realmNameApp", appPrincipalOther);
//            Serializable tkIdUserAndAppOther = ticketManager.generateTkId();
//            tk = ticketManager.createSecTicket(tkIdUserAndAppOther);
//            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
//            tk.setPrincipalCollection(pcUserAndAppOther);
//            TestCase.assertEquals(2, tk.getPrincipalCollection().size());

            // 创建一个拥有 userPrincipalOther身份 的令牌
            ZKSecPrincipalCollection<String> pcUserOther = new ZKSecDefaultPrincipalCollection<String>();
            pcUserOther.add("realmNameApp", userPrincipalOther);
            Serializable tkIdUserOther = ticketManager.generateTkId();
            tk = ticketManager.createSecTicket(tkIdUserOther);
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
            tk.setPrincipalCollection(pcUserOther);
            TestCase.assertEquals(1, tk.getPrincipalCollection().size());

            // 创建一个拥有 appPrincipalOther身份 的令牌
            ZKSecPrincipalCollection<String> pcAppOther = new ZKSecDefaultPrincipalCollection<String>();
            pcAppOther.add("realmNameApp", appPrincipalOther);
            Serializable tkIdAppOther = ticketManager.generateTkId();
            tk = ticketManager.createSecTicket(tkIdAppOther);
            TestCase.assertEquals(ZKSecTicket.KeyStatus.Start, tk.getStatus());
            tk.setPrincipalCollection(pcAppOther);
            TestCase.assertEquals(1, tk.getPrincipalCollection().size());

            /*** 查询 userPrincipal 身份 的令牌，2 个 ***/
            tks = ticketManager.findTickeByPrincipal(userPrincipal);
            TestCase.assertEquals(2, tks.size());
            for (ZKSecTicket t : tks) {
                if (t.getPrincipalCollection().size() == 1) {
                    TestCase.assertEquals(tkIdUser.toString(), t.getTkId().toString());
                }
                else {
                    TestCase.assertEquals(tkIdUserAndApp.toString(), t.getTkId().toString());
                }
                principalPrimary = t.getPrincipalCollection().getPrimaryPrincipal();
                TestCase.assertEquals(userPrincipal.getPkId().toString(), principalPrimary.getPkId().toString());
            }

            /*** 查询 appPrincipal 身份 的令牌，2 个 ***/
            tks = ticketManager.findTickeByPrincipal(appPrincipal);
            TestCase.assertEquals(2, tks.size());
            for (ZKSecTicket t : tks) {
                if (t.getPrincipalCollection().size() == 1) {
                    TestCase.assertEquals(tkIdApp.toString(), t.getTkId().toString());
                    principalPrimary = t.getPrincipalCollection().getPrimaryPrincipal();
                    TestCase.assertEquals(appPrincipal.getPkId().toString(), principalPrimary.getPkId().toString());
                }
                else {
                    TestCase.assertEquals(tkIdUserAndApp.toString(), t.getTkId().toString());
                    principalPrimary = t.getPrincipalCollection().getPrimaryPrincipal();
                    TestCase.assertEquals(userPrincipal.getPkId().toString(), principalPrimary.getPkId().toString());
                }
            }

            /*** 查询 userPrincipalOther 身份 的令牌，1 个 ***/
            tks = ticketManager.findTickeByPrincipal(userPrincipalOther);
            TestCase.assertEquals(1, tks.size());
            tk = tks.get(0);
            TestCase.assertEquals(tkIdUserOther.toString(), tk.getTkId().toString());
            principalPrimary = tk.getPrincipalCollection().getPrimaryPrincipal();
            TestCase.assertEquals(userPrincipalOther.getPkId().toString(), principalPrimary.getPkId().toString());

            /*** 查询 appPrincipalOther 身份 的令牌，1 个 ***/
            tks = ticketManager.findTickeByPrincipal(appPrincipalOther);
            TestCase.assertEquals(1, tks.size());
            tk = tks.get(0);
            TestCase.assertEquals(tkIdAppOther.toString(), tk.getTkId().toString());
            principalPrimary = tk.getPrincipalCollection().getPrimaryPrincipal();
            TestCase.assertEquals(appPrincipalOther.getPkId().toString(), principalPrimary.getPkId().toString());

            /*** 查询 userPrincipal 身份 过滤掉令牌 id 为 tkIdUserAndApp 的令牌，1 个 ***/
            tks = ticketManager.findTickeByPrincipal(userPrincipal,
                    Arrays.asList(ticketManager.getTicket(tkIdUserAndApp)));
            TestCase.assertEquals(1, tks.size());
            tk = tks.get(0);
            TestCase.assertEquals(tkIdUser.toString(), tk.getTkId().toString());
            principalPrimary = tk.getPrincipalCollection().getPrimaryPrincipal();
            TestCase.assertEquals(userPrincipal.getPkId().toString(), principalPrimary.getPkId().toString());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
