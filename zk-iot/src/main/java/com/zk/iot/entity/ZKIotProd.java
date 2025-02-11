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
 * 产品
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_iot_prod", alias = "iotProd", orderBy = " c_create_date ASC ")
public class ZKIotProd extends ZKBaseEntity<String, ZKIotProd> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKIotProd());
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
	 * 产品名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_prod_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson prodName;	
	/**
	 * 产品代码，全表唯一
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_prod_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String prodCode;	
	/**
	 * 产品说明 json
	 */
	@ZKColumn(name = "c_prod_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson prodDesc;	
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
	 * 产品品牌ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_brand_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String brandId;	
	/**
	 * 产品品牌代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_brand_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String brandCode;	
	/**
	 * 产品场景分类ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_cate_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String cateId;	
	/**
	 * 产品场景分类代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_cate_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String cateCode;	
	
    // 场景实体
    @Transient
    ZKIotProdCategores prodCate;

    // 产品品牌
    @Transient
    ZKIotProdBrand prodBrand;

	public ZKIotProd() {
		super();
	}

    public ZKIotProd(String pkId) {
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
	 * 产品名称	
	 */	
	public ZKJson getProdName() {
		return prodName;
	}
	
	/**
	 * 产品名称
	 */	
	public void setProdName(ZKJson prodName) {
		this.prodName = prodName;
	}
	/**
	 * 产品代码，全表唯一	
	 */	
	public String getProdCode() {
		return prodCode;
	}
	
	/**
	 * 产品代码，全表唯一
	 */	
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	/**
	 * 产品说明 json	
	 */	
	public ZKJson getProdDesc() {
		return prodDesc;
	}
	
	/**
	 * 产品说明 json
	 */	
	public void setProdDesc(ZKJson prodDesc) {
		this.prodDesc = prodDesc;
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
	 * 产品品牌ID	
	 */	
	public String getBrandId() {
		return brandId;
	}
	
	/**
	 * 产品品牌ID
	 */	
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	/**
	 * 产品品牌代码	
	 */	
	public String getBrandCode() {
		return brandCode;
	}
	
	/**
	 * 产品品牌代码
	 */	
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	/**
	 * 产品场景分类ID	
	 */	
	public String getCateId() {
		return cateId;
	}
	
	/**
	 * 产品场景分类ID
	 */	
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}
	/**
	 * 产品场景分类代码	
	 */	
	public String getCateCode() {
		return cateCode;
	}
	
	/**
	 * 产品场景分类代码
	 */	
	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}

    /**
     * @return prodCate sa
     */
    public ZKIotProdCategores getProdCate() {
        return prodCate;
    }

    /**
     * @param prodCate
     *            the prodCate to set
     */
    public void setProdCate(ZKIotProdCategores prodCate) {
        this.prodCate = prodCate;
    }

    /**
     * @return prodBrand sa
     */
    public ZKIotProdBrand getProdBrand() {
        return prodBrand;
    }

    /**
     * @param prodBrand
     *            the prodBrand to set
     */
    public void setProdBrand(ZKIotProdBrand prodBrand) {
        this.prodBrand = prodBrand;
    }
	
}


