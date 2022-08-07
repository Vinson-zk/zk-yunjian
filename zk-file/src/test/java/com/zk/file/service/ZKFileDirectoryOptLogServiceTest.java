/**
 * 
 */
package com.zk.file.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.file.entity.ZKFileDirectoryOptLog;
import com.zk.file.helper.ZKFileTestHelper;

/**
 * ZKFileDirectoryOptLogServiceTest
 * @author 
 * @version 
 */
public class ZKFileDirectoryOptLogServiceTest {

	static ZKFileDirectoryOptLog makeNew() {
        ZKFileDirectoryOptLog e = new ZKFileDirectoryOptLog();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKFileDirectoryOptLogService s = ZKFileTestHelper.getMainCtx().getBean(ZKFileDirectoryOptLogService.class);

        List<ZKFileDirectoryOptLog> dels = new ArrayList<>();

        try {
            ZKFileDirectoryOptLog e = null;
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