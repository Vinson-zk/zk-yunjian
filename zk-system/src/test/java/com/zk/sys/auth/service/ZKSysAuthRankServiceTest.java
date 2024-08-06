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
import com.zk.sys.auth.entity.ZKSysAuthRank;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgRank;
import com.zk.sys.org.service.ZKSysOrgRankServiceTest;

import junit.framework.TestCase;

/**
 * ZKSysAuthRankServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthRankServiceTest {

    static ZKSysAuthRank makeNew() {
        ZKSysAuthRank e = new ZKSysAuthRank();
        e.setVersion(-1l);
        e.setRemarks("testData");
        e.setCompanyCode("companyCode");
        e.setGroupCode("GroupCode");
        e.setCompanyId("1");
        e.setAuthId("1");
        e.setAuthCode("authCode");
        e.setRankId("1");
        e.setRankCode("deptCode");
        return e;
    }

    @Test
    public void testFindApiCodesByRankId() {

        ZKSysAuthRankService s1 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRankService.class);
        ZKSysAuthFuncApiService s2 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthFuncApiService.class);

        List<ZKSysAuthRank> dels1 = new ArrayList<>();
        List<ZKSysAuthFuncApi> dels2 = new ArrayList<>();

        try {

            List<String> res;
            ZKSysAuthRank auth = null;
            ZKSysAuthFuncApi funcApi = null;

            String rankId = "1";
            String authId = "2";
            String funcApiId = "3";
            String systemCode = "systemCode";
            String authCode = UUID.randomUUID().toString();
            String funcApiCode = UUID.randomUUID().toString();

            auth = makeNew();
            auth.setRankId(rankId);
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

            res = s1.findApiCodesByRankId(rankId, systemCode);
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

        ZKSysAuthRankService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRankService.class);

        List<ZKSysAuthRank> dels = new ArrayList<>();

        try {
            ZKSysAuthRank aut = null;
            ZKSysOrgRank rank = ZKSysOrgRankServiceTest.makeNew();
            ZKSysAuthDefined authDefined = ZKSysAuthDefinedServiceTest.makeNew();

            rank.preInsert();
            authDefined.preInsert();

            dels.addAll(s.allotAuthToRank(rank, Arrays.asList(authDefined)));
            dels.addAll(s.allotAuthToRank(rank, Arrays.asList(authDefined)));

            aut = s.getRelationByAuthIdAndRankId(authDefined.getPkId(), rank.getPkId());
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
    public void testFindAllotAuthByRank() {

        ZKSysAuthRankService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRankService.class);
        ZKSysAuthDefinedService ads = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthDefined> dels = new ArrayList<>();

        try {
            dels.addAll(ZKSysAuthDefinedServiceTest.makeAuthData(ZKSysTestHelper.getMainCtx()));
            List<ZKSysAuthDefined> res;

            ZKSysOrgRank rank = new ZKSysOrgRank();
            rank.setPkId("-1");
            rank.setCompanyId("-1");

            res = s.findAllotAuthByRank(rank, null, null);
            TestCase.assertEquals(2, res.size());

            res = s.findAllotAuthByRank(rank, "no-", null);
            TestCase.assertEquals(0, res.size());

            res = s.findAllotAuthByRank(rank, "权限--1", null);
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