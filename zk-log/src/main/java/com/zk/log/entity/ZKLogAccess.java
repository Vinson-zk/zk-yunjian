/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKLogAccess.java 
* @author Vinson 
* @Package com.zk.log.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 13, 2022 9:52:20 AM 
* @version V1.0 
*/
package com.zk.log.entity;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;
import org.springframework.data.annotation.Transient;

/** 
* @ClassName: ZKLogAccess 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_log_access", alias = "logAccess", orderBy = " c_create_date ASC ")
public class ZKLogAccess extends ZKBaseEntity<String, ZKLogAccess> {

    static ZKDBSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKLogAccess());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 集团代码
     */
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class,
            query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String groupCode;

    /**
     * 公司ID
     */
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_id", isInsert = true, javaType = String.class,
            query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyId;

    /**
     * 公司代码
     */
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyCode;

    @ZKColumn(name = "c_user_id", isInsert = true, javaType = String.class)
    String userId; // 创建用户ID

    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_date_time", isInsert = true, javaType = Date.class)
    Date dateTime; // 修改时间戳 “yyyy-MM-dd HH:mm:ss.ssssss”

    @ZKColumn(name = "c_user_agent", isInsert = true, javaType = String.class)
    String userAgent; // 用户访问媒介【操作系统、浏览器类型、终端情况等信息】

    @ZKColumn(name = "c_title", isInsert = true, javaType = String.class)
    String title; // 功能名称

    @ZKColumn(name = "c_remote_addr", isInsert = true, javaType = String.class,
            query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String remoteAddr; // 访问者IP地址

    @ZKColumn(name = "c_request_uri", isInsert = true, javaType = String.class,
            query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String requestUri; // 请求路径

    @ZKColumn(name = "c_method", isInsert = true, javaType = String.class)
    String method; // 请求方式

    @ZKColumn(name = "c_params", isInsert = true, javaType = String.class)
    String params; // 请求参数

    @ZKColumn(name = "c_exception", isInsert = true, javaType = String.class)
    String exception; // 异常信息

    public ZKLogAccess() {
        super();
    }

    public ZKLogAccess(String id) {
        super(id);
    }

    /**
     * @return groupCode sa
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * @return companyId sa
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @return companyCode sa
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @param groupCode
     *            the groupCode to set
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * @param companyId
     *            the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @param companyCode
     *            the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setParamsMap(Map<String, ?> paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
//        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
//            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
//            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
//            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue,
//                    100));
//        }
        this.params = params.toString();
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
    
}
