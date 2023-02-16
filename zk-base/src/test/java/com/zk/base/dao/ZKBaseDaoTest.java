package com.zk.base.dao;

import org.junit.Test;

import com.zk.base.helper.dao.ZKBaseHelperDao;
import com.zk.base.helper.entity.ZKBaseHelperEntityLong;
import com.zk.core.utils.ZKClassUtils;
import com.zk.db.entity.ZKDBBaseEntity;
import com.zk.db.mybatis.dao.ZKDBBaseDao;

import junit.framework.TestCase;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description:
 * @ClassName ZKBaseDaoTest
 * @Package com.zk.base.dao
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-10-12 15:36:43
 **/
public class ZKBaseDaoTest {

    @Test
    public void testGetSuperclassByName(){
        try {
            Class<ZKDBBaseEntity<?>> classz = ZKClassUtils.getSuperclassByName(ZKDBBaseDao.class,
                    ZKBaseHelperDao.class, "E");
            System.out.println("[^_^:20221012-1526-001] classz: " + classz.getName());
            TestCase.assertEquals(ZKBaseHelperEntityLong.class, classz);
        }catch (Exception e){
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}
