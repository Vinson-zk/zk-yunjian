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
* @Title: ZKTableInfo.java 
* @author Vinson 
* @Package com.zk.code.generate.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 4:18:18 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.entity;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * @ClassName: ZKTableInfo
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKTable(name = "t_dt_code_gen_table_info", alias = "tableInfo", orderBy = " c_create_date ASC ")
public class ZKTableInfo extends ZKBaseEntity<String, ZKTableInfo> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKTableInfo());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKTableInfo() {
        super();
    }

    public ZKTableInfo(String pkId) {
        super(pkId);
    }

    /**********************************************************************************/
    /*** 表信息 */
    /**********************************************************************************/
    /**
     * 表名；和模块名组成唯一ID
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_table_name",
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LIKE))
    String tableName;

    /**
     * 表：描述，与JAVA类属性注释共用
     */
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 0, max = 512, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_table_comments", update = @ZKUpdate(true))
    String tableComments = "";

    /**
     * 主键字段名
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_pk_col_name", update = @ZKUpdate(true))
    String pkColName;

    /**********************************************************************************/
    /***  */
    /**********************************************************************************/

    /**
     * java 实体类名，首字母大写
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_class_name", update = @ZKUpdate(true))
    String className;

    /**
     * java 功能子模块名, 默认没有；
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_sub_module_name", update = @ZKUpdate(true))
    String subModuleName;

    /**
     * 功能名，首字母大写
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_func_name", update = @ZKUpdate(true))
    String funcName;

    /**
     * 是否是树形结构; true-是树形结构；false-不是树形结构；默认: false；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_is_tree", update = @ZKUpdate(true))
    boolean isTree = false;

    /**
     * 表标签
     */
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_label", update = @ZKUpdate(true))
    String label;

    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_icon", update = @ZKUpdate(true))
    String icon;

    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_nav_code", update = @ZKUpdate(true),
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LIKE))
    String navCode;

    /**********************************************************************************/
    /*** 其他 一些代码生成的辅助信息 */
    /**********************************************************************************/

    /**
     * 是否是树形结构; true-是树形结构；false-不是树形结构；默认: false；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 20, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_module_id", update = @ZKUpdate(true),
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.EQ))
    String moduleId;

    /**
     * 功能模块信息
     */
    ZKModule module;

    /**
     * java 字段信息明细链表
     */
    Collection<ZKColInfo> cols;

    /**
     * @return tableName sa
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName
     *            the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return tableComments sa
     */
    public String getTableComments() {
        return tableComments;
    }

    /**
     * @param tableComments
     *            the tableComments to set
     */
    public void setTableComments(String tableComments) {
        this.tableComments = tableComments;
    }

    /**
     * @return pkColName sa
     */
    public String getPkColName() {
        return pkColName;
    }

    /**
     * @param pkColName
     *            the pkColName to set
     */
    public void setPkColName(String pkColName) {
        this.pkColName = pkColName;
    }

    /**
     * @return subModuleName sa
     */
    public String getSubModuleName() {
        return subModuleName == null ? "" : ZKStringUtils.capitalize(subModuleName);
    }

    /**
     * @param subModuleName
     *            the subModuleName to set
     */
    public void setSubModuleName(String subModuleName) {
        this.subModuleName = subModuleName;
    }

    /**
     * 取原类名，首字母大写
     * 
     * @return className sa
     */
    public String getClassName() {
        return ZKStringUtils.capitalize(className);
    }

    /**
     * @param className
     *            the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return funcName sa
     */
    public String getFuncName() {
        return ZKStringUtils.capitalize(funcName);
    }

    /**
     * @param funcName
     *            the funcName to set
     */
    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    /**
     * @return getIsTree sa
     */
    public boolean getIsTree() {
        return isTree;
    }

    /**
     * @param isTree
     *            the isTree to set
     */
    public void setIsTree(boolean isTree) {
        this.isTree = isTree;
    }

    /**
     * @return moduleId sa
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId
     *            the moduleId to set
     */
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * @return module sa
     */
    public ZKModule getModule() {
        return module;
    }

    /**
     * @param module
     *            the module to set
     */
    public void setModule(ZKModule module) {
        this.module = module;
    }

    /**
     * @return cols sa
     */
    public Collection<ZKColInfo> getCols() {
        return cols == null ? Collections.emptyList() : cols;
    }

    /**
     * @param cols
     *            the cols to set
     */
    public void setCols(Collection<ZKColInfo> cols) {
        this.cols = cols;
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

    /**
     * @return icon sa
     */
    public String getIcon() {
        return icon == null ? "" : icon;
    }

    /**
     * @return navCode sa
     */
    public String getNavCode() {
        return navCode == null ? "" : navCode;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @param navCode
     *            the navCode to set
     */
    public void setNavCode(String navCode) {
        this.navCode = navCode;
    }


    /**********************************************************************************/
    /*** 计算信息 */
    /**********************************************************************************/

    /**
     * @return pkAttrType 返回主键的 属性类型；
     */
    public String getPkAttrType() {
        for (ZKColInfo m : this.getCols()) {
            if (this.getPkColName().equals(m.getColName())) {
                return m.getAttrType();
            }
        }
        return "String";
    }

    public ZKColInfo getPkCol() {
        if (this.getCols() != null) {
            for (ZKColInfo col : this.getCols()) {
                if (col.colIsPK) {
                    return col;
                }
            }
        }
        return null;
    }

    /**
     * 需要引入的 类
     *
     * @Title: getImportList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 2:11:09 PM
     * @return
     * @return Set<String>
     */
    public Set<String> getImportList() {
        Set<String> r = new HashSet<String>();
        for (ZKColInfo m : this.getCols()) {
            if (m.getAttrIsBaseField()) {
                continue;
            }
            r.addAll(m.getImportList());
        }
        if ("String".equals(this.getPkAttrType()) || ("Long".equals(this.getPkAttrType()))) {
            r.add("com.zk.core.utils.ZKIdUtils");
        }
        
        return r;
    }

    public Set<String> getImportAnnotationsList() {
        Set<String> r = new HashSet<String>();
        for (ZKColInfo m : this.getCols()) {
            if (m.getAttrIsBaseField()) {
                continue;
            }
            r.addAll(m.getImportAnnotationsList());
        }
        return r;
    }

    /**
     * 判断指定字段名是否存在
     *
     * @Title: colIsExists
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 2:11:14 PM
     * @param colName
     * @return
     * @return boolean
     */
    public boolean colIsExists(String colName) {
        for (ZKColInfo m : this.getCols()) {
            if (colName.equals(m.getColName())) {
                return true;
            }
        }
        return false;
    }

    public String getSubModuleNamePath() {
        if (ZKStringUtils.isEmpty(this.getSubModuleName())) {
            return "";
        }
        return ZKStringUtils.replaceEach(ZKStringUtils.uncapitalize(this.getSubModuleName()),
                new String[] { "//", "/", "." },
                new String[] { File.separator, File.separator, File.separator });
    }

    // 判断字段中是否有 图标编辑
    public boolean getIsHasIconEdit() {
        for (ZKColInfo m : this.getCols()) {
            if (ZKColInfo.KeyEditStrategy.EditIcon.equals(m.getEditStrategy())) {
                return true;
            }
        }
        return false;
    }
}
