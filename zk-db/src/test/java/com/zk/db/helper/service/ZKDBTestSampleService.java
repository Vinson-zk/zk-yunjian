package com.zk.db.helper.service;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.helper.dao.ZKDBTestDao;
import com.zk.db.helper.dao.ZKDBTestSampleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 测试 service
 * @ClassName ZKDBTestSampleService
 * @Package com.zk.db.helper.service
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-27 18:44:06
 **/
@Service
@Transactional(readOnly = true)
public class ZKDBTestSampleService {

    @Autowired
    ZKDBTestSampleDao testSampleDao;



}
