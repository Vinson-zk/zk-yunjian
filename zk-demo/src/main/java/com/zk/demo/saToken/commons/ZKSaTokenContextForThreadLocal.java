package com.zk.demo.saToken.commons;

import java.util.HashMap;
import java.util.Map;

import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.context.model.SaStorage;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 测试 sa-token 本地存储
 * @ClassName ZKSaTokenContextForThreadLocal
 * @Package com.zk.demo.saToken.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-05 18:16:31
 **/
public class ZKSaTokenContextForThreadLocal implements SaTokenContext {

    public static interface Key {
        public static final String SaStorage = "_SaStorage";
        public static final String ZKSaRequest = "ZKSaRequest";
    }

    static ThreadLocal<Map<String, Object>> threadLocal = new InheritableThreadLocal<Map<String, Object>>();

    protected Map<String, Object> getKeyValue(){
        Map<String, Object> tlMap = threadLocal.get();
        if(tlMap == null){
            tlMap = new HashMap<>();
            threadLocal.set(tlMap);
        }
        return tlMap;
    }

    @Override
    public SaRequest getRequest() {
        return null;
    }

    @Override
    public SaResponse getResponse() {
        return null;
    }

    @Override
    public SaStorage getStorage() {
        SaStorage tlStorage = (SaStorage) this.getKeyValue().get(Key.SaStorage);
        if(tlStorage == null){
            tlStorage = new ZKSaStorage();
            this.getKeyValue().put(Key.SaStorage, tlStorage);
        }
        return tlStorage;
    }

    @Override
    public boolean matchPath(String s, String s1) {
        return false;
    }

    @Override
    public boolean isValid() {
//        return SaTokenContext.super.isValid();
        return true;
    }
}
