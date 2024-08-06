/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKDBService.java 
 * @author Vinson 
 * @Package com.zk.db.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:12:44 AM 
 * @version V1.0   
*/
package com.zk.db.helper.service;

import java.util.List;

import com.zk.db.helper.dao.ZKDBTestDao;
import com.zk.db.helper.entity.ZKDBTestSampleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;

/**
 * @ClassName: ZKDBTestService
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ZKDBTestService {

    @Autowired
    ZKDBTestDao testDao;

    @Transactional(readOnly = false)
    public int insert(ZKDBTestSampleEntity te) {
        return testDao.insert(te);
    }

    @Transactional(readOnly = false)
    public int insertErrorValueNull(ZKDBTestSampleEntity te) {
        testDao.insert(te);

        // 当 value 为 null 时抛出异常，测试事务注解是否有效
        te.getValue().chars();

        return 1;
    }

    @Transactional(readOnly = false)
    public int update(ZKDBTestSampleEntity te) {
        return testDao.update(te);
    }

    @Transactional(readOnly = false)
    public int del(String id) {
        return testDao.del(id);
    }

    public List<ZKDBTestSampleEntity> find(ZKDBTestSampleEntity te) {
        return testDao.find(te);
    }

    public ZKDBTestSampleEntity get(String id) {
        if (ZKStringUtils.isEmpty(id)) {
            return null;
        }
        List<ZKDBTestSampleEntity> tes = testDao.find(new ZKDBTestSampleEntity(id));
        return tes.size() == 1 ? tes.get(0) : null;
    }

    @Transactional(readOnly = false)
    public ZKDBTestSampleEntity updateTransactional(ZKDBTestSampleEntity te) {
        testDao.update(te);

        te = this.get(te.getId());
        System.out.println("[^_^:20201109-0940-001]: " + ZKJsonUtils.toJsonStr(te));

        return te;
    }

}
