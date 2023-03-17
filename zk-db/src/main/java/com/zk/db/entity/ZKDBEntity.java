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
* @Title: ZKDBEntity.java 
* @author Vinson 
* @Package com.zk.db.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 10, 2020 3:45:28 PM 
* @version V1.0 
*/
package com.zk.db.entity;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/**
 * @ClassName: ZKDBEntity
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public abstract class ZKDBEntity<E extends ZKDBEntity<E>> implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    // 额外参数集合; 用于传输额外的查询参数，自定义SQL（SQL标识，SQL内容）或返回额外的关联参数；
    @Transient
    @XmlTransient
    @JsonIgnore
    protected Map<String, Object> extraParams;

    @Transient
    protected ZKPage<E> page;

    /**
     * @return page
     */
    @JsonIgnore
    @XmlTransient
    public ZKPage<E> getPage() {
        return page;
    }

    /**
     * @param page
     *            the page to set
     */
    public void setPage(ZKPage<E> page) {
        this.page = page;
    }

    /**
     * 额外参数集合
     * 
     * @return extraParams
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public Map<String, Object> getExtraParams() {
        if (extraParams == null) {
            extraParams = Maps.newHashMap();
        }
        return extraParams;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    @XmlTransient
    @Transient
    public <T> T getParamByName(String paramName) {
        return (T) this.getExtraParams().get(paramName);
    }

    /**
     * 获取数据库名称
     */
    @Transient
    @XmlTransient
    @JsonIgnore
    public String getJdbcType() {
        return ZKEnvironmentUtils.getString("jdbc.type", "mysql");
    }

    /********************************************************/
    /*** 转换实体到持久层信息的一些实体 **/
    /********************************************************/
    @Transient
    @XmlTransient
    @JsonIgnore
    public abstract ZKDBSqlHelper getSqlHelper();

//    @Transient
//    @JsonIgnore
//    @XmlTransient
//    protected final ZKSqlConvert getSqlConvert() {
//        return this.getSqlHelper().getSqlConvert();
//    }

//    @Transient
//    @JsonIgnore
//    @XmlTransient
//    protected final ZKDBAnnotationProvider getAnnotationProvider() {
//        return this.getSqlProvider().getAnnotationProvider();
//    }

//    @Transient
//    @JsonIgnore
//    @XmlTransient
//    protected final ZKTable getTable() {
//        return this.getSqlProvider().getAnnotationProvider().getTable();
//    }

    /********************************************************/
    /*** 提供的一些 sql 自定义重载函数 **************************/
    /********************************************************/

    /**
     * 取自定义 逻辑删除语句 中 set 内容；抽象方法；为方便定制
     * 
     * 示例："c_del_flag = #{delFlag}, c_update_user_id = #{updateUserId}, c_update_date = #{updateDate}";
     *
     * @Title: getDelSetSql
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 14, 2020 6:50:21 PM
     * @return
     * @return String
     */
    @Transient
    @JsonIgnore
    @XmlTransient
    public abstract String getZKDbDelSetSql();

    /**
     * 方便在实体中自定义修改 where 会把系统生成的 where 做为参数传入；
     * 
     * 可能重写来实现自定查询条件组合
     *
     * @Title: getZKDbWhere
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:46:20 AM
     * @param sqlConvert
     * @param mapInfo
     * @return
     * @return ZKDBQueryWhere
     */
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        return sqlConvert.resolveQueryCondition(mapInfo);
    }

}
