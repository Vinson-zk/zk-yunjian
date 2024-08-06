package com.zk.demo.saToken.commons;

import java.util.HashMap;
import java.util.Map;

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
 * @Description: test SaStorage
 * @ClassName ZKSaStorage
 * @Package com.zk.demo.saToken.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-05 18:30:30
 **/
public class ZKSaStorage implements SaStorage {

    Map<String, Object> dataMap = new HashMap<String, Object>();

    @Override
    public Object getSource() {
        return dataMap;
    }

    @Override
    public void set(String s, Object o) {
        dataMap.put(s, o);
    }

    @Override
    public Object get(String s) {
        return dataMap.get(s);
    }

    @Override
    public void delete(String s) {
        dataMap.remove(s);
    }
}
