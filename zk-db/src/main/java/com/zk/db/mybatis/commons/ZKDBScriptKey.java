package com.zk.db.mybatis.commons;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: mybatis 脚本字段定义
 * @ClassName ZKDBScriptKey
 * @Package com.zk.db.mybatis.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-13 11:51:12
 **/
public interface ZKDBScriptKey {
    public static final String[] Script = {"<script>", "</script>"};
    /**
    trim 标签的主要属性有四个，如下表所示：
    属性名	作用
    prefix	给sql增加前缀
    suffix	给sql增加后缀
    prefixOverrides	去掉sql前面多余的关键字或者字符
    suffixOverrides	去掉sql后面多余的关键字或者字符
     */
    public static final String[] trimAndOr = {"<trim prefixOverrides=\"and|or\">", "</trim>"};
    public static final String[] where = {"<where>", "</where>"};
    public static final String[] set = {"<set>", "</set>"};
}
