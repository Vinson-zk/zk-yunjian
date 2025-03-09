/**
 * 
 */
package com.zk.iot.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.iot.entity.ZKIotLogEventRecord;
import com.zk.iot.helper.ZKIotTestHelper;

/**
 * ZKIotLogEventRecordServiceTest
 * @author 
 * @version 
 */
public class ZKIotLogEventRecordServiceTest {

	static ZKIotLogEventRecord makeNew() {
        ZKIotLogEventRecord e = new ZKIotLogEventRecord();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKIotLogEventRecordService s = ZKIotTestHelper.getMainCtx().getBean(ZKIotLogEventRecordService.class);

        List<ZKIotLogEventRecord> dels = new ArrayList<>();

        try {
            ZKIotLogEventRecord e = null;
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