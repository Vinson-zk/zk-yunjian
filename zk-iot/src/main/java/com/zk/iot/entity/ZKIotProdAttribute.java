/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.entity;

import java.math.BigInteger;

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
 * 产品、产品型号、产品实例的属性
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_iot_prod_attribute", alias = "iotProdAttribute", orderBy = " c_create_date ASC ")
public class ZKIotProdAttribute extends ZKBaseEntity<String, ZKIotProdAttribute> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKIotProdAttribute());
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
        // 属性归属，1-产品；2-型号；3-产品实例；
        public static interface AttrFrom {
            /**
             * 1-产品；
             */
            public static final int prod = 1;
            /**
             * 2-型号；
             */
            public static final int prodModel = 2;
            /**
             * 3-产品实例；
             */
            public static final int prodInstance = 3;
        }
        // 属性类型；0-标签；1-属性；2-事件；3-功能；
        public static interface AttrType {
            /**
             * 0-标签；
             */
            public static final int label = 0;
            /**
             * 1-属性；
             */
            public static final int attr = 1;
            /**
             * 2-事件；
             */
            public static final int event = 2;
            /**
             * 3-功能；
             */
            public static final int func = 3;
        }
    }
	
	/**
	 * 属性归属，1-产品；2-型号；3-产品实例；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_attr_from", isInsert = true, javaType = Integer.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer attrFrom;	
	/**
	 * 属性归属目标ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_target_id", isInsert = true, javaType = Integer.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String targetId;
	/**
	 * 属性类型；0-标签；1-属性；2-事件；3-功能；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_attr_type", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer attrType;	
	/**
	 * 属性名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_attr_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	ZKJson attrName;	
    /**
     * 属性代码；归属分类+目标ID+下唯一
     */
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_attr_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String attrCode;	
    /**
     * JSON 数组，对象格式为：
     * {
     *      label: JSON 名称, 
     *      code: 标识代码, 
     *      type: 1-字符串、2-整型、3-浮点型、4-时间、5-json、6-数组、7-布尔型，
     *      value: 值, required: 0-非必须；1-必须；
     *      limit: 取值范围, 
     *      sort: 排序,
     *      desc: 说明
     * }
     */
	@ZKColumn(name = "c_attr_data", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson attrData;	
	/**
	 * 属性说明
	 */
	@ZKColumn(name = "c_attr_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson attrDesc;	
	/**
	 * 排序
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_sort", isInsert = true, javaType = BigInteger.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer sort;
	/**
	 * 状态；0-正常；1-禁用；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_status", isInsert = true, javaType = BigInteger.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer status;
	
	public ZKIotProdAttribute() {
		super();
	}

    public ZKIotProdAttribute(String pkId) {
		super(pkId);
	}
	
	/**
	 * 属性归属，1-产品；2-型号；3-产品实例；	
	 */	
	public Integer getAttrFrom() {
		return attrFrom;
	}
	
	/**
	 * 属性归属，1-产品；2-型号；3-产品实例；
	 */	
	public void setAttrFrom(Integer attrFrom) {
		this.attrFrom = attrFrom;
	}
	/**
	 * 属性归属目标ID	
	 */	
    public String getTargetId() {
		return targetId;
	}
	
	/**
	 * 属性归属目标ID
	 */	
    public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	/**
	 * 属性类型；0-标签；1-属性；2-事件；3-功能；	
	 */	
	public Integer getAttrType() {
		return attrType;
	}
	
	/**
	 * 属性类型；0-标签；1-属性；2-事件；3-功能；
	 */	
	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
	/**
	 * 属性名称	
	 */	
	public ZKJson getAttrName() {
		return attrName;
	}
	
	/**
	 * 属性名称
	 */	
	public void setAttrName(ZKJson attrName) {
		this.attrName = attrName;
	}
	/**
	 * 属性代码，唯一	
	 */	
	public String getAttrCode() {
		return attrCode;
	}
	
	/**
	 * 属性代码，唯一
	 */	
	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}
	/**
	 * JSON 数组，对象格式为：{\n    label: JSON 名称,\n    code: 标识代码,\n    type: 1-字符串、2-整型、3-浮点型、\n        4-时间、5-json、6-数组、7-布尔型，\n    value: 值,\n    required: 0-非必须；1-必须；\n    limit: 取值范围,\n    sort: 排序,\n}	
	 */	
	public ZKJson getAttrData() {
		return attrData;
	}
	
	/**
	 * JSON 数组，对象格式为：{\n    label: JSON 名称,\n    code: 标识代码,\n    type: 1-字符串、2-整型、3-浮点型、\n        4-时间、5-json、6-数组、7-布尔型，\n    value: 值,\n    required: 0-非必须；1-必须；\n    limit: 取值范围,\n    sort: 排序,\n}
	 */	
	public void setAttrData(ZKJson attrData) {
		this.attrData = attrData;
	}
	/**
	 * 属性说明	
	 */	
	public ZKJson getAttrDesc() {
		return attrDesc;
	}
	
	/**
	 * 属性说明
	 */	
	public void setAttrDesc(ZKJson attrDesc) {
		this.attrDesc = attrDesc;
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


