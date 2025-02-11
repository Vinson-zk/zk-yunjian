/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 产品场景分类
 * @author 
 * @version 
 */
@ZKTable(name = "t_iot_prod_categores", alias = "iotProdCategores", orderBy = " c_create_date ASC ")
public class ZKIotProdCategores extends ZKBaseTreeEntity<String, ZKIotProdCategores> {
	
	static ZKTreeSqlHelper sqlHelper;

	@Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKTreeSqlHelper getTreeSqlHelper() {
        return sqlHelper();
    }

	@Transient
    @XmlTransient
    @JsonIgnore
    public static ZKTreeSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKIotProdCategores());
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
	 * 场景类型名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_prod_categores_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	ZKJson prodCategoresName;	
	/**
	 * 场景分类代码，全表唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_prod_categores_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String prodCategoresCode;	
	/**
	 * 场景说明 json
	 */
    @ZKColumn(name = "c_prod_categores_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	ZKJson prodCategoresDesc;	
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
	
	public ZKIotProdCategores() {
		super();
	}

    public ZKIotProdCategores(String pkId) {
		super(pkId);
	}
	
	/**
	 * 场景类型名称	
	 */	
	public ZKJson getProdCategoresName() {
		return this.prodCategoresName;
	}
	
	/**
	 * 场景类型名称
	 */	
	public void setProdCategoresName(ZKJson prodCategoresName) {
		this.prodCategoresName = prodCategoresName;
	}
	/**
	 * 场景分类代码，全表唯一	
	 */	
	public String getProdCategoresCode() {
		return this.prodCategoresCode;
	}
	
	/**
	 * 场景分类代码，全表唯一
	 */	
	public void setProdCategoresCode(String prodCategoresCode) {
		this.prodCategoresCode = prodCategoresCode;
	}
	/**
	 * 场景说明 json	
	 */	
	public ZKJson getProdCategoresDesc() {
		return this.prodCategoresDesc;
	}
	
	/**
	 * 场景说明 json
	 */	
	public void setProdCategoresDesc(ZKJson prodCategoresDesc) {
		this.prodCategoresDesc = prodCategoresDesc;
	}
	/**
	 * 排序	
	 */	
	public Integer getSort() {
		return this.sort;
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
		return this.status;
	}
	
	/**
	 * 状态；0-正常；1-禁用；
	 */	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}