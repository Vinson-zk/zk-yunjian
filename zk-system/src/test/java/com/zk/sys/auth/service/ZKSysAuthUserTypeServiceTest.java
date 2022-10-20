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
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthUserType;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgUserType;
import com.zk.sys.org.service.ZKSysOrgUserTypeServiceTest;

import junit.framework.TestCase;

/**
 * ZKSysAuthUserTypeServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthUserTypeServiceTest {

    public static ZKSysAuthUserType makeNew() {
        ZKSysAuthUserType e = new ZKSysAuthUserType();
        e.setVersion(-1l);
        e.setRemarks("testData");
        e.setCompanyCode("companyCode");
        e.setGroupCode("GroupCode");
        e.setCompanyId("1");
        e.setAuthId("1");
        e.setAuthCode("authCode");
        e.setUserTypeId("1");
        e.setUserTypeCode("userTypeCode");
        return e;
    }

    @Test
    public void testFindApiCodesByUserTypeId() {

        ZKSysAuthUserTypeService s1 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserTypeService.class);
        ZKSysAuthFuncApiService s2 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthFuncApiService.class);

        List<ZKSysAuthUserType> dels1 = new ArrayList<>();
        List<ZKSysAuthFuncApi> dels2 = new ArrayList<>();

        try {

            List<String> res;
            ZKSysAuthUserType auth = null;
            ZKSysAuthFuncApi funcApi = null;

            String userTypeId = "1";
            String authId = "2";
            String funcApiId = "3";
            String authCode = UUID.randomUUID().toString();
            String funcApiCode = UUID.randomUUID().toString();

            auth = makeNew();
            auth.setUserTypeId(userTypeId);
            auth.setAuthId(authId);
            auth.setAuthCode(authCode);

            funcApi = ZKSysAuthFuncApiServiceTest.makeNew();
            funcApi.setAuthId(authId);
            funcApi.setFuncApiId(funcApiId);
            funcApi.setFuncApiCode(funcApiCode);

            s1.save(auth);
            dels1.add(auth);
            s2.save(funcApi);
            dels2.add(funcApi);

            res = s1.findApiCodesByUserTypeId(userTypeId);
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
        

        ZKSysAuthUserTypeService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserTypeService.class);

        List<ZKSysAuthUserType> dels = new ArrayList<>();

        try {
            ZKSysAuthUserType aut = null;
            ZKSysOrgUserType userType = ZKSysOrgUserTypeServiceTest.makeNew();
            ZKSysAuthDefined authDefined = ZKSysAuthDefinedServiceTest.makeNew();

//            userType.setPkId("-1");
//            authDefined.setPkId("-2");

            userType.preInsert();
            authDefined.preInsert();

            dels.addAll(s.addRelationByUserType(userType, Arrays.asList(authDefined), Arrays.asList(authDefined)));
            dels.addAll(s.addRelationByUserType(userType, Arrays.asList(authDefined), null));

            aut = s.getRelationByAuthIdAndUserTypeId(authDefined.getPkId(), userType.getPkId());
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
	
        ZKSysAuthUserTypeService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserTypeService.class);

        List<ZKSysAuthUserType> dels = new ArrayList<>();

        try {
            ZKSysAuthUserType e = null;
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