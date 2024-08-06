/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthRole;
import com.zk.sys.auth.entity.ZKSysAuthUserRole;
import com.zk.sys.helper.ZKSysTestHelper;

import junit.framework.TestCase;

/**
 * ZKSysAuthUserRoleServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthUserRoleServiceTest {

    public static ZKSysAuthUserRole makeNew() {
        ZKSysAuthUserRole e = new ZKSysAuthUserRole();
        e.setVersion(-1l);
        e.setRemarks("testData");
        e.setCompanyCode("companyCode");
        e.setGroupCode("GroupCode");
        e.setCompanyId("1");
        e.setUserId("1");
        e.setRoleId("1");
        e.setRoleCode("roleCode");
        return e;
    }

    @Test
    public void testFindApiCodesByUserId() {

        ZKSysAuthUserRoleService s1 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserRoleService.class);
        ZKSysAuthRoleService s2 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthRoleService.class);
        ZKSysAuthFuncApiService s3 = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthFuncApiService.class);

        List<ZKSysAuthUserRole> dels1 = new ArrayList<>();
        List<ZKSysAuthRole> dels2 = new ArrayList<>();
        List<ZKSysAuthFuncApi> dels3 = new ArrayList<>();

        try {

            List<String> res;
            ZKSysAuthUserRole auth = null;
            ZKSysAuthRole authRole = null;
            ZKSysAuthFuncApi funcApi = null;

            String userId = "1";
            String roleId = "2";
            String authId = "3";
            String funcApiId = "4";
            String systemCode = "systemCode";
            String authCode = UUID.randomUUID().toString();
            String funcApiCode = UUID.randomUUID().toString();

            auth = makeNew();
            auth.setUserId(userId);
            auth.setRoleId(roleId);

            authRole = ZKSysAuthRoleServiceTest.makeNew();
            authRole.setAuthId(authId);
            authRole.setAuthCode(authCode);
            authRole.setRoleId(roleId);

            funcApi = ZKSysAuthFuncApiServiceTest.makeNew();
            funcApi.setAuthId(authId);
            funcApi.setFuncApiId(funcApiId);
            funcApi.setFuncApiCode(funcApiCode);
            funcApi.setSystemCode(systemCode);

            s1.save(auth);
            dels1.add(auth);
            s2.save(authRole);
            dels2.add(authRole);
            s3.save(funcApi);
            dels3.add(funcApi);

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
            dels3.forEach(item -> {
                s3.diskDel(item);
            });
        }
    }
	
	@Test
    public void testDml() {
        ZKSysAuthUserRoleService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserRoleService.class);
        List<ZKSysAuthUserRole> dels = new ArrayList<>();
        try {
            ZKSysAuthUserRole e = null;
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

    @Test
    public void testDiskDel() {
        ZKSysAuthUserRoleService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthUserRoleService.class);
        List<ZKSysAuthUserRole> dels = new ArrayList<>();
        try {
            ZKSysAuthUserRole e = null;
            int result = 0;
            String userId = "-1", roleId = "-1";

            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);
            s.diskDelByUserId(userId);
            TestCase.assertEquals(null, s.getRelationByUserIdAndRoleId(userId, roleId));

            e.setNewRecord(true);
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);
            s.diskDelByRoleId(roleId);
            TestCase.assertEquals(null, s.getRelationByUserIdAndRoleId(userId, roleId));

            e.setNewRecord(true);
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);
            s.diskDelByUserIdAndRoleId(userId, roleId);
            TestCase.assertEquals(null, s.getRelationByUserIdAndRoleId(userId, roleId));

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