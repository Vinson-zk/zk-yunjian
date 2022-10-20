/**
 * 
 */
package com.zk.sys.org.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgDept;

import junit.framework.TestCase;

/**
 * ZKSysOrgDeptServiceTest
 * @author 
 * @version 
 */
public class ZKSysOrgDeptServiceTest {

    public static ZKSysOrgDept makeNew() {
        ZKSysOrgDept e = new ZKSysOrgDept();
        e.setCode("t-code");
        e.setGroupCode("t-groupCode");
        e.setCompanyId("-1");
        e.setCompanyCode("t-companyCode");
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKSysOrgDeptService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysOrgDeptService.class);

        List<ZKSysOrgDept> dels = new ArrayList<>();

        try {
            ZKSysOrgDept e = null;
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