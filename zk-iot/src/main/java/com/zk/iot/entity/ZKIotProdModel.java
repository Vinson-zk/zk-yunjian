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
 * 产品型号
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_iot_prod_model", alias = "iotProdModel", orderBy = " c_create_date ASC ")
public class ZKIotProdModel extends ZKBaseEntity<String, ZKIotProdModel> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKIotProdModel());
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
    }
	
	/**
	 * 集团代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String groupCode;	
	/**
	 * 公司ID 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_id", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyId;
	/**
	 * 公司代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String companyCode;	
    /**
     * 产品ID
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_prod_id", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String prodId;
    /**
     * 产品代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_prod_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String prodCode;
    /**
     * 产品型号名称
     */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_prod_model_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson prodModelName;	
	/**
	 * 产品型号代码，全表唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_prod_model_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodModelCode;	
	/**
	 * 产品型号说明 json
	 */
	@ZKColumn(name = "c_prod_model_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson prodModelDesc;	
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
	/**
	 * 展示图标
	 */
	@Length(min = 0, max = 256, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_icon", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String icon;	
	/**
	 * 数据处理协议ID
	 */
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_business_data_protocol_id", isInsert = true, javaType = Integer.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String businessDataProtocolId;

	/**
	 * 业务数据处理协议代码
	 */
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_business_data_protocol_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String businessDataProtocolCode;	
	
    // 产品
    @Transient
    ZKIotProd prod;

	public ZKIotProdModel() {
		super();
	}

    public ZKIotProdModel(String pkId) {
		super(pkId);
	}
	
	/**
	 * 集团代码	
	 */	
	public String getGroupCode() {
		return groupCode;
	}
	
	/**
	 * 集团代码
	 */	
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	/**
	 * 公司ID 	
	 */	
    public String getCompanyId() {
		return companyId;
	}
	
	/**
	 * 公司ID 
	 */	
    public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	/**
	 * 公司代码	
	 */	
	public String getCompanyCode() {
		return companyCode;
	}
	
	/**
	 * 公司代码
	 */	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

    /**
     * @return prodId sa
     */
    public String getProdId() {
        return prodId;
    }

    /**
     * @param prodId
     *            the prodId to set
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    /**
     * @return prodCode sa
     */
    public String getProdCode() {
        return prodCode;
    }

    /**
     * @param prodCode
     *            the prodCode to set
     */
    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    /**
     * 产品型号名称
     */	
	public ZKJson getProdModelName() {
		return prodModelName;
	}
	
	/**
	 * 产品型号名称
	 */	
	public void setProdModelName(ZKJson prodModelName) {
		this.prodModelName = prodModelName;
	}
	/**
	 * 产品型号代码，全表唯一	
	 */	
	public String getProdModelCode() {
		return prodModelCode;
	}
	
	/**
	 * 产品型号代码，全表唯一
	 */	
	public void setProdModelCode(String prodModelCode) {
		this.prodModelCode = prodModelCode;
	}
	/**
	 * 产品型号说明 json	
	 */	
	public ZKJson getProdModelDesc() {
		return prodModelDesc;
	}
	
	/**
	 * 产品型号说明 json
	 */	
	public void setProdModelDesc(ZKJson prodModelDesc) {
		this.prodModelDesc = prodModelDesc;
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
	/**
	 * 展示图标	
	 */	
	public String getIcon() {
		return icon;
	}
	
	/**
	 * 展示图标
	 */	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * 数据处理协议ID	
	 */	
    public String getBusinessDataProtocolId() {
		return businessDataProtocolId;
	}
	
	/**
	 * 数据处理协议ID
	 */	
    public void setBusinessDataProtocolId(String businessDataProtocolId) {
		this.businessDataProtocolId = businessDataProtocolId;
	}
	/**
	 * 业务数据处理协议代码	
	 */	
	public String getBusinessDataProtocolCode() {
		return businessDataProtocolCode;
	}
	
	/**
	 * 业务数据处理协议代码
	 */	
	public void setBusinessDataProtocolCode(String businessDataProtocolCode) {
		this.businessDataProtocolCode = businessDataProtocolCode;
	}

    /**
     * @return prod sa
     */
    public ZKIotProd getProd() {
        return prod;
    }

    /**
     * @param prod
     *            the prod to set
     */
    public void setProd(ZKIotProd prod) {
        this.prod = prod;
    }
	
	
}


