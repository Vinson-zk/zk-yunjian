/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBQueryScript;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 产品实例，设备实例
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_iot_prod_instance", alias = "iotProdInstance", orderBy = " c_create_date ASC ")
public class ZKIotProdInstance extends ZKBaseEntity<String, ZKIotProdInstance> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKIotProdInstance());
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
	@ZKColumn(name = "c_company_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
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
	@ZKColumn(name = "c_prod_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodId;	
	/**
	 * 产品代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_prod_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String prodCode;	
	/**
	 * 型号ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_prod_model_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodModelId;	
	/**
	 * 型号代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_prod_model_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String prodModelCode;	
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
	 * 实例名称，默认带入型号名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	ZKJson name;	
    /**
     * 实例序列号，型号+下唯一
     */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_sn_num", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String snNum;	
    /**
     * 实例mac地址，全表唯一；
     */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_mac_addr", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String macAddr;	
	/**
	 * IP地址
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_ip_addr", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String ipAddr;	
	/**
	 * 端口
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_port", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String port;	
	/**
	 * 最后一次心跳时间
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_last_in_time", isInsert = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Date lastInTime;	
	
    // 产品型号
    @Transient
    ZKIotProdModel prodModel;

	public ZKIotProdInstance() {
		super();
	}

    public ZKIotProdInstance(String pkId) {
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
	 * 产品ID	
	 */	
	public String getProdId() {
		return prodId;
	}
	
	/**
	 * 产品ID
	 */	
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	/**
	 * 产品代码	
	 */	
	public String getProdCode() {
		return prodCode;
	}
	
	/**
	 * 产品代码
	 */	
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	/**
	 * 型号ID	
	 */	
	public String getProdModelId() {
		return prodModelId;
	}
	
	/**
	 * 型号ID
	 */	
	public void setProdModelId(String prodModelId) {
		this.prodModelId = prodModelId;
	}
	/**
	 * 型号代码	
	 */	
	public String getProdModelCode() {
		return prodModelCode;
	}
	
	/**
	 * 型号代码
	 */	
	public void setProdModelCode(String prodModelCode) {
		this.prodModelCode = prodModelCode;
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
	 * 实例名称，默认带入型号名称	
	 */	
	public ZKJson getName() {
		return name;
	}
	
	/**
	 * 实例名称，默认带入型号名称
	 */	
	public void setName(ZKJson name) {
		this.name = name;
	}
	/**
	 * 实例序列号，全表唯一	
	 */	
	public String getSnNum() {
		return snNum;
	}
	
	/**
	 * 实例序列号，全表唯一
	 */	
	public void setSnNum(String snNum) {
		this.snNum = snNum;
	}
	/**
	 * 实例mac地址	
	 */	
	public String getMacAddr() {
		return macAddr;
	}
	
	/**
	 * 实例mac地址
	 */	
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	/**
	 * IP地址	
	 */	
	public String getIpAddr() {
		return ipAddr;
	}
	
	/**
	 * IP地址
	 */	
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	/**
	 * 端口	
	 */	
	public String getPort() {
		return port;
	}
	
	/**
	 * 端口
	 */	
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * 最后一次心跳时间	
	 */	
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
	public Date getLastInTime() {
		return this.lastInTime;
	}
	
	/**
	 * 最后一次心跳时间
	 */	
	public void setLastInTime(Date lastInTime) {
		this.lastInTime = lastInTime;
	}

    /**
     * @return prodModel sa
     */
    public ZKIotProdModel getProdModel() {
        return prodModel;
    }

    /**
     * @param prodModel
     *            the prodModel to set
     */
    public void setProdModel(ZKIotProdModel prodModel) {
        this.prodModel = prodModel;
    }

    // ===========================================================================
    // 查询是否在线辅助
    @Transient
    @JsonIgnore
    @XmlTransient
    Boolean isIn;
	
    /**
     * 是否在线
     * 
     * @return boolean true-在线；false-离线；
     */
    public boolean isIn() {
        if (this.getLastInTime() != null) {
            long renewalIntervalInSeconds = ZKEnvironmentUtils.getLong("zk.iot.prod.instance.renewal.interval.in.seconds", 0L);
            long rTime = System.currentTimeMillis() - this.getLastInTime().getTime();
            return rTime <= renewalIntervalInSeconds;
        }
        return false;
    }

    public void setIsIn(Boolean isIn) {
        this.isIn = isIn;
    }

    @Transient
    @JsonIgnore
    @XmlTransient
    public boolean getIsIn() {
        return this.isIn == null ? false : this.isIn.booleanValue();
    }

    @Transient
    @JsonIgnore
    @XmlTransient
    public Date getEndInTime() {
        if (this.getIsIn()) {
            long renewalIntervalInSeconds = ZKEnvironmentUtils
                    .getLong("zk.iot.prod.instance.renewal.interval.in.seconds", 0L);
            return ZKDateUtils.parseDate(System.currentTimeMillis() - renewalIntervalInSeconds);
        }
        return null;
    }

    // 取 where 条件；实体定义可以定制；在 生成的 sql；注意：末尾加空格
    @Override
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo);
        // 制作一个根据名称和代码同时查询的 查询条件，用过度 java 属性 searchValue 为传参数值
        ZKDBQueryCol queryCol = ZKDBQueryCol.as(ZKDBOptComparison.GTE, "c_last_in_time", "endInTime",
                Date.class, null, false);
        where.put(ZKDBQueryScript.asIf(queryCol, 2, "endInTime", Date.class));
        return where;
    }
}


