/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthUser;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.org.service.ZKSysOrgUserServiceTest;

import junit.framework.TestCase;

/**
 * ZKSysAuthUserServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthUserServiceTest {

    public static ZKSysAuthUser makeNew() {
        ZKSysAuthUser e = new ZKSysAuthUser();
        e.setVersion(-1l);
        e.setRemarks("testData");
        e.setCompanyCode("companyCode");
        e.setGroupCode("GroupCode");
        e.setCompanyId("1");
        e.setAuthId("1");
        e.setAuthCode("authCode");
        e.setUserId("1");
        return e;
    }

    @Test
    public void testFindApiCodesByUserId() {

        ZKSysAuthUserService s1 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserService.class);
        ZKSysAuthFuncApiService s2 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthFuncApiService.class);

        List<ZKSysAuthUser> dels1 = new ArrayList<>();
        List<ZKSysAuthFuncApi> dels2 = new ArrayList<>();

        try {

            List<String> res;
            ZKSysAuthUser auth = null;
            ZKSysAuthFuncApi funcApi = null;

            String userId = "1";
            String authId = "2";
            String funcApiId = "3";
            String systemCode = "systemCode";
            String authCode = UUID.randomUUID().toString();
            String funcApiCode = UUID.randomUUID().toString();

            auth = makeNew();
            auth.setUserId(userId);
            auth.setAuthId(authId);
            auth.setAuthCode(authCode);

            funcApi = ZKSysAuthFuncApiServiceTest.makeNew();
            funcApi.setAuthId(authId);
            funcApi.setFuncApiId(funcApiId);
            funcApi.setFuncApiCode(funcApiCode);
            funcApi.setSystemCode(systemCode);

            s1.save(auth);
            dels1.add(auth);
            s2.save(funcApi);
            dels2.add(funcApi);

            res = s1.findApiCodesByUserId(userId, systemCode);
            TestCase.assertNotNull(res);
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

        ZKSysAuthUserService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserService.class);
        ZKSysOrgUserService userS = ZKSysTestHelper.getMainCtx().getBean(ZKSysOrgUserService.class);
        ZKSysAuthDefinedService authDefinedS = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthUser> dels = new ArrayList<>();

        try {
            ZKSysAuthUser aut = null;
            ZKSysOrgUser user = ZKSysOrgUserServiceTest.makeNew();
            ZKSysAuthDefined authDefined = ZKSysAuthDefinedServiceTest.makeNew();

            userS.preInsert(user);
            authDefinedS.preInsert(authDefined);

            dels.addAll(s.allotAuthToUser(user, Arrays.asList(authDefined)));
            dels.addAll(s.allotAuthToUser(user, Arrays.asList(authDefined)));

            aut = s.getRelationByAuthIdAndUserId(authDefined.getPkId(), user.getPkId());
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
    public void testFindAllotAuthByUser() {

        ZKSysAuthUserService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserService.class);
        ZKSysAuthDefinedService ads = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthDefined> dels = new ArrayList<>();

        try {
            dels.addAll(ZKSysAuthDefinedServiceTest.makeAuthData(ZKSysTestHelper.getMainCtx()));
            List<ZKSysAuthDefined> res;

            ZKSysOrgUser user = new ZKSysOrgUser();
            user.setPkId("-1");
            user.setCompanyId("-1");

            res = s.findAllotAuthByUser(user, null, null);
            TestCase.assertEquals(2, res.size());

            res = s.findAllotAuthByUser(user, "no-", null);
            TestCase.assertEquals(0, res.size());

            res = s.findAllotAuthByUser(user, "权限--1", null);
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