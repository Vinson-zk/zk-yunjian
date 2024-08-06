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
 * @Title: ZKDynamicTransactionManager.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic.spring.transaction 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:31:40 AM 
 * @version V1.0   
*/
package com.zk.db.dynamic.spring.transaction;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import com.zk.db.dynamic.ZKDynamicDataSourceHelper;
import com.zk.db.dynamic.ZKPatternType;

/** 
* @ClassName: ZKDynamicTransactionManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDynamicTransactionManager extends DataSourceTransactionManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1702189803474830540L;

    protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * 只读事务到读库，读写事务到写库
     * 
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        // 设置数据源
        boolean readOnly = definition.isReadOnly();
        if (readOnly) {
            ZKDynamicDataSourceHelper.putDataSource(ZKPatternType.READ);
        }
        else {
            ZKDynamicDataSourceHelper.putDataSource(ZKPatternType.WRITE);
        }
        logger.debug("[^_^:201706241146-001] doBegin: DynamicDataSourceHolder.putDataSource={}",
                ZKDynamicDataSourceHelper.getDataSource().name());
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     * 
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        ZKDynamicDataSourceHelper.clearDataSource();
    }

}
