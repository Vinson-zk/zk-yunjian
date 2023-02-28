/**
 * 
 */
package com.zk.file.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.file.entity.ZKFileDirectory;
import com.zk.file.helper.ZKFileTestHelper;

import junit.framework.TestCase;

/**
 * ZKFileDirectoryServiceTest
 * @author 
 * @version 
 */
public class ZKFileDirectoryServiceTest {

	static ZKFileDirectory makeNew() {
        ZKFileDirectory e = new ZKFileDirectory();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
//         e.setCompanyId("1");
         e.setCompanyCode("t-c");
         e.setCode("tc");
         e.setName(ZKJson.parse("{\"zh_CN\":\"tname\"}"));
         e.setStauts(ZKFileDirectory.KeyStatus.disabled);
        return e;
    }
	
	@Test
    public void testDml() {
        ZKFileDirectoryService s = ZKFileTestHelper.getMainCtx().getBean(ZKFileDirectoryService.class);
        List<ZKFileDirectory> dels = new ArrayList<>();
        try {
            ZKFileDirectory e = null;
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
    public void testGetByCode() {
        ZKFileDirectoryService s = ZKFileTestHelper.getMainCtx().getBean(ZKFileDirectoryService.class);
        List<ZKFileDirectory> dels = new ArrayList<>();
        try {
            ZKFileDirectory e = null;
            int result = 0;

            /*** 保存 ***/
            e = makeNew();
            result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);

            /*** 查询 ***/
            e = s.getByCode(e.getCompanyCode(), e.getCode());
            TestCase.assertNotNull(e);
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