/**
 * 
 */
package com.zk.sys.org.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgRole;

import junit.framework.TestCase;

/**
 * ZKSysOrgRoleServiceTest
 * @author 
 * @version 
 */
public class ZKSysOrgRoleServiceTest {

    public static ZKSysOrgRole makeNew() {
        ZKSysOrgRole e = new ZKSysOrgRole();
        e.setCode("t-code");
        e.setGroupCode("t-groupCode");
        e.setCompanyId("-1");
        e.setCompanyCode("t-companyCode");
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKSysOrgRoleService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysOrgRoleService.class);

        List<ZKSysOrgRole> dels = new ArrayList<>();

        try {
            ZKSysOrgRole e = null;
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