package com.zk.db.mybatis.session;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.Configuration;

import com.zk.db.mybatis.binding.ZKDBMapperCustomInfoHandler;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/**
 * Copyright (c) 2017-2022 Vinson. address: All rights reserved This software is the confidential and proprietary information of Vinson. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered
 * into with Vinson;
 *
 * @Description:
 * @ClassName ZKDBMybatisConfiguration
 * @Package com.zk.db.mybatis.session
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-30 17:39:19
 **/
public class ZKDBMybatisConfiguration extends Configuration {

    protected final Map<String, ZKDBSqlHelper> mappedSqlHelper = new HashMap<>();
    protected final ZKDBMapperCustomInfoHandler mapperCustomInfoHandler;

    public ZKDBMybatisConfiguration() {
        super();
        mapperCustomInfoHandler = new ZKDBMapperCustomInfoHandler(this);
    }

    public void addSqlHelper(String mappedStatementId, ZKDBSqlHelper sqlHelper){
        this.mappedSqlHelper.put(mappedStatementId, sqlHelper);
    }

    public ZKDBSqlHelper getSqlHelper(String mappedStatementId){
        return this.mappedSqlHelper.get(mappedStatementId);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        this.mapperRegistry.addMappers(packageName, superType);
        this.mapperCustomInfoHandler.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        this.mapperRegistry.addMappers(packageName);
        this.mapperCustomInfoHandler.addMappers(packageName);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        this.mapperRegistry.addMapper(type);
        this.mapperCustomInfoHandler.addMapper(type);
    }


}
