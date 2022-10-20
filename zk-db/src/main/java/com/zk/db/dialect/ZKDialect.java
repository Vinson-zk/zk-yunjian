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
 * @Title: ZKDialect.java 
 * @author Vinson 
 * @Package com.zk.db.dialect 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:29:37 AM 
 * @version V1.0   
*/
package com.zk.db.dialect;

import com.zk.core.commons.data.ZKJson;

/**
 * @ClassName: ZKDialect
 * @Description: 类似 hibernate 的 Dialect, 但只精简出分页部分
 * @author Vinson
 * @version 1.0
 */
public interface ZKDialect {

    /**
     * 数据库本身是否支持分页当前的分页查询方式 如果数据库不支持的话，则不进行数据库分页
     *
     * @return true：支持当前的分页查询方式
     */
    public boolean supportsLimit();

    /**
     * 将sql转换为分页SQL，分别调用分页sql
     *
     * @param sql
     *            SQL语句
     * @param offset
     *            开始条数
     * @param limit
     *            每页显示多少纪录条数
     * @return 分页查询的sql
     */
    public String getLimitString(String sql, int offset, int limit);

    /**
     * 处理 like 查询参数 like 查询参数处理交给数据库函数处理，不用 java 函数做替换。
     * 
     * @param sql
     * @return
     */
    public String replaceLikeParam(String sql);

    /**
     * sql 是否有分页
     *
     * @Title: isPage
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年3月12日 下午12:57:45
     * @param sql
     * @return
     * @return boolean
     */
    public boolean isPage(String sql);

    /**
     * sql 是否有替换字符
     *
     * @Title: isReplace
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年3月12日 下午12:57:45
     * @param sql
     * @return
     * @return boolean
     */
    public boolean isReplace(String sql);

    /**
     * 是否支持 json
     */
    public boolean supportsJson();

    /**
     * 取对象 Json 插入 sql 字符串 INSERT into t_test(id, json) values(@id,
     * JSON_INSERT('{}', '$.t1', 10, '$.t2', '[true, false]', '$.t4',
     * JSON_INSERT('{}', '$.t1', 10)));
     * 
     * @return
     */
    public Object getInsertParam(ZKJson json);

    /**
     * 取对象 Json 修改 sql 字符串 update t_test set json=JSON_SET(json, '$.t2', 'update
     * t2', '$.t3', 'add attr t3', '$.t4.t1', 'ddd') where id=@id;
     * 此方法生成这个字符串：'$.t2', 'update t2', '$.t3', 'add attr t3', '$.t4.t1', 'ddd'
     * 
     * @return
     */
    public Object getUpdateParam(ZKJson json);

    /**
     * 取对象 Json 查询 sql 字符串 JSON_UNQUOTE(json_extract(t.type_name,
     * #{localeStrOpt})) like
     * 
     * @return
     */
    public Object getSelectParam(ZKJson json);

}
