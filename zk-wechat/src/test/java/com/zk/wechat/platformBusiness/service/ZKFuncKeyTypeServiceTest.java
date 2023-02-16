/**
 * 
 */
package com.zk.wechat.platformBusiness.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyType;

import junit.framework.TestCase;

/**
 * ZKFuncKeyTypeServiceTest
 * 
 * @author
 * @version
 */
public class ZKFuncKeyTypeServiceTest {

    static ZKFuncKeyType makeNew() {
        ZKFuncKeyType e = new ZKFuncKeyType();
        e.setVersion(-1l);
        e.setRemarks("zk.test.data");
        // e.set
        return e;
    }

    @Test
    public void testDml() {

        ZKFuncKeyTypeService s = ZKWechatTestHelper.getMainCtx().getBean(ZKFuncKeyTypeService.class);

        List<ZKFuncKeyType> dels = new ArrayList<>();

        try {
            ZKFuncKeyType e = null;
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