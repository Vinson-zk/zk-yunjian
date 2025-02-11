/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 业务数据处理协议
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_iot_prod_business_data_protocol", alias = "iotProdBusinessDataProtocol", orderBy = " c_create_date ASC ")
public class ZKIotProdBusinessDataProtocol extends ZKBaseEntity<String, ZKIotProdBusinessDataProtocol> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKIotProdBusinessDataProtocol());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;

    public static interface ValueKey {
        // 状态：0-正常；1-禁用；
        public static interface Status {
            /**
             * 0-正常
             */
            public static final int normal = 0;

            /**
             * 1-禁用
             */
            public static final int disabled = 1;
        }

        // 状态：0-jar；
        public static interface Type {
            /**
             * 0-正常
             */
            public static final int jar = 0;
        }

    }
	
	/**
	 * 协议代码，全表唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_protocol_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String protocolCode;	
	/**
	 * 协议类型，0-jar;
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_protocol_type", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer protocolType;	
	/**
	 * 注入bean 名称
	 */
    @NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_bean_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String beanName;	
	/**
	 * 协议名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_protocol_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson protocolName;	
	/**
	 * 排序
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_sort", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer sort;	
	/**
	 * 状态；0-正常；1-禁用；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status", isInsert = true, javaType = Integer.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer status;	
	
	public ZKIotProdBusinessDataProtocol() {
		super();
	}

    public ZKIotProdBusinessDataProtocol(String pkId) {
		super(pkId);
	}
	
	/**
	 * 协议代码，全表唯一	
	 */	
	public String getProtocolCode() {
		return protocolCode;
	}
	
	/**
	 * 协议代码，全表唯一
	 */	
	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}
	/**
	 * 协议类型，0-jar;	
	 */	
	public Integer getProtocolType() {
		return protocolType;
	}
	
	/**
	 * 协议类型，0-jar;
	 */	
	public void setProtocolType(Integer protocolType) {
		this.protocolType = protocolType;
	}
	/**
	 * 注入bean 名称	
	 */	
	public String getBeanName() {
		return beanName;
	}
	
	/**
	 * 注入bean 名称
	 */	
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	/**
	 * 协议名称	
	 */	
	public ZKJson getProtocolName() {
		return protocolName;
	}
	
	/**
	 * 协议名称
	 */	
	public void setProtocolName(ZKJson protocolName) {
		this.protocolName = protocolName;
	}
	/**
	 * 排序	
	 */	
	public Integer getSort() {
		return sort;
	}
	
	/**
	 * 排序
	 */	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	/**
	 * 状态；0-正常；1-禁用；	
	 */	
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * 状态；0-正常；1-禁用；
	 */	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}