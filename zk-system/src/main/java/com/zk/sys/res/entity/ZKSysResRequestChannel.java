/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 请求渠道
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_sys_res_request_channel", alias = "sysResRequestChannel", orderBy = " c_create_date ASC ")
public class ZKSysResRequestChannel extends ZKBaseEntity<String, ZKSysResRequestChannel> {

    static ZKDBSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysResRequestChannel());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 渠道名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson name;
    /**
     * 渠道代码，唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String code;
    /**
     * 渠道说明
     */
    @ZKColumn(name = "c_channel_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson channelDesc;

    public ZKSysResRequestChannel() {
        super();
    }

    public ZKSysResRequestChannel(String pkId) {
        super(pkId);
    }

    /**
     * 渠道名称
     */
    public ZKJson getName() {
        return name;
    }

    /**
     * 渠道名称
     */
    public void setName(ZKJson name) {
        this.name = name;
    }

    /**
     * 渠道代码，唯一
     */
    public String getCode() {
        return code;
    }

    /**
     * 渠道代码，唯一
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 渠道说明
     */
    public ZKJson getChannelDesc() {
        return channelDesc;
    }

    /**
     * 渠道说明
     */
    public void setChannelDesc(ZKJson channelDesc) {
        this.channelDesc = channelDesc;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
	@JsonIgnore
	public String genId() {
        return ZKIdUtils.genLongStringId();
    }

}