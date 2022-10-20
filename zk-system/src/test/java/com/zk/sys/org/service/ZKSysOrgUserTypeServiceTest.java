/**
 * 
 */
package com.zk.sys.org.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgUserType;

import junit.framework.TestCase;

/**
 * ZKSysOrgUserTypeServiceTest
 * @author 
 * @version 
 */
public class ZKSysOrgUserTypeServiceTest {

    public static ZKSysOrgUserType makeNew() {
        ZKSysOrgUserType e = new ZKSysOrgUserType();
        e.setCode("t-code");
        e.setGroupCode("t-groupCode");
        e.setCompanyCode("companyCode");
        e.setCompanyId("-1");
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKSysOrgUserTypeService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysOrgUserTypeService.class);

        List<ZKSysOrgUserType> dels = new ArrayList<>();

        try {
            ZKSysOrgUserType e = null;
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