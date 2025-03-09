/**
 * 
 */
package com.zk.iot.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.exception.ZKBusinessException;
import com.zk.iot.entity.ZKIotProdAttribute;
import com.zk.iot.entity.ZKIotProdAttribute.ValueKey;
import com.zk.iot.helper.ZKIotTestHelper;

import junit.framework.TestCase;

/**
 * ZKIotProdAttributeServiceTest
 * @author 
 * @version 
 */
public class ZKIotProdAttributeServiceTest {

	static ZKIotProdAttribute makeNew() {
        ZKIotProdAttribute e = new ZKIotProdAttribute();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        e.setAttrFrom(ValueKey.AttrFrom.prodInstance);
        e.setTargetId("-1");
        e.setAttrCode("t-code-attrCode-func-01");
        e.setAttrType(ValueKey.AttrType.func);
        e.setAttrName(ZKJson.parse("{\"zh_CN\":\"测试属性1\"}"));
        e.setStatus(ValueKey.Status.normal);
        return e;
    }
	
	@Test
    public void testDml() {
        ZKIotProdAttributeService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdAttributeService.class);
        List<ZKIotProdAttribute> dels = new ArrayList<>();

        try {
            ZKIotProdAttribute e = null;
            int result = 0;
            String pkId;

            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** 修改 ***/
            result = 0;
            e.setAttrName(ZKJson.parse("{\"zh_CN\":\"测试属性1-update\"}"));
            result = s.save(e);
            TestCase.assertEquals(1, result);
            e = s.get(e);
            TestCase.assertNotNull(e);
            TestCase.assertEquals(ZKJson.parse("{\"zh_CN\":\"测试属性1-update\"}").toString(), e.getAttrName().toString());

            /*** 代码已存在 ***/
            pkId = e.getPkId();
            e.setPkId(null);
            try {
                s.save(e);
                TestCase.assertTrue(false);
            }
            catch(ZKBusinessException ex) {
                TestCase.assertEquals("zk.iot.000011", ex.getCode());
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

    // 根据代码取实体及明细
    @Test
    public void testGetCode() {
        ZKIotProdAttributeService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdAttributeService.class);
        List<ZKIotProdAttribute> dels = new ArrayList<>();

        try {
            ZKIotProdAttribute e = null;
            int result = 0;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** getByCode ***/
            e = s.getByCode(e.getAttrFrom(), e.getTargetId(), e.getAttrCode());
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

    // 测试 禁用，激活
    @Test
    public void testUpdateStatus() {
        ZKIotProdAttributeService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdAttributeService.class);
        List<ZKIotProdAttribute> dels = new ArrayList<>();

        try {
            ZKIotProdAttribute e = null;
            int result = 0;
            String pkId;

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

    // 测试 取指定目标ID 下指定类型属性的 最大排序号
    @Test
    public void testGetMaxSort() {
        ZKIotProdAttributeService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdAttributeService.class);
        List<ZKIotProdAttribute> dels = new ArrayList<>();

        try {
            ZKIotProdAttribute e = null;
            int result = 0;
            Integer maxSort = 0;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** getMaxSort ***/
            maxSort = s.getMaxSort(e.getAttrFrom(), e.getTargetId(), e.getAttrType());
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

    // 测试 取指定父节点下的子节点的最大排序号
    @Test
    public void testFindProdAttrs() {
        ZKIotProdAttributeService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdAttributeService.class);
        List<ZKIotProdAttribute> dels = new ArrayList<>();

        try {
            ZKIotProdAttribute e = null;
            int result = 0;
            List<ZKIotProdAttribute> resList = null;

            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** findProdAttrs ***/
            resList = s.findProdAttrs(e.getAttrFrom(), e.getTargetId(), e.getAttrType());
            TestCase.assertEquals(1, resList.size());

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


