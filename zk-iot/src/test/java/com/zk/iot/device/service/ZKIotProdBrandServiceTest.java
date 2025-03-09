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
import com.zk.iot.entity.ZKIotProdBrand;
import com.zk.iot.entity.ZKIotProdBrand.ValueKey;
import com.zk.iot.helper.ZKIotTestHelper;

import junit.framework.TestCase;

/**
 * ZKIotProdBrandServiceTest
 * @author 
 * @version 
 */
public class ZKIotProdBrandServiceTest {

	static ZKIotProdBrand makeNew() {
        ZKIotProdBrand e = new ZKIotProdBrand();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        e.setBrandCode("t-code-brand-temp");
        e.setBrandName(ZKJson.parse("{\"zh_CN\":\"测试产品品牌1\"}"));
        e.setStatus(ValueKey.Status.normal);
        return e;
    }
	
	@Test
    public void testDml() {
        ZKIotProdBrandService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdBrandService.class);
        List<ZKIotProdBrand> dels = new ArrayList<>();

        try {
            ZKIotProdBrand e = null;
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
            e.setBrandName(ZKJson.parse("{\"zh_CN\":\"测试产品品牌1-update\"}"));
            result = s.save(e);
            TestCase.assertEquals(1, result);
            e = s.get(e);
            TestCase.assertNotNull(e);
            TestCase.assertEquals(ZKJson.parse("{\"zh_CN\":\"测试产品品牌1-update\"}").toString(),
                    e.getBrandName().toString());

            /*** 代码已存在 ***/
            pkId = e.getPkId();
            e.setPkId(null);
            try {
                s.save(e);
                TestCase.assertTrue(false);
            }
            catch(ZKBusinessException ex) {
                TestCase.assertEquals("zk.iot.000002", ex.getCode());
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
    public void testGetByBandCode() {
        ZKIotProdBrandService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdBrandService.class);
        List<ZKIotProdBrand> dels = new ArrayList<>();

        try {
            ZKIotProdBrand e = null;
            int result = 0;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** getBrandCode ***/
            e = s.getByCode(e.getBrandCode());
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
        ZKIotProdBrandService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdBrandService.class);
        List<ZKIotProdBrand> dels = new ArrayList<>();

        try {
            ZKIotProdBrand e = null;
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
        ZKIotProdBrandService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdBrandService.class);
        List<ZKIotProdBrand> dels = new ArrayList<>();

        try {
            ZKIotProdBrand e = null;
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
}