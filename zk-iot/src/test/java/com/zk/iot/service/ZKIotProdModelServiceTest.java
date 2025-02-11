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
import com.zk.iot.entity.ZKIotProdModel;
import com.zk.iot.entity.ZKIotProdModel.ValueKey;
import com.zk.iot.helper.ZKIotTestHelper;

import junit.framework.TestCase;

/**
 * ZKIotProdModelServiceTest
 * @author 
 * @version 
 */
public class ZKIotProdModelServiceTest {

    // =======================================================
    // 需要提前准备好测试数据：t-code-prod 产品
    // =======================================================


    static final String prodCode = "prodNet";

	static ZKIotProdModel makeNew() {
        ZKIotProdModel e = new ZKIotProdModel();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        e.setProdCode(prodCode);
        e.setProdModelCode("t-code-prodModel-temp");
        e.setProdModelName(ZKJson.parse("{\"zh_CN\":\"测试产品型号1\"}"));
        e.setStatus(ValueKey.Status.normal);
        return e;
    }

    @Test
    public void testDml() {
        ZKIotProdModelService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdModelService.class);
        List<ZKIotProdModel> dels = new ArrayList<>();

        try {
            ZKIotProdModel e = null;
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
            e.setProdModelName(ZKJson.parse("{\"zh_CN\":\"测试产品型号1-update\"}"));
            result = s.save(e);
            TestCase.assertEquals(1, result);
            e = s.get(e);
            TestCase.assertNotNull(e);
            TestCase.assertEquals(ZKJson.parse("{\"zh_CN\":\"测试产品型号1-update\"}").toString(),
                    e.getProdModelName().toString());

            /*** 代码已存在 ***/
            pkId = e.getPkId();
            e.setPkId(null);
            try {
                s.save(e);
                TestCase.assertTrue(false);
            }
            catch(ZKBusinessException ex) {
                TestCase.assertEquals("zk.iot.000007", ex.getCode());
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
    public void testGetProdModelCode() {
        ZKIotProdModelService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdModelService.class);
        List<ZKIotProdModel> dels = new ArrayList<>();

        try {
            ZKIotProdModel e = null;
            int result = 0;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** getProdModelCode ***/
            e = s.getByCode(e.getProdModelCode());
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
        ZKIotProdModelService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdModelService.class);
        List<ZKIotProdModel> dels = new ArrayList<>();

        try {
            ZKIotProdModel e = null;
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
        ZKIotProdModelService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdModelService.class);
        List<ZKIotProdModel> dels = new ArrayList<>();

        try {
            ZKIotProdModel e = null;
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

    // 测试 修改数据处理协议
    @Test
    public void testUpdateDataProtocol() {
        ZKIotProdModelService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotProdModelService.class);
        List<ZKIotProdModel> dels = new ArrayList<>();

        try {
            ZKIotProdModel e = null;
            int result = 0;
            String pkId;
            String businessDataProtocolId;
            String businessDataProtocolCode;

            ZKIotTestHelper.putCurrentUser();
            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            pkId = e.getPkId();

            /*** updateDataProtocol ***/
            businessDataProtocolId = "-1";
            businessDataProtocolCode = "t-businessDataProtocolCode";
            result = s.updateDataProtocol(pkId, businessDataProtocolId, businessDataProtocolCode);
            TestCase.assertEquals(1, result);

            e = s.get(pkId);
            TestCase.assertEquals(businessDataProtocolId, e.getBusinessDataProtocolId());
            TestCase.assertEquals(businessDataProtocolCode, e.getBusinessDataProtocolCode());
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