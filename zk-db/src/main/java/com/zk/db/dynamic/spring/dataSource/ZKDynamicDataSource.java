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
 * @Title: ZKDynamicDataSource.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic.spring.dataSource 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:31:20 AM 
 * @version V1.0   
*/
package com.zk.db.dynamic.spring.dataSource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.zk.db.dynamic.ZKDynamicDataSourceHelper;
import com.zk.db.dynamic.ZKPatternType;

/** 
* @ClassName: ZKDynamicDataSource 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDynamicDataSource extends AbstractRoutingDataSource {

    protected final Logger log = LogManager.getLogger(getClass());

    /**
     * 写数据源
     */
    private DataSource writeDataSource;

    /**
     * 读数据源
     */
    private DataSource readDataSource;

    @Override
    public void afterPropertiesSet() {
        if (this.writeDataSource == null) {
            logger.error("Property 'writeDataSource' is required");
            throw new IllegalArgumentException("Property 'writeDataSource' is required");
        }
        setDefaultTargetDataSource(writeDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(ZKPatternType.WRITE.name(), writeDataSource);
        if (readDataSource != null) {
            targetDataSources.put(ZKPatternType.READ.name(), readDataSource);
        }
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {

        ZKPatternType pattern = ZKDynamicDataSourceHelper.getDataSource();

        log.debug("[^_^:20190911-1725-001] determineCurrentLookupKey: dataSourcePattern={}",
                pattern == null ? "null" : pattern.name());

        if (pattern == null || pattern == ZKPatternType.WRITE) {
            return ZKPatternType.WRITE.name();
        }

        return ZKPatternType.READ.name();
    }

    // ---------------- pojo
    public DataSource getWriteDataSource() {
        return writeDataSource;
    }

    public void setWriteDataSource(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public DataSource getReadDataSource() {
        return readDataSource;
    }

    public void setReadDataSource(DataSource readDataSource) {
        this.readDataSource = readDataSource;
    }

}
