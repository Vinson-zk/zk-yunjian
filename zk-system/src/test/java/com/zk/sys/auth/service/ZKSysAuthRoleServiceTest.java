/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthRole;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.service.ZKSysOrgRoleServiceTest;

import junit.framework.TestCase;

/**
 * ZKSysAuthRoleServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthRoleServiceTest {

    public static ZKSysAuthRole makeNew() {
        ZKSysAuthRole e = new ZKSysAuthRole();

        e.setVersion(-1l);
        e.setRemarks("testData");
        e.setCompanyCode("companyCode");
        e.setGroupCode("GroupCode");
        e.setCompanyId("1");
        e.setAuthId("1");
        e.setAuthCode("authCode");
        e.setRoleId("1");
        e.setRoleCode("roleCode");
        return e;
    }

//    @Test
//    public void testFindApiCodesByRoleId() {
//
//        ZKSysAuthRoleService s1 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRoleService.class);
//        ZKSysAuthFuncApiService s2 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthFuncApiService.class);
//
//        List<ZKSysAuthRole> dels1 = new ArrayList<>();
//        List<ZKSysAuthFuncApi> dels2 = new ArrayList<>();
//
//        try {
//
//            List<String> res;
//            ZKSysAuthRole auth = null;
//            ZKSysAuthFuncApi funcApi = null;
//
//            String roleId = "1";
//            String authId = "2";
//            String funcApiId = "3";
//            String authCode = UUID.randomUUID().toString();
//            String funcApiCode = UUID.randomUUID().toString();
//
//            auth = makeNew();
//            auth.setRoleId(roleId);
//            auth.setAuthId(authId);
//            auth.setAuthCode(authCode);
//
//            funcApi = ZKSysAuthFuncApiServiceTest.makeNew();
//            funcApi.setAuthId(authId);
//            funcApi.setFuncApiId(funcApiId);
//            funcApi.setFuncApiCode(funcApiCode);
//
//            s1.save(auth);
//            dels1.add(auth);
//            s2.save(funcApi);
//            dels2.add(funcApi);
//
//            res = s1.findApiCodesByRoleId(roleId);
//            TestCase.assertNotNull(res);
//            TestCase.assertEquals(funcApiCode, res.get(0));
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            TestCase.assertTrue(false);
//        } finally {
//            dels1.forEach(item -> {
//                s1.diskDel(item);
//            });
//            dels2.forEach(item -> {
//                s2.diskDel(item);
//            });
//        }
//    }
	
	@Test
    public void testAddRelationByAuthDefined() {

        ZKSysAuthRoleService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRoleService.class);

        List<ZKSysAuthRole> dels = new ArrayList<>();

        try {
            ZKSysAuthRole aut = null;
            ZKSysOrgRole role = ZKSysOrgRoleServiceTest.makeNew();
            ZKSysAuthDefined authDefined = ZKSysAuthDefinedServiceTest.makeNew();

            role.preInsert();
            authDefined.preInsert();

            dels.addAll(s.addRelationByRole(role, Arrays.asList(authDefined), Arrays.asList(authDefined)));
            dels.addAll(s.addRelationByRole(role, Arrays.asList(authDefined), null));

            aut = s.getRelationByAuthIdAndRoleId(authDefined.getPkId(), role.getPkId());
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
	
        ZKSysAuthRoleService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRoleService.class);

        List<ZKSysAuthRole> dels = new ArrayList<>();

        try {
            ZKSysAuthRole e = null;
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