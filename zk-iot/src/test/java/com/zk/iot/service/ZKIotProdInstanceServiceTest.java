/**
 * 
 */
package com.zk.iot.service;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.iot.entity.ZKIotProdInstance;
import com.zk.iot.entity.ZKIotProdInstance.ValueKey;
import com.zk.iot.helper.ZKIotTestHelper;

import junit.framework.TestCase;

/**
 * ZKIotProdInstanceServiceTest
 * @author 
 * @version 
 */
public class ZKIotProdInstanceServiceTest {

    // =======================================================
    // 需要提前准备好测试数据：t-code-prodModel 产品
    // =======================================================

    static final String prodModel = "prodModelNet";

	static ZKIotProdInstance makeNew() {
        ZKIotProdInstance e = new ZKIotProdInstance();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        e.setSnNum("t-snSum-test");
        e.setProdModelCode(prodModel);
        e.setName(ZKJson.parse("{\"zh_CN\":\"测试产品设备1\"}"));
        e.setStatus(ValueKey.Status.normal);
        e.setMacAddr("t-mac-addr");
        return e;
    }
	
    @Test
    public void testDml() {
        ZKIotProdInstanceService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdInstanceService.class);
        List<ZKIotProdInstance> dels = new ArrayList<>();

        try {
            ZKIotProdInstance e = null;
            int result = 0;
            String pkId;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** 修改 ***/
            result = 0;
            e.setName(ZKJson.parse("{\"zh_CN\":\"测试产品设备1-update\"}"));
            result = s.save(e);
            TestCase.assertEquals(1, result);
            e = s.get(e);
            TestCase.assertNotNull(e);
            TestCase.assertEquals(ZKJson.parse("{\"zh_CN\":\"测试产品设备1-update\"}").toString(),
                    e.getName().toString());

            /*** 代码已存在 ***/
            pkId = e.getPkId();
            e.setPkId(null);
            try {
                s.save(e);
                TestCase.assertTrue(false);
            }
            catch(ZKBusinessException ex) {
                TestCase.assertEquals("zk.iot.000008", ex.getCode());
            }

            /*** 查询 ***/
            e = s.get(pkId);
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

    // 测试 禁用，激活
    @Test
    public void testUpdateStatus() {
        ZKIotProdInstanceService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdInstanceService.class);
        List<ZKIotProdInstance> dels = new ArrayList<>();

        try {
            ZKIotProdInstance e = null;
            int result = 0;
            String pkId;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            pkId = e.getPkId();

            /*** updateStatusDisable, updateStatusActive ***/
            e = s.get(pkId);
            TestCase.assertEquals(ValueKey.Status.normal, e.getStatus().intValue());

            s.updateStatusDisable(pkId);
            e = s.get(pkId);
            TestCase.assertEquals(ValueKey.Status.disabled, e.getStatus().intValue());

            s.updateStatusActive(pkId);
            e = s.get(pkId);
            TestCase.assertEquals(ValueKey.Status.normal, e.getStatus().intValue());

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

    // 测试 取指定父节点下的子节点的最大排序号
    @Test
    public void testGetMaxSort() {
        ZKIotProdInstanceService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdInstanceService.class);
        List<ZKIotProdInstance> dels = new ArrayList<>();

        try {
            ZKIotProdInstance e = null;
            int result = 0;
            Integer maxSort = 0;

            /*** getMaxSort ***/
//          有数据时，这个测试无意义
//            maxSort = s.getMaxSort();
//            TestCase.assertEquals(null, maxSort);

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** getMaxSort ***/
            maxSort = s.getMaxSort();
            TestCase.assertEquals(e.getSort().intValue(), maxSort.intValue());

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

    // 根据型号和序列号取实体
    @Test
    public void testGetSn() {
        ZKIotProdInstanceService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdInstanceService.class);
        List<ZKIotProdInstance> dels = new ArrayList<>();

        try {
            ZKIotProdInstance e = null;
            int result = 0;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            String prodModelCode = e.getProdModelCode();
            String snNum = e.getSnNum();

            /*** getBySnAndModelCode, getBySnAndModelId ***/
            e = s.getBySnAndModelCode(prodModelCode, snNum);
            TestCase.assertNotNull(e);

            prodModelCode = e.getProdModelId();
            e = s.getBySnAndModelId(prodModelCode, snNum);
            TestCase.assertNotNull(e);
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
    
    // 根据 mac 地址取实体
    @Test
    public void testGetByMacAddr() {
        ZKIotProdInstanceService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdInstanceService.class);
        List<ZKIotProdInstance> dels = new ArrayList<>();

        try {
            ZKIotProdInstance e = null;
            int result = 0;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            String macAddr = e.getMacAddr();

            /*** getByMacAddr ***/
            e = s.getByMacAddr(macAddr);
            TestCase.assertNotNull(e);
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

    // 测试 修改最后心跳时间和查询是否在线
    @Test
    public void testUpdateLastInTimeAndFind() {
        ZKIotProdInstanceService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdInstanceService.class);
        List<ZKIotProdInstance> dels = new ArrayList<>();

        try {
            ZKIotProdInstance e = null, p = null;
            int result = 0;
            List<ZKIotProdInstance> res = null;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            Date lastInTime = null;
            String pkId = e.getPkId();
            String macAddr = e.getMacAddr();
            String prodModelCode = e.getProdModelCode();
            String snNum = e.getSnNum();
            p = new ZKIotProdInstance();
            p.setProdModelCode(e.getProdModelCode());
            p.setSnNum(e.getSnNum());

            long renewalIntervalInSeconds = ZKEnvironmentUtils
                    .getLong("zk.iot.prod.instance.renewal.interval.in.seconds", 0L);

            /*** updateLastInTime, updateLastInTimeBySn, updateLastInTimeByMacAddr, findList ***/
            // 根据 ID 修改最后心跳时间为 当前是时间
            lastInTime = ZKDateUtils.getToday();
            result = s.updateLastInTime(pkId, lastInTime);
            TestCase.assertEquals(1, result);
            p.setIsIn(true);
            res = s.findList(p);
            TestCase.assertEquals(1, res.size());
            p.setIsIn(false);
            res = s.findList(p);
            TestCase.assertEquals(1, res.size());

            // 根据 序列号 修改最后心跳时间为 一个有效间隔前的时间
            lastInTime = ZKDateUtils.parseDate(System.currentTimeMillis() - renewalIntervalInSeconds);
            result = s.updateLastInTimeBySn(prodModelCode, snNum, lastInTime);
            TestCase.assertEquals(1, result);
            p.setIsIn(true);
            res = s.findList(p);
            TestCase.assertEquals(0, res.size());
            p.setIsIn(false);
            res = s.findList(p);
            TestCase.assertEquals(1, res.size());

            // 根据 mac 地址 修改最后心跳时间为 当前是时间
            lastInTime = ZKDateUtils.getToday();
            result = s.updateLastInTimeByMacAddr(macAddr, lastInTime);
            TestCase.assertEquals(1, result);
            p.setIsIn(true);
            res = s.findList(p);
            TestCase.assertEquals(1, res.size());
            p.setIsIn(false);
            res = s.findList(p);
            TestCase.assertEquals(1, res.size());
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


