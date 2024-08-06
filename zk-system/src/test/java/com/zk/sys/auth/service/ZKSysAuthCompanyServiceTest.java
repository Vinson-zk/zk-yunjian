/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKPage;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.helper.ZKSysTestHelper;

import junit.framework.TestCase;

/**
 * ZKSysAuthCompanyServiceTest
 * @author 
 * @version 
 */
public class ZKSysAuthCompanyServiceTest {

	ZKSysAuthCompany makeNew() {
        ZKSysAuthCompany e = new ZKSysAuthCompany();
        // e.set
        return e;
    }
	
    @Test
    public void testDml() {

        ZKSysAuthCompanyService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthCompanyService.class);

        List<ZKSysAuthCompany> dels = new ArrayList<>();

        try {
            ZKSysAuthCompany e = null;
            int result = 0;

            /*** 保存 ***/
            e = this.makeNew();
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

    // 测试 查询指定公司的权限及拥有权限的方式
    @Test
    public void testFindAuthByCompanyId() {

        ZKSysAuthCompanyService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthCompanyService.class);
        ZKSysAuthDefinedService ads = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthDefined> dels = new ArrayList<>();

        try {
            dels.addAll(ZKSysAuthDefinedServiceTest.makeAuthData(ZKSysTestHelper.getMainCtx()));
            List<ZKSysAuthDefined> res;
            ZKPage<ZKSysAuthDefined> page;
            int count = 0;
            String companyId;
            Integer ownerType;
            String searchValue;

            companyId = "-1";
            searchValue = null;
            page = null;
            ownerType = ZKSysAuthCompany.KeyOwnerType.all;
            count = 1;
            res = s.findAuthByCompanyId(companyId, ownerType, searchValue, page);
            TestCase.assertEquals(count, res.size());
            TestCase.assertNotNull(res.get(0));

            ownerType = ZKSysAuthCompany.KeyOwnerType.normal;
            count = 1;
            res = s.findAuthByCompanyId(companyId, ownerType, searchValue, page);
            TestCase.assertEquals(count, res.size());
            TestCase.assertNotNull(res.get(0));

            ownerType = null;
            count = 2;
            res = s.findAuthByCompanyId(companyId, ownerType, searchValue, page);
            TestCase.assertEquals(count, res.size());
            TestCase.assertNotNull(res.get(0));

            // 分页查询
            ownerType = null;
            page = ZKPage.asPage();
            page.setPageSize(1);
            count = 2;
            res = s.findAuthByCompanyId(companyId, ownerType, searchValue, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());

            ownerType = ZKSysAuthCompany.KeyOwnerType.all;
            page = ZKPage.asPage();
            page.setPageSize(1);
            count = 1;
            res = s.findAuthByCompanyId(companyId, ownerType, searchValue, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());
            TestCase.assertNotNull(res.get(0).getAuthRelation());
            TestCase.assertEquals(ownerType.intValue(), res.get(0).getAuthRelation().getOwnerType().intValue());

            ownerType = ZKSysAuthCompany.KeyOwnerType.normal;
            page = ZKPage.asPage();
            page.setPageSize(1);
            count = 1;
            res = s.findAuthByCompanyId(companyId, ownerType, searchValue, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());
            TestCase.assertNotNull(res.get(0).getAuthRelation());
            TestCase.assertEquals(ownerType.intValue(), res.get(0).getAuthRelation().getOwnerType().intValue());

            ownerType = null;
            page = ZKPage.asPage();
            page.setPageNo(2);
            page.setPageSize(1);
            count = 2;
            res = s.findAuthByCompanyId(companyId, ownerType, searchValue, page);
            TestCase.assertEquals(0, res.size());
            TestCase.assertEquals(count, page.getTotalCount());

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

    // 测试 根据授权公司与被授权公司，查询可授权的权限列表及被授权公司已拥有的权限状态
    @Test
    public void testFindAllotAuthByCompanyId() {

        ZKSysAuthCompanyService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthCompanyService.class);
        ZKSysAuthDefinedService ads = ZKSysTestHelper.getMainCtx().getBean(ZKSysAuthDefinedService.class);

        List<ZKSysAuthDefined> dels = new ArrayList<>();

        try {
            dels.addAll(ZKSysAuthDefinedServiceTest.makeAuthData(ZKSysTestHelper.getMainCtx()));
            String fromCompanyId;
            List<ZKSysAuthDefined> res;
            ZKPage<ZKSysAuthDefined> page;
            int count = 0;

            /* 平台给拥有者公司授权 ----- */
            fromCompanyId = null;
            page = null;
            count = ads.findList(new ZKSysAuthDefined()).size();
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(count, res.size());
            TestCase.assertNotNull(res.get(0));

            // 分页查询
            page = ZKPage.asPage();
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());

            page = ZKPage.asPage();
            page.setPageNo(1);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());

            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(2);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());

            page = ZKPage.asPage();
            page.setPageNo(20);
            page.setPageSize(2);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(0, res.size());
            TestCase.assertEquals(count, page.getTotalCount());

            /* 给其他公司授权 ----- */
            fromCompanyId = "-1";
            page = null;
            count = 1;
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, null);
            TestCase.assertEquals(count, res.size());

            // 分页查询
            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());

            page = ZKPage.asPage();
            page.setPageNo(1);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(0, res.size());
            TestCase.assertEquals(count, page.getTotalCount());

            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", null, page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());
            TestCase.assertNotNull(res.get(0).getAuthRelation());
            TestCase.assertEquals(ZKSysAuthCompany.KeyOwnerType.all,
                    res.get(0).getAuthRelation().getOwnerType().intValue());

            // searchValue
            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", "测试", page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());
            TestCase.assertNotNull(res.get(0).getAuthRelation());
            TestCase.assertEquals(ZKSysAuthCompany.KeyOwnerType.all,
                    res.get(0).getAuthRelation().getOwnerType().intValue());

            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", "-sourceCode", page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());
            TestCase.assertNotNull(res.get(0).getAuthRelation());
            TestCase.assertEquals(ZKSysAuthCompany.KeyOwnerType.all,
                    res.get(0).getAuthRelation().getOwnerType().intValue());

            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", "-authCode", page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());
            TestCase.assertNotNull(res.get(0).getAuthRelation());
            TestCase.assertEquals(ZKSysAuthCompany.KeyOwnerType.all,
                    res.get(0).getAuthRelation().getOwnerType().intValue());

            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", "-notHave-", page);
            TestCase.assertEquals(0, res.size());
            TestCase.assertEquals(0, page.getTotalCount());

            page = ZKPage.asPage();
            page.setPageNo(0);
            page.setPageSize(1);
            res = s.findAllotAuthByCompanyId(fromCompanyId, "-1", "zh", page);
            TestCase.assertEquals(page.getPageSize(), res.size());
            TestCase.assertEquals(count, page.getTotalCount());
            TestCase.assertNotNull(res.get(0).getAuthRelation());
            TestCase.assertEquals(ZKSysAuthCompany.KeyOwnerType.all,
                    res.get(0).getAuthRelation().getOwnerType().intValue());

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
