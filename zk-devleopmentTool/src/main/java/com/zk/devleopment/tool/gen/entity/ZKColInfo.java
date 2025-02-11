/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKColInfo.java 
* @author Vinson 
* @Package com.zk.code.generate.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 4:18:10 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;
import com.zk.devleopment.tool.gen.action.ZKConvertUtils;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * @ClassName: ZKColInfo
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKTable(name = "t_dt_code_gen_table_col_info", alias = "colInfo", orderBy = " c_col_sort ASC ")
public class ZKColInfo extends ZKBaseEntity<String, ZKColInfo> {

    static ZKDBSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKColInfo());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKColInfo() {
        super();
    }

    public ZKColInfo(String pkId) {
        super(pkId);
    }

    /**********************************************************************************/
    /*** 表 字段 信息 */
    /**********************************************************************************/

    /**
     * 表: 字段名
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_col_name", query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LIKE))
    String colName;

    /**
     * 表：JDBC类型；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 512, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_col_jdbc_type")
    String colJdbcType;

    /**
     * 表：描述，与JAVA类属性注释共用
     */
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 0, max = 512, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_col_comments", update = @ZKUpdate(true))
    String colComments = "";

    /**
     * 表：是否是主键；计算得出来的；false-不是；true-主键；默认 false;
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_col_is_pk")
    boolean colIsPK = false;

    /**
     * 表：是否可为空；false-不为空；true-可为空；默认 false
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_col_is_null")
    boolean colIsNull = false;

    /**
     * 表：排序字段, 默认为 999999
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999, message = "{zk.core.data.validation.range}")
    @ZKColumn(name = "c_col_sort", update = @ZKUpdate(true))
    int colSort = 999999;

    /**********************************************************************************/
    /*** 转为代码字段信息 */
    /**********************************************************************************/

    /**
     * 类：属性名
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_attr_name", update = @ZKUpdate(true))
    String attrName = "";

    /**
     * 类：属性类型,默认 String
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_attr_type", update = @ZKUpdate(true))
    String attrType = "String";

    /**
     * 判断是否是父类中的字段; false-不是; true-是; 默认：false
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_attr_is_base", update = @ZKUpdate(true))
    boolean attrIsBaseField = false;

    /**
     * 查询方式；UI 页面不作用这个字段判断
     * ""-不是查询字段；EQ-等于；NEQ-不等于；GT-大于；GTE-大于等于；LT-小于；LTE-小于等于；IN-在其中；NIN-不在其中；LIKE-模糊查询；NLIKE；ISNULL-为空；ISNOTNULL-不为空；
     * 默认：null
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_attr_query_type", update = @ZKUpdate(true))
    String attrQueryType;

    /**
     * 是否为插入字段; false-不插入；true-插入字段；默认 true
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_attr_is_insert", update = @ZKUpdate(true))
    boolean attrIsInsert = true;

    /**
     * 是否编辑字段；false-不是编辑字段；true-编辑字段；默认 true;
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_attr_is_update", update = @ZKUpdate(true))
    boolean attrIsUpdate = true;

    /**********************************************************************************/
    /*** 其他 一些代码生成的辅助信息 */
    /**********************************************************************************/
    /**
     * 所属表 id
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 20, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_table_id", query = @ZKQuery(value = true, queryType = ZKDBOptComparison.EQ))
    String tableId;

    /**
     * 属性标签
     */
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_label", update = @ZKUpdate(true))
    String label;

    /**
     * UI 编辑策略；返回值说明，见返枚举类型说明;
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_edit_strategy", update = @ZKUpdate(true))
    String editStrategy = ZKColInfo.KeyEditStrategy.noEdit;

    /**
     * UI 查询策略；返回值说明，见返枚举类型说明；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_search_strategy", update = @ZKUpdate(true))
    String searchStrategy = ZKColInfo.KeySearchStrategy.noSearch;

    /**********************************************************************************/
    /**********************************************************************************/
    /**
     * @return colName sa
     */
    public String getColName() {
        return colName;
    }

    /**
     * @param colName
     *            the colName to set
     */
    public void setColName(String colName) {
        this.colName = colName;
    }

    /**
     * @return colJdbcType sa
     */
    public String getColJdbcType() {
        return colJdbcType;
    }

    /**
     * @param colJdbcType
     *            the colJdbcType to set
     */
    public void setColJdbcType(String colJdbcType) {
        this.colJdbcType = colJdbcType;
    }

    /**
     * @return colComments sa
     */
    public String getColComments() {
        return colComments;
    }

    /**
     * @param colComments
     *            the colComments to set
     */
    public void setColComments(String colComments) {
        this.colComments = colComments;
    }

    /**
     * @return colIsPK sa
     */
    public boolean getColIsPK() {
        return colIsPK;
    }

    /**
     * @param colIsPK
     *            the colIsPK to set
     */
    public void setColIsPK(boolean colIsPK) {
        this.colIsPK = colIsPK;
    }

    /**
     * @return colIsNull sa
     */
    public boolean getColIsNull() {
        return colIsNull;
    }

    /**
     * @param colIsNull
     *            the colIsNull to set
     */
    public void setColIsNull(boolean colIsNull) {
        this.colIsNull = colIsNull;
    }

    /**
     * @return colSort sa
     */
    public int getColSort() {
        return colSort;
    }

    /**
     * @param colSort
     *            the colSort to set
     */
    public void setColSort(int colSort) {
        this.colSort = colSort;
    }

    /**
     * @return attrName sa
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * @param attrName
     *            the attrName to set
     */
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * @return attrType sa
     */
    public String getAttrType() {
        return attrType;
    }

    /**
     * @param attrType
     *            the attrType to set
     */
    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    /**
     * @return attrIsBaseField sa
     */
    public boolean getAttrIsBaseField() {
        return attrIsBaseField;
    }

    /**
     * @param attrIsBaseField
     *            the attrIsBaseField to set
     */
    public void setAttrIsBaseField(boolean attrIsBaseField) {
        this.attrIsBaseField = attrIsBaseField;
    }

    /**
     * @return attrQueryType sa
     */
    public String getAttrQueryType() {
        return attrQueryType;
    }

    /**
     * @param attrQueryType
     *            the attrQueryType to set
     */
    public void setAttrQueryType(String attrQueryType) {
        this.attrQueryType = attrQueryType;
    }

    /**
     * @return attrIsInsert sa
     */
    public boolean getAttrIsInsert() {
        return attrIsInsert;
    }

    /**
     * @param attrIsInsert
     *            the attrIsInsert to set
     */
    public void setAttrIsInsert(boolean attrIsInsert) {
        this.attrIsInsert = attrIsInsert;
    }

    /**
     * @return attrIsUpdate sa
     */
    public boolean getAttrIsUpdate() {
        return attrIsUpdate;
    }

    /**
     * @param attrIsUpdate
     *            the attrIsUpdate to set
     */
    public void setAttrIsUpdate(boolean attrIsUpdate) {
        this.attrIsUpdate = attrIsUpdate;
    }

    /**
     * @return tableName sa
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * @param tableId
     *            the tableName to set
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     * @return label sa
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /************************************/

    /**
     * UI 编辑策略
     * 
     * 不编辑；字符串；整型；符点型；Json；下拉选择；单选；复选；图标编辑；日期；
     * 
     * @ClassName: KeyEditStrategy
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface KeyEditStrategy {
        /**
         * 不编辑；编辑页面不显示；
         */
        public static final String noEdit = "noEdit";

        /**
         * 字符串
         */
        public static final String EditString = "EditString";

        /**
         * 整型
         */
        public static final String EditInt = "EditInt";

        /**
         * 符点型
         */
        public static final String EditFloat = "EditFloat";

        /**
         * Json
         */
        public static final String EditJson = "EditJson";

        /**
         * 下拉选择
         */
        public static final String EditSel = "EditSel";

        /**
         * 单选
         */
        public static final String EditRadio = "EditRadio";

        /**
         * 复选
         */
        public static final String EditCheck = "EditCheck";

        /**
         * 图标编辑
         */
        public static final String EditIcon = "EditIcon";

        /**
         * 日期
         */
        public static final String EditDate = "EditDate";
    }

    /**
     * UI 查询策略
     * 
     * 不查询；文本输入；json; 下拉选择；日期；日期范围；数字；
     * 
     * @ClassName: KeySearchStrategy
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface KeySearchStrategy {
        /**
         * 不编辑
         */
        public static final String noSearch = "noSearch";

        /**
         * 文本输入
         */
        public static final String SearchString = "SearchString";

        /**
         * json
         */
        public static final String SearchJson = "SearchJson";

        /**
         * 下拉选择
         */
        public static final String SearchSel = "SearchSel";

        /**
         * 日期
         */
        public static final String SearchDate = "SearchDate";

        /**
         * 日期范围
         */
        public static final String SearchDateRang = "SearchDateRang";

        /**
         * 数字
         */
        public static final String SearchNum = "SearchNum";
    }

    /**
     * UI 编辑策略
     * 
     * @return editStrategy sa
     */
    public String getEditStrategy() {
        return editStrategy;
    }

    /**
     * @param editStrategy
     *            the editStrategy to set
     */
    public void setEditStrategy(String editStrategy) {
        this.editStrategy = editStrategy;
    }

    /**
     * UI 查询策略
     * 
     * @return searchStrategy sa
     */
    public String getSearchStrategy() {
        return searchStrategy;
    }

    /**
     * @param searchStrategy
     *            the searchStrategy to set
     */
    public void setSearchStrategy(String searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    /**********************************************************************************/
    /*** 计算信息 信息 */
    /**********************************************************************************/
    
    public boolean getAttrTypeStartWith(String str) {
        return this.getAttrType().startsWith(str);
    }

    /**
     * @return colLength sa
     */
    public int[] getColLength() {
        return ZKConvertUtils.convertJdbcLength(this.getColJdbcType());
    }

    public String getMaxValue() {
        if ("String".equals(this.getAttrType())) {
            return String.valueOf(this.getColLength() == null ? 64 : (this.getColLength()[0] / 4));
        }
        else if (this.isInt()) {
            int maxV = 999999999;
            int[] cl = this.getColLength();
            if (cl != null) {
                maxV = cl[0];
                maxV = Double.valueOf(Math.pow(10, maxV)).intValue();
                maxV = maxV - 1;
            }
            return String.valueOf(maxV);
        }
        else if (this.isFloat()) {
            int maxV = 999999999;
            int[] cl = this.getColLength();
            if (cl != null) {
                maxV = cl[0];
                if (cl.length > 1) {
                    maxV = maxV - cl[1];
                }
                maxV = Double.valueOf(Math.pow(10, maxV)).intValue();
                maxV = maxV - 1;
            }
            return String.valueOf(maxV);
        }
        return "9";
    }

    public String getMinValue() {
        if ("String".equals(this.getAttrType())) {
            return this.getColIsNull() ? "0" : "1";
        }
        else if (this.isInt() || this.isFloat()) {
            return "0";
        }
        return "0";
    }

    public Set<String> getImportAnnotationsList() {
        Set<String> r = new HashSet<String>();
        // 字段是否为空
        if (!this.getColIsNull()) {
            r.add("javax.validation.constraints.NotNull");
            if (this.getAttrType().startsWith("ZKJson")) {
                r.add("javax.validation.constraints.NotEmpty");
            }
        }
        // 字段长度
        if (this.getColLength() != null) {
            if ("String".equals(this.getAttrType())) {
                r.add("org.hibernate.validator.constraints.Length");
            }
            else if (this.isInt() || this.isFloat()) {
                r.add("org.hibernate.validator.constraints.Range");
            }
        }
        else {
            if ("String".equals(this.getAttrType())) {
                r.add("org.hibernate.validator.constraints.Length");
            }
            else if (this.isInt() || this.isFloat()) {
                r.add("org.hibernate.validator.constraints.Range");
            }
        }
        // 日期类型
        if ("Date".equals(this.getAttrType())) {
            r.add("com.fasterxml.jackson.annotation.JsonFormat");
            r.add("com.fasterxml.jackson.annotation.JsonInclude");
        }
        // 字段映射注解
        r.add("com.zk.db.annotation.ZKColumn");
        // 字段修改注解信息
        if(this.getAttrIsUpdate()){
            r.add("com.zk.db.annotation.ZKUpdate");
        }
        // 字段查询注解信息
        if(!ZKStringUtils.isEmpty(this.getAttrQueryType())){
            r.add("com.zk.db.annotation.ZKQuery");
        }
        return r;
    }

    public Set<String> getImportList() {
        Set<String> r = new HashSet<String>();
        // 查询类型
        if (!ZKStringUtils.isEmpty(this.getAttrQueryType())) {
            r.add("com.zk.db.commons.ZKDBOptComparison");
        }
        // 日期类型
        if ("Date".equals(this.getAttrType())) {
            r.add("java.util.Date");
            r.add("com.zk.core.utils.ZKDateUtils");
        }
        else if ("String".equals(this.getAttrType())) {
            r.add("java.lang.String");
        }
        else if ("BigDecimal".equals(this.getAttrType())) {
            r.add("java.math.BigDecimal");
        }
        else if ("BigInteger".equals(this.getAttrType())) {
            r.add("java.math.BigInteger");
        }
        else if (this.getAttrType().startsWith("ZKJson")) {
            r.add("com.zk.core.commons.data.ZKJson");
        }

        return r;
    }

    public List<String> getFieldAnnotations() {
        String annotStr = "";
        List<String> filedAnnotations = new ArrayList<>();
        // 字段是否为空
        if (!this.getColIsNull()) {
            annotStr = "@NotNull(message = \"{zk.core.data.validation.notNull}\")";
            filedAnnotations.add(annotStr);
            if (this.getAttrType().startsWith("ZKJson")) {
                filedAnnotations.add("@NotEmpty(message = \"{zk.core.data.validation.notNull}\")");
            }
        }
        // 字段长度
        if ("String".equals(this.getAttrType())) {
            annotStr = String.format("@Length(min = %s, max = %s, message = \"{zk.core.data.validation.length.max}\")",
                    this.getMinValue(), this.getMaxValue());
            filedAnnotations.add(annotStr);
        }
        else if (this.isInt()) {
            annotStr = String.format("@Range(min = %s, max = %s, message = \"{zk.core.data.validation.rang.int}\")",
                    this.getMinValue(), this.getMaxValue());
            filedAnnotations.add(annotStr);
        }
        else if (this.isFloat()) {
            annotStr = String.format("@Range(min = %s, max = %s, message = \"{zk.core.data.validation.rang}\")",
                    this.getMinValue(), this.getMaxValue());
            filedAnnotations.add(annotStr);
        }

        // 日期类型相关注解
        if ("Date".equals(this.getAttrType())) {
            annotStr = "@JsonInclude(value = JsonInclude.Include.NON_EMPTY)";
            filedAnnotations.add(annotStr);
            annotStr = "@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)";
            filedAnnotations.add(annotStr);
        }

        // 字段注解
//        formats = "%Y-%m-%d %H:%i:%S", 
        StringBuffer sb = new StringBuffer();
        sb = sb.append("@ZKColumn(");
        // 字段基本信息
        sb = sb.append("name = \"").append(this.getColName()).append("\", ");
        sb = sb.append("isInsert = ").append(this.getAttrIsInsert()).append(", ");
        sb = sb.append("javaType = ").append(this.getAttrType().replaceAll("<(.*)>", "")).append(".class");
        if ("Date".equals(this.getAttrType())) {
            sb = sb.append(", formats = \"").append("%Y-%m-%d %H:%i:%S").append("\"");
        }
        // 字段修改注解信息
        if(this.getAttrIsUpdate()){
            // 字段修改
            sb = sb.append(", update = @ZKUpdate(true)");
        }
        // 字段查询注解信息
        if(!ZKStringUtils.isEmpty(this.getAttrQueryType())){
            // 字段是查询条件
            sb = sb.append(", query = @ZKQuery(queryType = ZKDBOptComparison.").append(this.getAttrQueryType());
            sb = sb.append(")");
        }
        sb = sb.append(")");
        annotStr = sb.toString();

        filedAnnotations.add(annotStr);
        return filedAnnotations;
    }

    public boolean isFloat() {
        return ZKConvertUtils.isFloat(this.getAttrType());
    }

    public boolean isInt() {
        return ZKConvertUtils.isInt(this.getAttrType());
    }

}
