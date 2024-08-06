/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.settings.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.core.utils.ZKUtils;
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
import jakarta.validation.constraints.Pattern;

/**
 * 应用系统配置项条目
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_sys_set_item", alias = "sysSetItem", orderBy = " c_create_date ASC ")
public class ZKSysSetItem extends ZKBaseEntity<String, ZKSysSetItem> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysSetItem());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 配置值类型： 0-字符串；1-boolean 布尔值；2-整数；3-字符数组；4-数字数组；
     * 
     * @ClassName: Key_ValueType
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface Key_ValueType {
        /**
         * 0-字符串；
         */
        public static final int vString = 0;

        /**
         * 1-boolean 布尔值；
         */
        public static final int vBoolean = 1;

        /**
         * 2-整数；
         */
        public static final int vInt = 2;

        /**
         * 3-字符数组；暂不支持
         */
        public static final int vStringArray = 3;

        /**
         * 4-数字数组；暂不支持
         */
        public static final int vIntArray = 4;

    }

    /**
     * 配置项组别ID
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_collection_id", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String collectionId;

    /**
     * 配置项组别代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_collection_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String collectionCode;

    /**
     * 配置项类型：0-平台配置；1-通用配置；2-公司专属配置；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_type", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer type;
    /**
     * 配置项名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson name;

    /**
     * 配置项代码；组别下全表唯一；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String code;
    /**
     * 配置项说明
     */
    @ZKColumn(name = "c_set_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson setDesc;

    /**
     * 配置值类型：0-字符串；1-boolean 布尔值；2-整数；3-字符数组；4-数字数组；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_value_type", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    Integer valueType;

    /**
     * 配置值：通用配置类型时，公司可以自定义配置值
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 256, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_value", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String value;

    /**
     * 集团代码：仅公司专属配置类型时，有值
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String groupCode;

    /**
     * 公司代码：仅公司专属配置类型时，有值
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String companyCode;

    public ZKSysSetItem() {
        super();
    }

    public ZKSysSetItem(String pkId) {
        super(pkId);
    }

    /**
     * 配置项类型：0-平台配置；1-通用配置；2-公司专属配置；
     */
    public Integer getType() {
        return type;
    }

    /**
     * 配置项类型：0-平台配置；1-通用配置；2-公司专属配置；
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 配置项名称
     */
    public ZKJson getName() {
        return name;
    }

    /**
     * 配置项名称
     */
    public void setName(ZKJson name) {
        this.name = name;
    }

    /**
     * 配置项代码；组别下全表唯一；
     */
    public String getCode() {
        return code;
    }

    /**
     * 配置项代码；组别下全表唯一；
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 配置项说明
     */
    public ZKJson getSetDesc() {
        return setDesc;
    }

    /**
     * 配置项说明
     */
    public void setSetDesc(ZKJson setDesc) {
        this.setDesc = setDesc;
    }

    /**
     * @return valueType sa
     */
    public Integer getValueType() {
        return valueType;
    }

    /**
     * @param valueType
     *            the valueType to set
     */
    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    /**
     * 配置值：通用配置类型时，公司可以自定义配置值
     */
    public String getValue() {
        return value;
    }

    /**
     * 配置值：通用配置类型时，公司可以自定义配置值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 集团代码：仅公司专属配置类型时，有值
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * 集团代码：仅公司专属配置类型时，有值
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * 公司代码：仅公司专属配置类型时，有值
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 公司代码：仅公司专属配置类型时，有值
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
    protected String genId() {
        return ZKIdUtils.genLongStringId();
    }

    /**
     * 配置项组别ID
     */
    public String getCollectionId() {
        return collectionId;
    }

    /**
     * 配置项组别ID
     */
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    /**
     * 配置项组别代码
     */
    public String getCollectionCode() {
        return collectionCode;
    }

    /**
     * 配置项组别代码
     */
    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    String searchValue;

    /**
     * @return searchValue sa
     */
    public String getSearchValue() {
        return searchValue;
    }

    /**
     * @param searchValue
     *            the searchValue to set
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    // 取 where 条件；实体定义可以定制；在 生成的 sql；注意：末尾加空格
    @Override
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo);
        // 制作一个根据名称和代码同时查询的 查询条件，用过度 java 属性 searchValue 为传参数值
        ZKDBQueryWhere sWhere = ZKDBQueryWhere.asOr("(", ")",
            ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_name", "searchValue", String.class, null, false),
            ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_code", "searchValue", String.class, null, false));

        where.put(ZKDBQueryScript.asIf(sWhere, 0, "searchValue", String.class));
        return where;
    }

    @Transient
    @JsonIgnore
    @XmlTransient
    public boolean getBooleanValue() {
        if (this.getValueType() == Key_ValueType.vBoolean) {
            return ZKUtils.isTrue(this.getValue());
        }
        log.error("[>_<:20220511-1658-001] 配置项目值类型与要求不匹配；要求为：【{}】，实际为：【{}】", Key_ValueType.vInt, this.getValueType());
        return false;
    }

    @Transient
    @JsonIgnore
    @XmlTransient
    public String getStringValue() {
        return this.getValue();
    }

    @Transient
    @JsonIgnore
    @XmlTransient
    public int getIntValue() {
        if (this.getValueType() == Key_ValueType.vInt) {
            if (this.getValue() == null) {
                return 0;
            } else {
                return Integer.valueOf(this.getValue());
            }
        }
        log.error("[>_<:20220511-1658-002] 配置项目值类型与要求不匹配；要求为：【{}】，实际为：【{}】", Key_ValueType.vInt, this.getValueType());
        return -1;
    }
}
