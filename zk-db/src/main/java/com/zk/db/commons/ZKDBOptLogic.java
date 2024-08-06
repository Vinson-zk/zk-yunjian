package com.zk.db.commons;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: sql 逻辑运算符
 * @ClassName ZKDBOptLogic
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-26 00:07:38
 **/
public enum ZKDBOptLogic {

    AND("AND", " AND "), OR("OR", " OR ");

    // key
    private final String key;

    // 转译字符
    private final String esc;

    ZKDBOptLogic(String key, String esc) {
        this.key = key;
        this.esc = esc;
    }

    /**
     * @return key sa
     */
    public String getKey() {
        return key;
    }

    /**
     * @return esc sa
     */
    public String getEsc() {
        return esc;
    }

}
