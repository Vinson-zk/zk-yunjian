/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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

            dels.addAll(s.allotAuthToRole(role, Arrays.asList(authDefined)));
            dels.addAll(s.allotAuthToRole(role, Arrays.asList(authDefined)));

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
    public void testFindAllotAuthByRole() {

        ZKSysAuthRoleService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRoleService.class);
        ZKSysAuthDefinedService ads = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthDefined> dels = new ArrayList<>();

        try {
            dels.addAll(ZKSysAuthDefinedServiceTest.makeAuthData(ZKSysTestHelper.getMainCtx()));
            List<ZKSysAuthDefined> res;

            ZKSysOrgRole role = new ZKSysOrgRole();
            role.setPkId("-1");
            role.setCompanyId("-1");

            res = s.findAllotAuthByRole(role, null, null);
            TestCase.assertEquals(2, res.size());

            res = s.findAllotAuthByRole(role, "no-", null);
            TestCase.assertEquals(0, res.size());

            res = s.findAllotAuthByRole(role, "权限--1", null);
            TestCase.assertEquals(1, res.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                ads.diskDel(item);
            });
        }
    }
}