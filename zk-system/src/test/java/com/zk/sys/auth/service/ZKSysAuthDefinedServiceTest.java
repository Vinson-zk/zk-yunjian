/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.helper.ZKSysTestHelper;

import junit.framework.TestCase;

/**
 * ZKSysAuthDefinedServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthDefinedServiceTest {

    public static ZKSysAuthDefined makeNew() {
        ZKSysAuthDefined e = new ZKSysAuthDefined();
        e.setCode("test-code");
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKSysAuthDefinedService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthDefined> dels = new ArrayList<>();

        try {
            ZKSysAuthDefined e = null;
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
}