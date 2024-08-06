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
 * @Title: ZKJsonTypeHandler.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.typeHandler 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:35:13 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.mybatis.ZKMybatisSqlHelper;

/**
 * 
 * @ClassName: ZKJsonTypeHandler
 * @Description:
 * 
 * 支持 json 数据类型；不支持复杂 sql
 * 插入：#{json} 
 * 修改：c_json=JSON_MERGE_PATCH(c_json, #{json}) 
 * 查询：用循环处理；-- 弃用 JSON_CONTAINS(json, #{json})；如：
 * "<if test='json != null and json.getKeyValues() != null'> AND ",
 *      "<foreach item=\"_v\" index=\"_k\" collection=\"json.getKeyValues()\" open=\"\" separator=\"AND \" close=\"\">" ,
 *          "JSON_UNQUOTE(JSON_EXTRACT(c_json, #{_k})) = #{_v}",
 *      "</foreach>",
 *  "</if>",
 *      注：foreach 中定义的 item 与 index 名称不要与其他参数名相同，否则会干扰参数取值。
 * 
 * @author Vinson
 * @version 1.0
 */
public class ZKJsonTypeHandler extends BaseTypeHandler<ZKJson> {

    protected Logger logger = LogManager.getLogger(getClass());

    protected static final String InsertREGEX = ".*insert\\u0020.*";

    protected static final String UpdateREGEX = ".*update\\u0020.*";

//  protected static final String DeleteREGEX = ".*delete\\u0020.*";
    protected static final String SelectREGEX = ".*select\\u0020.*";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ZKJson parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            return;
        }
        if (ZKMybatisSqlHelper.getDialect().supportsJson()) {
            ps.setString(i, ZKJsonUtils.toJsonStr(parameter));
//            MetaObject metaObject = MetaObject.forObject(ps, ZKMybatisSqlHelper.DEFAULT_OBJECT_FACTORY,
//                    ZKMybatisSqlHelper.DEFAULT_OBJECT_WRAPPER_FACTORY, ZKMybatisSqlHelper.DEFAULT_REFLECTOR_FACTORY);
//            if (metaObject.hasGetter(ZKMybatisSqlHelper.WRAPPER.originalSql)) {
//                String originalSql = (String) metaObject.getValue(ZKMybatisSqlHelper.WRAPPER.originalSql);
//                originalSql = ZKMybatisSqlHelper.formatSql(originalSql).toLowerCase();
//                if (originalSql.matches(InsertREGEX)) {
//                    ps.setObject(i, ZKMybatisSqlHelper.getDialect().getInsertParam(parameter));
//                }
//                else if (originalSql.matches(UpdateREGEX)) {
//                    ps.setObject(i, ZKMybatisSqlHelper.getDialect().getUpdateParam(parameter));
//                }
//                else if (originalSql.matches(SelectREGEX)) {
//                    ps.setObject(i, ZKMybatisSqlHelper.getDialect().getSelectParam(parameter));
//                }
//                else {
//                    ps.setString(i, ZKJsonUtils.toJsonStr(parameter));
//                }
//            }
//            else {
//                ps.setString(i, ZKJsonUtils.toJsonStr(parameter));
//            }
        }
        else {
            ps.setString(i, ZKJsonUtils.toJsonStr(parameter));
        }
    }

    @Override
    public ZKJson getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String vStr = rs.getString(columnName);
        return vStr == null ? null : ZKJsonUtils.parseZKJson(vStr);
    }

    @Override
    public ZKJson getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String vStr = rs.getString(columnIndex);
        return vStr == null ? null : ZKJsonUtils.parseZKJson(vStr);
    }

    @Override
    public ZKJson getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String vStr = cs.getString(columnIndex);
        return vStr == null ? null : ZKJsonUtils.parseZKJson(vStr);
    }

}
