/**
 * 
 */
package com.zk.mail.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.mail.entity.ZKMailType;
import com.zk.mail.helper.ZKMailTestHelper;

/**
 * ZKMailTypeServiceTest
 * @author 
 * @version 
 */
public class ZKMailTypeServiceTest {

	static ZKMailType makeNew() {
        ZKMailType e = new ZKMailType();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKMailTypeService s = ZKMailTestHelper.getMainCtx().getBean(ZKMailTypeService.class);

        List<ZKMailType> dels = new ArrayList<>();

        try {
            ZKMailType e = null;
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