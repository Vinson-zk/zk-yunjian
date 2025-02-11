/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.platformBusiness.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * 微信平台，功能 key 配置；网页授权成功后，会根据这个 key 重定向到业务功能；
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_wx_pb_func_key_config", alias = "funcKeyConfig", orderBy = " c_create_date ASC ")
public class ZKFuncKeyConfig extends ZKBaseEntity<String, ZKFuncKeyConfig> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKFuncKeyConfig());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;

    /**
     * 状态；0-启用；1-禁用；
     */
    public static interface KeyStatus {
        /**
         * 0-启用
         */
        public static final int normal = 0;

        /**
         * 1-禁用
         */
        public static final int disabled = 1;
    }
	
	/**
	 * 功能类型id；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_func_type_id", isInsert = true, javaType = String.class)
	String funcTypeId;	
	/**
	 * 功能类型代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_func_type_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String funcTypeCode;	

    /**
     * 功能名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_func_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson funcName;

	/**
	 * 功能标识代码 key；全表唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_func_key", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String funcKey;

    /**
     * 状态；0-启用；1-禁用；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer status;

	/**
	 * 重写向，源真实地址；从域名根路径下开始写
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 256, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_redirect_original_url", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String redirectOriginalUrl;	
	/**
	 * 重写向，真实地址；从域名根路径下开始写
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 256, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_redirect_proxy_url", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String redirectProxyUrl;	
	/**
	 * 功能说明
	 */
	@ZKColumn(name = "c_func_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
	ZKJson funcDesc;	
	
	public ZKFuncKeyConfig() {
		super();
	}

	public ZKFuncKeyConfig(String pkId){
		super(pkId);
	}
	
	/**
	 * 功能类型id；	
	 */	
	public String getFuncTypeId() {
		return funcTypeId;
	}
	
	/**
	 * 功能类型id；
	 */	
	public void setFuncTypeId(String funcTypeId) {
		this.funcTypeId = funcTypeId;
	}
	/**
	 * 功能类型代码	
	 */	
	public String getFuncTypeCode() {
		return funcTypeCode;
	}
	
	/**
	 * 功能类型代码
	 */	
	public void setFuncTypeCode(String funcTypeCode) {
		this.funcTypeCode = funcTypeCode;
	}

    /**
     * @return funcName sa
     */
    public ZKJson getFuncName() {
        return funcName;
    }

    /**
     * @param funcName
     *            the funcName to set
     */
    public void setFuncName(ZKJson funcName) {
        this.funcName = funcName;
    }

    /**
     * 功能标识代码 key；全表唯一
     */	
	public String getFuncKey() {
        return this.funcKey;
	}
	
    /**
     * @return status sa
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 功能标识代码 key；全表唯一
     */	
    public void setFuncKey(String funcKey) {
        this.funcKey = funcKey;
	}
	/**
	 * 重写向，源真实地址；从域名根路径下开始写	
	 */	
	public String getRedirectOriginalUrl() {
		return redirectOriginalUrl;
	}
	
	/**
	 * 重写向，源真实地址；从域名根路径下开始写
	 */	
	public void setRedirectOriginalUrl(String redirectOriginalUrl) {
		this.redirectOriginalUrl = redirectOriginalUrl;
	}
	/**
	 * 重写向，真实地址；从域名根路径下开始写	
	 */	
	public String getRedirectProxyUrl() {
		return redirectProxyUrl;
	}
	
	/**
	 * 重写向，真实地址；从域名根路径下开始写
	 */	
	public void setRedirectProxyUrl(String redirectProxyUrl) {
		this.redirectProxyUrl = redirectProxyUrl;
	}
	/**
	 * 功能说明	
	 */	
	public ZKJson getFuncDesc() {
		return funcDesc;
	}
	
	/**
	 * 功能说明
	 */	
	public void setFuncDesc(ZKJson funcDesc) {
		this.funcDesc = funcDesc;
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