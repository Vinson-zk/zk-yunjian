/**
 * 
 */
package com.zk.wechat.thirdParty.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.thirdParty.entity.ZKThirdPartyAuthAccount;

import junit.framework.TestCase;

/**
 * ZKThirdPartyAuthAccountServiceTest
 * @author 
 * @version 
 */
public class ZKThirdPartyAuthAccountServiceTest {

// extends ZKBaseService<String, ZKThirdPartyAuthAccount, ZKThirdPartyAuthAccountDao>
	
	@Test
    public void testDml() {
	
        ZKThirdPartyAuthAccountService s = ZKWechatTestHelper.getMainCtx().getBean(ZKThirdPartyAuthAccountService.class);

        List<ZKThirdPartyAuthAccount> dels = new ArrayList<>();

        try {
            ZKThirdPartyAuthAccount entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = new ZKThirdPartyAuthAccount();
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