/**
 * 
 */
package com.zk.sys.res.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.res.entity.ZKSysResApplicationSystem;

import junit.framework.TestCase;

/**
 * ZKSysResApplicationSystemServiceTest
 * @author 
 * @version 
 */
public class ZKSysResApplicationSystemServiceTest {

// extends ZKBaseService<String, ZKSysResApplicationSystem, ZKSysResApplicationSystemDao>
	
	@Test
    public void testDml() {
	
        ZKSysResApplicationSystemService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysResApplicationSystemService.class);

        List<ZKSysResApplicationSystem> dels = new ArrayList<>();

        try {
            ZKSysResApplicationSystem entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = new ZKSysResApplicationSystem();
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
}