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
* @Title: ZKSysNav.java 
* @author Vinson 
* @Package com.zk.sys.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2020 12:19:24 PM 
* @version V1.0 
*/
package com.zk.sys.res.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.data.ZKJson;
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
* @ClassName: ZKSysNav 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_sys_res_navigation", alias = "sysNav", orderBy = " c_create_date ASC ")
public class ZKSysNav extends ZKBaseEntity<String, ZKSysNav> {

    /**
     * 0-不显示；1-显示；
     */
    public static interface KeyIsShow {
        /**
         * 0-不显示;
         */
        public static final int hide = 0;

        /**
         * 1-显示；
         */
        public static final int show = 1;
    }

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysNav());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKSysNav() {
        super();
    }

    public ZKSysNav(String pkId) {
        super(pkId);
    }

    /**
     * 不能为空；菜单(路由)的名称, 国际化json对象；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE, isCaseSensitive = false))
    protected ZKJson name;

    /* 前端路由相关的属性 */

    /**
     * 不能为空；导航栏目代码，唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_code", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String code;

    /**
     * 不能为空；功能模块代码；也是功能模块目录;
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_func_module_code", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String funcModuleCode;

    /**
     * 不能为空，功能名称，导航栏的首页组件名；将根据这名称查找到对应功能组件；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_func_name", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String funcName;

    /**
     * 访问路径；不能为空
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_path", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String path;

    /**
     * 不能为空；排序字段
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_sort", javaType = Integer.class, update = @ZKUpdate(true))
    protected Integer sort;

    /**
     * 是否是默认栏目，0-不是；1-是；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_is_index", javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    protected Integer isIndex;

    /**
     * zk.core.data.validation.rang.int 是否显示；0-不显示；1-显示；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_is_show", javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    protected Integer isShow;

    /**
     * 图标
     */
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_icon", update = @ZKUpdate(true))
    protected String icon;

    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    String searchValue;

    /**
     * @return name sa
     */
    public ZKJson getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(ZKJson name) {
        this.name = name;
    }

    @Transient
    public void putName(String key, String value) {
        if (this.name == null) {
            this.name = new ZKJson();
        }
        this.name.put(key, value);
    }

    /**
     * @return navCode sa
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the navCode to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return funcModuleCode sa
     */
    public String getFuncModuleCode() {
        return funcModuleCode;
    }

    /**
     * @param funcModuleCode
     *            the funcModuleCode to set
     */
    public void setFuncModuleCode(String funcModuleCode) {
        this.funcModuleCode = funcModuleCode;
    }

    /**
     * @return funcName sa
     */
    public String getFuncName() {
        return funcName;
    }

    /**
     * @param funcName
     *            the funcName to set
     */
    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    /**
     * @return path sa
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return sort sa
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return isIndex sa
     */
    public Integer getIsIndex() {
        return isIndex;
    }

    /**
     * @param isIndex
     *            the isIndex to set
     */
    public void setIsIndex(Integer isIndex) {
        this.isIndex = isIndex;
    }

    /**
     * @return isShow sa
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * @param isShow
     *            the isShow to set
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    /**
     * @return icon sa
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

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

}
