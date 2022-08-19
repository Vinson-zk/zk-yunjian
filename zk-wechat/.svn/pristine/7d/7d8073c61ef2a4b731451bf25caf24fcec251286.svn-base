/**
 * 
 */
package com.zk.wechat.thirdParty.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.thirdParty.entity.ZKThirdPartyAuthAccountUserGps;

import junit.framework.TestCase;

/**
 * ZKThirdPartyAuthAccountUserGpsServiceTest
 * @author 
 * @version 
 */
public class ZKThirdPartyAuthAccountUserGpsServiceTest {

// extends ZKBaseService<String, ZKThirdPartyAuthAccountUserGps, ZKThirdPartyAuthAccountUserGpsDao>
	
	@Test
    public void testDml() {
	
        ZKThirdPartyAuthAccountUserGpsService s = ZKWechatTestHelper.getMainCtx().getBean(ZKThirdPartyAuthAccountUserGpsService.class);

        List<ZKThirdPartyAuthAccountUserGps> dels = new ArrayList<>();

        try {
            ZKThirdPartyAuthAccountUserGps entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = new ZKThirdPartyAuthAccountUserGps();
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