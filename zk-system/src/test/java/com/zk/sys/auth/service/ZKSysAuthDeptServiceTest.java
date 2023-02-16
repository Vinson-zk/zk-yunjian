/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthDept;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgDept;
import com.zk.sys.org.service.ZKSysOrgDeptServiceTest;

import junit.framework.TestCase;

/**
 * ZKSysAuthDeptServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthDeptServiceTest {

    public static ZKSysAuthDept makeNew() {
        ZKSysAuthDept e = new ZKSysAuthDept();
        e.setVersion(-1l);
        e.setRemarks("testData");
        e.setCompanyCode("companyCode");
        e.setGroupCode("GroupCode");
        e.setCompanyId("1");
        e.setAuthId("1");
        e.setAuthCode("authCode");
        e.setDeptId("1");
        e.setDeptCode("deptCode");
        return e;
    }
	
    @Test
    public void testFindApiCodesByDeptId() {

        ZKSysAuthDeptService s1 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDeptService.class);
        ZKSysAuthFuncApiService s2 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthFuncApiService.class);

        List<ZKSysAuthDept> dels1 = new ArrayList<>();
        List<ZKSysAuthFuncApi> dels2 = new ArrayList<>();

        try {

            List<String> res;
            ZKSysAuthDept auth = null;
            ZKSysAuthFuncApi funcApi = null;
            
            String deptId = "1";
            String authId = "2";
            String funcApiId = "3";
            String authCode = UUID.randomUUID().toString();
            String funcApiCode = UUID.randomUUID().toString();
            
            auth = makeNew();
            auth.setAuthId(authId);
            auth.setAuthCode(authCode);
            auth.setDeptId(deptId);
            
            funcApi = ZKSysAuthFuncApiServiceTest.makeNew();
            funcApi.setAuthId(authId);
            funcApi.setFuncApiId(funcApiId);
            funcApi.setFuncApiCode(funcApiCode);
            
            s1.save(auth);
            dels1.add(auth);
            s2.save(funcApi);
            dels2.add(funcApi);
            
            res = s1.findApiCodesByDeptId(deptId);
            TestCase.assertNotNull(res);
            // expected value
            TestCase.assertEquals(funcApiCode, res.get(0));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels1.forEach(item -> {
                s1.diskDel(item);
            });
            dels2.forEach(item -> {
                s2.diskDel(item);
            });
        }
    }

    @Test
    public void testAddRelationByAuthDefined() {

        ZKSysAuthDeptService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDeptService.class);

        List<ZKSysAuthDept> dels = new ArrayList<>();

        try {
            ZKSysAuthDept aut = null;
            ZKSysOrgDept dept = ZKSysOrgDeptServiceTest.makeNew();
            ZKSysAuthDefined authDefined = ZKSysAuthDefinedServiceTest.makeNew();

            dept.preInsert();
            authDefined.preInsert();

            dels.addAll(s.addRelationByDept(dept, Arrays.asList(authDefined), Arrays.asList(authDefined)));
            dels.addAll(s.addRelationByDept(dept, Arrays.asList(authDefined), null));

            aut = s.getRelationByAuthIdAndDeptId(authDefined.getPkId(), dept.getPkId());
            TestCase.assertEquals(2, dels.size());
            TestCase.assertNotNull(aut);
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
    public void testDml() {
	
        ZKSysAuthDeptService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDeptService.class);

        List<ZKSysAuthDept> dels = new ArrayList<>();

        try {
            ZKSysAuthDept e = null;
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