/**
 * 
 */
package com.zk.sys.res.service;
 
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.data.ZKJson;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.res.entity.ZKSysResFuncApi;

import junit.framework.TestCase;

/**
 * ZKSysResFuncApiServiceTest
 * @author 
 * @version 
 */
public class ZKSysResFuncApiServiceTest {

    ZKSysResFuncApi makeNew() {
        ZKSysResFuncApi e = new ZKSysResFuncApi();
        e.setName(ZKJson.parse("{\"zh-CN\":\"t-name\"}"));
        e.setCode("t-code");
        e.setSystemCode("t-systemCode");
        e.setReqContentType(ZKContentType.JSON_UTF8.getContentType());
        e.setResContentType(ZKContentType.X_FORM.getContentType());
        e.setAgentUri("t-agentUri");
        e.setOriginalUri("t-originalUri");
        e.setReqMethods(ZKSysResFuncApi.ReqMethod.GET | ZKSysResFuncApi.ReqMethod.POST);

        return e;
    }
	
	@Test
    public void testDml() {
	
        ZKSysResFuncApiService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysResFuncApiService.class);

        List<ZKSysResFuncApi> dels = new ArrayList<>();

        try {
            ZKSysResFuncApi entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = this.makeNew();
            result = 0;
            result = s.save(entity);
            TestCase.assertEquals(1, result);
            dels.add(entity);

            /*** 修改 ***/
            result = 0;
            result = s.save(entity);
            TestCase.assertEquals(1, result);

            /*** 查询 ***/
            entity = s.get(entity);
            TestCase.assertNotNull(entity);
            TestCase.assertEquals(ZKSysResFuncApi.ReqMethod.GET | ZKSysResFuncApi.ReqMethod.POST,
                    entity.getReqMethods().intValue());
            TestCase.assertEquals(ZKContentType.JSON_UTF8.getContentType(), entity.getReqContentType());
            TestCase.assertEquals(ZKContentType.X_FORM.getContentType(), entity.getResContentType());

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