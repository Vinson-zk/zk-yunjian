package com.zk.db.mybatis.binding;

import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.db.entity.ZKDBBaseEntity;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;
import com.zk.db.mybatis.dao.ZKDBBaseDao;
import com.zk.db.mybatis.session.ZKDBConfiguration;
import org.apache.ibatis.io.ResolverUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 自定 mapper 映射信息处理
 * @ClassName ZKDBMapperCustomInfoHandler
 * @Package com.zk.db.mybatis.binding
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-10-03 21:18:31
 **/
public class ZKDBMapperCustomInfoHandler {

    private final ZKDBConfiguration configuration;

    private final Map<Class<?>, ZKDBSqlHelper> knownMappers = new HashMap();

    public ZKDBMapperCustomInfoHandler(ZKDBConfiguration configuration){
        this.configuration = configuration;
    }

    public <T> void addMapper(Class<T> type) {
        if(isZKDBBaseDao(type)){
            ZKDBSqlHelper sqlHelper = null;
            if(!this.hasSqlHelper(type)){
                sqlHelper = this.loadSqlHelper(type);
                this.knownMappers.put(type, sqlHelper);
            }else{
                sqlHelper = this.knownMappers.get(type);
            }

            Method[] var2 = type.getMethods();
            int var3 = var2.length;
            Method method = null;
            String mappedStatementId = null;
            for(int var4 = 0; var4 < var3; ++var4) {
                method = var2[var4];
                if (this.canHaveStatement(method)) {
                    mappedStatementId = type.getName() + "." + method.getName();
                    this.configuration.addSqlHelper(mappedStatementId, sqlHelper);
                }
            }
        }
    }

    public void addMappers(String packageName, Class<?> superType) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        Iterator var5 = mapperSet.iterator();

        while(var5.hasNext()) {
            Class<?> mapperClass = (Class)var5.next();
            this.addMapper(mapperClass);
        }
    }

    public void addMappers(String packageName) {
        this.addMappers(packageName, Object.class);
    }

    private boolean isZKDBBaseDao(Class<?> type){
        return ZKDBBaseDao.class.isAssignableFrom(type);
    }

    private ZKDBSqlHelper loadSqlHelper(Class<?> type) {
        try {
            Class<ZKDBBaseEntity> classz = ZKClassUtils.getSuperclassByName(ZKDBBaseDao.class, type, "E");
            ZKDBBaseEntity entity = ZKClassUtils.newInstance(classz);
            return entity.getSqlHelper();
        } catch (InstantiationException|IllegalAccessException e) {
            ZKExceptionsUtils.unchecked(e);
        }
        return null;
    }

    private boolean canHaveStatement(Method method) {
        return !method.isBridge() && !method.isDefault();
    }

    public boolean hasSqlHelper(Class<?> type) {
        return this.knownMappers.containsKey(type);
    }

}