/**
 * 
 */
package com.zk.iot.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.iot.entity.ZKIotProdCategores;
import com.zk.iot.entity.ZKIotProdCategores.ValueKey;
import com.zk.iot.helper.ZKIotTestHelper;

import junit.framework.TestCase;

/**
 * ZKIotProdCategoresServiceTest
 * @author 
 * @version 
 */
public class ZKIotProdCategoresServiceTest {

    static String parentId = null;

    static List<ZKIotProdCategores> makeNew(ZKIotProdCategoresService s) {
        List<ZKIotProdCategores> eList = new ArrayList<>();
        ZKIotProdCategores e0;
        e0 = new ZKIotProdCategores();
        e0.setProdCategoresName(ZKJson.parse("{\"zh_CN\":\"测试场景1\"}"));
        e0.setProdCategoresCode("t-code-p01");
        e0.setSort(1);
        e0.setStatus(ValueKey.Status.normal);
        e0.setVersion(-1l);
        e0.setRemarks("zk.test.data");
        e0.setNewRecord(true);
//        e0.preInsert();
        s.preInsert(e0);
        eList.add(e0);
        parentId = e0.getPkId();

        // -------------------------
        e0 = new ZKIotProdCategores();
        e0.setProdCategoresName(ZKJson.parse("{\"zh_CN\":\"测试子场景1-1\"}"));
        e0.setProdCategoresCode("t-code-c01");
        e0.setSort(1);
        e0.setStatus(ValueKey.Status.normal);
        e0.setVersion(-1l);
        e0.setRemarks("zk.test.data");
        e0.setParentId(parentId);
        eList.add(e0);

        e0 = new ZKIotProdCategores();
        e0.setProdCategoresName(ZKJson.parse("{\"zh_CN\":\"测试子场景1-2\"}"));
        e0.setProdCategoresCode("t-code-c02");
        e0.setSort(999999);
        e0.setStatus(ValueKey.Status.normal);
        e0.setVersion(-1l);
        e0.setRemarks("zk.test.data");
        e0.setParentId(parentId);
        eList.add(e0);

        return eList;
    }
	
	@Test
    public void testDml() {
	
        ZKIotProdCategoresService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdCategoresService.class);
        List<ZKIotProdCategores> dels = new ArrayList<>();

        try {
            List<ZKIotProdCategores> eList = null;
            int result = 0;
            String updateStr;

            /*** 制作测试数据。及保存 ***/
            eList = makeNew(s);
            for (ZKIotProdCategores e : eList) {
                result = 0;
                result = s.save(e);
                TestCase.assertEquals(1, result);
                dels.add(e);
            }

            /*** 修改，查询 ***/
            for (ZKIotProdCategores e : eList) {
                e.setNewRecord(false);
                result = 0;
                updateStr = e.getRemarks();
                updateStr = updateStr + " update";
                e.setRemarks(updateStr);
                result = s.save(e);
                TestCase.assertEquals(1, result);
                e = s.get(e.getPkId());
                TestCase.assertEquals(updateStr, e.getRemarks());
            }

            /*** 删除 ***/
            for (ZKIotProdCategores e : eList) {
                result = 0;
                result = s.del(e);
                TestCase.assertEquals(3, result);
                e = s.get(e);
                TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, e.getDelFlag().intValue());
            }

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

    // 根据代码取实体及明细
    @Test
    public void testGetByCode() {

        ZKIotProdCategoresService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdCategoresService.class);
        List<ZKIotProdCategores> dels = new ArrayList<>();

        try {
            List<ZKIotProdCategores> eList = null;
            int result = 0;

            /*** 制作测试数据。及保存 ***/
            eList = makeNew(s);
            for (ZKIotProdCategores e : eList) {
                result = 0;
                result = s.save(e);
                TestCase.assertEquals(1, result);
                dels.add(e);
            }

            /*** getCode ***/
            for (ZKIotProdCategores e : eList) {
                TestCase.assertNull(e.getParent());
                e = s.getByCode(e.getProdCategoresCode());
                TestCase.assertNotNull(e);
                if ("t-code-c01".equals(e.getProdCategoresCode())) {
                    TestCase.assertNotNull(e.getParent());
                }
            }
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

        ZKIotProdCategoresService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdCategoresService.class);
        List<ZKIotProdCategores> dels = new ArrayList<>();

        try {
            List<ZKIotProdCategores> eList = null;
            int result = 0;

            /*** 制作测试数据。及保存 ***/
            eList = makeNew(s);
            for (ZKIotProdCategores e : eList) {
                result = 0;
                result = s.save(e);
                TestCase.assertEquals(1, result);
                dels.add(e);
            }

            /*** updateStatusDisable, updateStatusActive ***/
            ZKIotProdCategores e = null;
            e = s.get(parentId);
            TestCase.assertEquals(ValueKey.Status.normal, e.getStatus().intValue());

            s.updateStatusDisable(parentId);
            e = s.get(parentId);
            TestCase.assertEquals(ValueKey.Status.disabled, e.getStatus().intValue());

            s.updateStatusActive(parentId);
            e = s.get(parentId);
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

        ZKIotProdCategoresService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdCategoresService.class);
        List<ZKIotProdCategores> dels = new ArrayList<>();

        try {
            List<ZKIotProdCategores> eList = null;
            int result = 0, maxSort = 0;

            /*** 制作测试数据。及保存 ***/
            eList = makeNew(s);
            for (ZKIotProdCategores e : eList) {
                result = 0;
                result = s.save(e);
                TestCase.assertEquals(1, result);
                dels.add(e);
            }

            /*** getMaxSort ***/
            maxSort = s.getMaxSort(parentId);
            TestCase.assertEquals(999999, maxSort);
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
