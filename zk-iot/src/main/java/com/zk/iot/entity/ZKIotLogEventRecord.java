/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.entity;

import org.hibernate.validator.constraints.Length;
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

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 事件日志记录
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_iot_log_event_record", alias = "iotLogEventRecord", orderBy = " c_create_date ASC ")
public class ZKIotLogEventRecord extends ZKBaseEntity<String, ZKIotLogEventRecord> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKIotLogEventRecord());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
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
	@ZKColumn(name = "c_prod_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodCode;	
	/**
	 * 产品型号ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_prod_model_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodModelId;	
	/**
	 * 产品型号代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_prod_model_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodModelCode;	
	/**
	 * 产品实例ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_prod_instance_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodInstanceId;	
	/**
	 * 产品实例代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_prod_instance_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodInstanceCode;	
	/**
	 * 事件ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_event_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String eventId;	
	/**
	 * 事件代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_event_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String eventCode;	
	/**
	 * 日志数据
	 */
	@ZKColumn(name = "c_data", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson data;	
	
	public ZKIotLogEventRecord() {
		super();
	}

    public ZKIotLogEventRecord(String pkId) {
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
	 * 产品型号ID	
	 */	
	public String getProdModelId() {
		return prodModelId;
	}
	
	/**
	 * 产品型号ID
	 */	
	public void setProdModelId(String prodModelId) {
		this.prodModelId = prodModelId;
	}
	/**
	 * 产品型号代码	
	 */	
	public String getProdModelCode() {
		return prodModelCode;
	}
	
	/**
	 * 产品型号代码
	 */	
	public void setProdModelCode(String prodModelCode) {
		this.prodModelCode = prodModelCode;
	}
	/**
	 * 产品实例ID	
	 */	
	public String getProdInstanceId() {
		return prodInstanceId;
	}
	
	/**
	 * 产品实例ID
	 */	
	public void setProdInstanceId(String prodInstanceId) {
		this.prodInstanceId = prodInstanceId;
	}
	/**
	 * 产品实例代码	
	 */	
	public String getProdInstanceCode() {
		return prodInstanceCode;
	}
	
	/**
	 * 产品实例代码
	 */	
	public void setProdInstanceCode(String prodInstanceCode) {
		this.prodInstanceCode = prodInstanceCode;
	}
	/**
	 * 事件ID	
	 */	
	public String getEventId() {
		return eventId;
	}
	
	/**
	 * 事件ID
	 */	
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * 事件代码	
	 */	
	public String getEventCode() {
		return eventCode;
	}
	
	/**
	 * 事件代码
	 */	
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	/**
	 * 日志数据	
	 */	
	public ZKJson getData() {
		return data;
	}
	
	/**
	 * 日志数据
	 */	
	public void setData(ZKJson data) {
		this.data = data;
	}
	
	
}