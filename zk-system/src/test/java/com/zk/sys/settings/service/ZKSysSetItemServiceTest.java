/**
 * 
 */
package com.zk.sys.settings.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.settings.entity.ZKSysSetItem;

import junit.framework.TestCase;

/**
 * ZKSysSetItemServiceTest
 * @author 
 * @version 
 */
public class ZKSysSetItemServiceTest {

// extends ZKBaseService<String, ZKSysSetItem, ZKSysSetItemDao>

    static ZKSysSetItem makeNew() {
        ZKSysSetItem e = new ZKSysSetItem();

        e.setCollectionId("6094827194473775616");
        e.setCollectionCode("Security_Center");
        e.setName(ZKJson.parse("{\"zh_CN\":\"test-name\"}"));
        e.setCode("t-code");
        e.setType(0);
        e.setValueType(0);
        e.setValue("22");
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKSysSetItemService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysSetItemService.class);

        List<ZKSysSetItem> dels = new ArrayList<>();

        try {
            ZKSysSetItem entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = makeNew();
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