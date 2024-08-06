/**
 * 
 */
package com.zk.sys.res.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.res.entity.ZKSysResDict;

import junit.framework.TestCase;

/**
 * ZKSysResDictServiceTest
 * @author 
 * @version 
 */
public class ZKSysResDictServiceTest {

// extends ZKBaseService<String, ZKSysResDict, ZKSysResDictDao>
	
	@Test
    public void testDml() {
	
        ZKSysResDictService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysResDictService.class);

        List<ZKSysResDict> dels = new ArrayList<>();

        try {
            ZKSysResDict entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = new ZKSysResDict();
//			entity.set
            result = 0;
            result = s.save(entity);
            TestCase.assertEquals(1, result);
            dels.add(entity);

            /*** 修改 ***/
//            entity.set
            result = 0;
            result = s.save(entity);
            TestCase.assertEquals(1, result);

            /*** 查询 ***/
            entity = s.get(entity);
            TestCase.assertNotNull(entity);

            /*** 删除 ***/
            result = 0;
            result = s.del(entity);
            TestCase.assertEquals(1, result);
            entity = s.get(entity);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, entity.getDelFlag().intValue());

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
    public void testGetDetail() {

        ZKSysResDictService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysResDictService.class);

        List<ZKSysResDict> dels = new ArrayList<>();

        try {
            ZKSysResDict e = null;
            String pkId = "5718922005063926272";

            e = s.getDetail(new ZKSysResDict(pkId));

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