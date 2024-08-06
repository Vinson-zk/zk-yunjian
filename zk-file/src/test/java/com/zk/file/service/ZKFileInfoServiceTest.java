/**
 * 
 */
package com.zk.file.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.file.entity.ZKFileInfo;
import com.zk.file.helper.ZKFileTestSpringBootMainHelper;

import junit.framework.TestCase;

/**
 * ZKFileInfoServiceTest
 * @author 
 * @version 
 */
public class ZKFileInfoServiceTest {

	static ZKFileInfo makeNew() {
        ZKFileInfo e = new ZKFileInfo();
        e.setGroupCode("t-test-groupCode");
        e.setCompanyId("-1");
        e.setCompanyCode("t-test-companyCode");
        e.afterAttrSet();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set

        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKFileInfoService s = ZKFileTestSpringBootMainHelper.run().getBean(ZKFileInfoService.class);

        List<ZKFileInfo> dels = new ArrayList<>();

        try {
            ZKFileInfo e = null;
            int result = 0;

            /*** 保存 ***/
            e = makeNew();
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
            System.out.println("[^_^:20231229-0101-001] s.get: " + ZKJsonUtils.toJsonStr(e));

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

    @Test
    public void testGet() {

        ZKFileInfoService s = ZKFileTestSpringBootMainHelper.run().getBean(ZKFileInfoService.class);

        try {
            ZKFileInfo e = null;
            String pkId = "6962321432462230016";
            /*** 查询 ***/
            e = s.get(new ZKFileInfo(pkId));
            TestCase.assertNotNull(e);
            // "createDate":"2023-12-29 11:47:50",
            System.out.println("[^_^:20231229-0003-001] e: " + ZKJsonUtils.toJsonStr(e));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}