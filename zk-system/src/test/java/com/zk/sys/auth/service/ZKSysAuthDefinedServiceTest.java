/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthDept;
import com.zk.sys.auth.entity.ZKSysAuthRank;
import com.zk.sys.auth.entity.ZKSysAuthRelation;
import com.zk.sys.auth.entity.ZKSysAuthRole;
import com.zk.sys.auth.entity.ZKSysAuthUser;
import com.zk.sys.auth.entity.ZKSysAuthUserType;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.org.entity.ZKSysOrgCompany;

import junit.framework.TestCase;

/**
 * ZKSysAuthDefinedServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthDefinedServiceTest {

    public static List<ZKSysAuthDefined> makeNewList(String... ids) {
        List<ZKSysAuthDefined> list = new ArrayList<>();
        ZKSysAuthDefined e = null;
        for (String id : ids) {
            e = new ZKSysAuthDefined();
            e.setPkId(id);
            e.setName(ZKJson.parse("{\"en-US\": \"test-name-" + id + "\", \"zh-CN\": \"测试-权限-" + id + "\"}"));
            e.setCode("test-authCode" + id);
            e.setStatus(ZKSysAuthDefined.KeyStatus.normal);
            e.setSourceCode("test-sourceCode");
            e.setNewRecord(true);
            list.add(e);
        }
        return list;
    }

    public static ZKSysAuthDefined makeNew() {
        ZKSysAuthDefined e = new ZKSysAuthDefined();
        e.setCode("test-code");
        return e;
    }

    /**
     * 创建一套权限数据，已供测试 权限：3个权限{"-1", "-2", "-3"}；
     * 
     * 公司权限：给公司("-1")分配两个，一个完全拥有，一个仅可使用
     * 
     * 部门权限：给部门分配一个权限
     * 
     * 角色权限：给角色分配一个权限
     * 
     * 用户权限：给用户分配一个权限
     * 
     * 职级权限：给职级分配一个权限
     * 
     * 用户类型权限：给用户类型分配一个权限
     * 
     * API接口：给每个权限分配一个API接口
     * 
     * 菜单：给每个权限分配一个菜单
     * 
     * 导航栏：给每个权限分配一个导航栏
     * 
     */
    public static List<ZKSysAuthDefined> makeAuthData(ConfigurableApplicationContext ctx) {
        ZKSysAuthDefinedService authDefinedService = ctx.getBean(ZKSysAuthDefinedService.class);
        ZKSysAuthCompanyService authCompanyService = ctx.getBean(ZKSysAuthCompanyService.class);
        ZKSysAuthDeptService authDeptService = ctx.getBean(ZKSysAuthDeptService.class);
        ZKSysAuthRoleService authRoleService = ctx.getBean(ZKSysAuthRoleService.class);
        ZKSysAuthUserService authUserService = ctx.getBean(ZKSysAuthUserService.class);
        ZKSysAuthRankService authRankService = ctx.getBean(ZKSysAuthRankService.class);
        ZKSysAuthUserTypeService authUserTypeService = ctx.getBean(ZKSysAuthUserTypeService.class);

//        ZKSysAuthFuncApiService authFuncApiService = ctx.getBean(ZKSysAuthFuncApiService.class);

        String[] authIds = { "-1", "-2", "-3" };
        // 删除测试数据，防此干扰
        for (String id : authIds) {
            authDefinedService.diskDel(new ZKSysAuthDefined(id));
        }
        // 创建一套权限数据，已供测试 权限：3个权限{"-1", "-2", "-3"}；
        List<ZKSysAuthDefined> adList = ZKSysAuthDefinedServiceTest.makeNewList(authIds);
        authDefinedService.saveBatch(adList);
        // 公司权限：给公司("-1")分配两个，一个完全拥有，一个仅可使用
        ZKSysOrgCompany company = new ZKSysOrgCompany();
        company.setGroupCode("tc-groupCode");
        company.setCode("tc-code");
        company.setPkId("-1");
        List<ZKSysAuthDefined> allotList = new ArrayList<>();
        allotList.add(adList.get(0));
        allotList.add(adList.get(1));
        allotList.get(0).setAuthRelation(new ZKSysAuthRelation());
        allotList.get(1).setAuthRelation(new ZKSysAuthRelation());
        allotList.get(0).getAuthRelation().setOwnerType(ZKSysAuthCompany.KeyOwnerType.all);
        allotList.get(1).getAuthRelation().setOwnerType(ZKSysAuthCompany.KeyOwnerType.normal);
        authCompanyService.allotAuthToCompany(company, allotList);

        ZKSysAuthDefined authDefined = allotList.get(0);
        // 部门权限：给部门分配一个权限
        ZKSysAuthDept authDeptRelation = new ZKSysAuthDept();
        authDeptRelation.setAuthId(authDefined.getPkId());
        authDeptRelation.setAuthCode(authDefined.getCode());
        authDeptRelation.setDeptId("-1");
        authDeptRelation.setDeptCode("test-dept-code");
        authDeptRelation.setGroupCode(company.getGroupCode());
        authDeptRelation.setCompanyId(company.getPkId());
        authDeptRelation.setCompanyCode(company.getCode());
        authDeptService.save(authDeptRelation);

        // 角色权限：给角色分配一个权限
        ZKSysAuthRole authRoleRelation = new ZKSysAuthRole();
        authRoleRelation.setAuthId(authDefined.getPkId());
        authRoleRelation.setAuthCode(authDefined.getCode());
        authRoleRelation.setRoleId("-1");
        authRoleRelation.setRoleCode("test-role-code");
        authRoleRelation.setGroupCode(company.getGroupCode());
        authRoleRelation.setCompanyId(company.getPkId());
        authRoleRelation.setCompanyCode(company.getCode());
        authRoleService.save(authRoleRelation);

        // 用户权限：给用户分配一个权限
        ZKSysAuthUser authUserRelation = new ZKSysAuthUser();
        authUserRelation.setAuthId(authDefined.getPkId());
        authUserRelation.setAuthCode(authDefined.getCode());
        authUserRelation.setUserId("-1");
        authUserRelation.setGroupCode(company.getGroupCode());
        authUserRelation.setCompanyId(company.getPkId());
        authUserRelation.setCompanyCode(company.getCode());
        authUserService.save(authUserRelation);

        // 职级权限：给职级分配一个权限
        ZKSysAuthRank authRankRelation = new ZKSysAuthRank();
        authRankRelation.setAuthId(authDefined.getPkId());
        authRankRelation.setAuthCode(authDefined.getCode());
        authRankRelation.setRankId("-1");
        authRankRelation.setRankCode("test-rankCode");
        authRankRelation.setGroupCode(company.getGroupCode());
        authRankRelation.setCompanyId(company.getPkId());
        authRankRelation.setCompanyCode(company.getCode());
        authRankService.save(authRankRelation);

        // 用户类型权限：给用户类型分配一个权限
        ZKSysAuthUserType authUserTypeRelation = new ZKSysAuthUserType();
        authUserTypeRelation.setAuthId(authDefined.getPkId());
        authUserTypeRelation.setAuthCode(authDefined.getCode());
        authUserTypeRelation.setUserTypeId("-1");
        authUserTypeRelation.setUserTypeCode("test-UserTypeCode");
        authUserTypeRelation.setGroupCode(company.getGroupCode());
        authUserTypeRelation.setCompanyId(company.getPkId());
        authUserTypeRelation.setCompanyCode(company.getCode());
        authUserTypeService.save(authUserTypeRelation);

//        // API接口：给每个权限分配一个API接口
//        ZKSysAuthFuncApi authFuncApi = null;
//        for (String id : authIds) {
//            authFuncApi = ZKSysAuthFuncApiServiceTest.makeNew();
//            authFuncApi.setAuthId(id);
//            authFuncApi.setFuncApiId(id);
//            authFuncApi.setFuncApiCode("test-funcApiCode-" + id);
//            authFuncApi.setSystemCode("testSys");
//            authFuncApiService.save(authFuncApi);
//        }
//
//        // 菜单：给每个权限分配一个菜单
//        ZKSysAuthMenu authMenu = null;
//        for (String id : authIds) {
//            funcApi = ZKSysAuthFuncApiServiceTest.makeNew();
//            funcApi.setAuthId(id);
//            funcApi.setFuncApiId(id);
//            funcApi.setFuncApiCode("test-funcApiCode-" + id);
//            funcApi.setSystemCode("testSys");
//            authFuncApiService.save(funcApi);
//        }
//
//        // 导航栏：给每个权限分配一个导航栏
//      ZKSysAuthNav authNav = null;
//      for (String id : authIds) {
//          funcApi = ZKSysAuthFuncApiServiceTest.makeNew();
//          funcApi.setAuthId(id);
//          funcApi.setFuncApiId(id);
//          funcApi.setFuncApiCode("test-funcApiCode-" + id);
//          funcApi.setSystemCode("testSys");
//          authFuncApiService.save(funcApi);
//      }

        return adList;
    }
	
	@Test
    public void testDml() {
	
        ZKSysAuthDefinedService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthDefined> dels = new ArrayList<>();

        try {
            ZKSysAuthDefined e = null;
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